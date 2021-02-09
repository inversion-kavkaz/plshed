package ru.inversion.plshed.model;

import lombok.Data;
import org.slf4j.Logger;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.tc.TaskContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static ru.inversion.plshed.utils.DateUtils.getNextDate;
import static ru.inversion.plshed.utils.SqlUtils.*;

/**
 * @author Dmitry Hvastunov
 * @created 18 Декабрь 2020 - 12:17
 * @project plshed
 */


@Data
public class Task {
    /**ALL 1, DEBUG 2, INFO 3,ERROR 4*/
    private final Long START_LOG_LEVEL = 4L;
    private final Long EVENT_ENEBLED = 1L;
    private final Long EVENT_TYPE = 2L;


    private PIkpTasks pIkpTasks;
    private Boolean isLaunch;
    private Boolean isWork = false;
    private TaskContext localTaskContext;
    private TaskContext taskContext;
    private Logger logger;
    private TaskCallBack taskCallBack;
    private Boolean isPeriod;
    private LocalDateTime nextStart = null;
    private Thread taskThread = null;

    private XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS;


    public enum StartType {Forced, Timer}

    public static Task taskFactory(PIkpTasks pIkpTasks, Logger logger, TaskContext taskContext, TaskCallBack taskCallBack) {
        return new Task(pIkpTasks, logger, taskContext, taskCallBack);
    }

    private Task(PIkpTasks pIkpTasks, Logger logger, TaskContext taskContext, TaskCallBack taskCallBack) {
        this.pIkpTasks = pIkpTasks;
        this.taskContext = taskContext;
        this.logger = logger;
        this.taskCallBack = taskCallBack;
        this.isPeriod = pIkpTasks.getITASKPERIOD() == 1L;
        this.isLaunch = pIkpTasks.getBTASKRUNNING() == 1;
        pIkpTasks.setLOGLEVEL(START_LOG_LEVEL);
        initDataSet(pIkpTasks, taskContext);
        if (isLaunch && isPeriod) {
            nextStart = pIkpTasks.getDTASKFROMTM();
            setNextStartDate();
        }

    }

    private void setNextStartDate() {
        nextStart = getNextDate(nextStart, pIkpTasks.getITASKFREQUENCY(), pIkpTasks.getITASKINTERVAL());
        pIkpTasks.setDTASKFROMTMMV(nextStart);
    }

    private void initDataSet(PIkpTasks pIkpTasks, TaskContext taskContext) {
        /**Инициализируем датасет с событиями*/
        this.dsIKP_TASK_EVENTS = new XXIDataSet<>(taskContext, PIkpTaskEvents.class);
        this.dsIKP_TASK_EVENTS.setFilter(String.format("IEVENTTASKID = %d", pIkpTasks.getITASKID()), false, false);
        this.dsIKP_TASK_EVENTS.orderBy("IEVENTNPP");
        try {
            dsIKP_TASK_EVENTS.execute();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
    }

    public Task startTask(StartType startType) {
        isWork = true;
        taskThread = new Thread(() -> {
            Object eventResult = null;
            localTaskContext = new TaskContext();
            logger.info(String.format("Start new thread task id: %d sessionID: %d", this.pIkpTasks.getITASKID(), localTaskContext.getSessionID()));

            try {
                /**Инициализация процесса*/
                Long initResult = initTask(pIkpTasks.getITASKID(), localTaskContext);
                if (initResult == -1) {
                    logger.info("Task already running");
                } else if (initResult == -100) {
                    logger.info("init task error");
                } else if (initResult == 0) {

                    /**Запуск процесса*/
                    runTask(pIkpTasks.getITASKID(), localTaskContext);

                    /**Обновить строку*/
                    taskCallBack.onTaskStart(pIkpTasks.getITASKID());

                    /**Запускаем работу с событиями*/
                    for (PIkpTaskEvents event : dsIKP_TASK_EVENTS.getRows()) {
                        if (event.getBEVENTENABLED() != EVENT_ENEBLED) continue;
                        initEvent(event.getIEVENTNPP(), localTaskContext);
                        taskCallBack.onEventStart(event.getIEVENTID());
                        /**Если это наш скритп запускаем обработку*/
                        if (event.getIEVENTTYPE() == EVENT_TYPE)
                            eventResult = runEventScript(event, eventResult);

                        /**Если вернули строку остановить задание*/
                        if (eventResult != null && String.valueOf(eventResult) == "StopTask")
                            break;

                        switch (event.getIEVENTFILEDIR().intValue()) {
                            case 0:
                                loadFromFileToDB(event, localTaskContext);
                                execEvent(event.getIEVENTNPP(), localTaskContext);
                                break;
                            case 1:
                                execEvent(event.getIEVENTNPP(), localTaskContext);
                                LoadFromDBToFile(event, localTaskContext);
                                break;
                            default:
                                execEvent(event.getIEVENTNPP(), localTaskContext);
                        }
                        taskCallBack.onEventFinish(event.getIEVENTID());
                    }
                    finishTask(pIkpTasks.getITASKID(), localTaskContext);
                }
                taskCallBack.onTaskFinish(pIkpTasks.getITASKID(),initResult);
                logger.info(String.format("Task result %d", initResult));
                if (startType == StartType.Timer)
                    setNextStartDate();
                isWork = false;
            } finally {
                finishTask(pIkpTasks.getITASKID(), localTaskContext);
                localTaskContext.close();
                logger.info(String.format("Stop thread is end task id: %d sessionID: %d is closed", this.pIkpTasks.getITASKID(), localTaskContext.getSessionID()));
            }
        });
        taskThread.setDaemon(true);
        taskThread.start();
        return this;
    }

    private Object runEventScript(PIkpTaskEvents event, Object preEventResult) {
        ScriptRunner scriptRunner = new ScriptRunner(logger, event.getLEVENTTEXT(), event, preEventResult, localTaskContext.getConnection());
        return scriptRunner.startScript();
    }


    private void loadFromFileToDB(PIkpTaskEvents event, TaskContext localTaskContext) {
        String loadFileDir = (event.getCEVENTINDIR() != null && !event.getCEVENTINDIR().isEmpty()) ? event.getCEVENTINDIR() : "";
        String loadFileArhDir = (event.getCEVENTARHDIR() != null && !event.getCEVENTARHDIR().isEmpty()) ? event.getCEVENTARHDIR() : "";

        if (loadFileDir.isEmpty() || !Files.exists(Paths.get(loadFileDir))) {
            logger.error(String.format("load file dir not find"));
            return;
        }

        File[] fileList = new File(loadFileDir).listFiles();
        if (fileList.length < 1) {
            logger.error(String.format("file not find"));
            return;
        }

        logger.info(String.format(" find one file: %s", fileList[0].getAbsolutePath()));

        /**Сохраняем файл в BLOB поле таблицы*/
        fileSave(new File(fileList[0].getAbsolutePath()), localTaskContext);
        /**Передаем имя файла в пакет*/
        setEventFileName(event.getIEVENTNPP(), fileList[0].getAbsolutePath(), localTaskContext);

        if (loadFileArhDir.isEmpty() || !Files.exists(Paths.get(loadFileArhDir))) {
            logger.error(String.format("arch file dir not find"));
            return;
        }

        try {
            Files.move(Paths.get(fileList[0].getAbsolutePath()),
                    Paths.get(loadFileArhDir.concat(File.separator).concat(fileList[0].getName())),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error(String.format("Error move file to arch dir"));
            return;
        }
    }

    private void LoadFromDBToFile(PIkpTaskEvents event, TaskContext localTaskContext) {
        String outFileDir = (event.getCEVENTOUTDIR() != null && !event.getCEVENTOUTDIR().isEmpty()) ? event.getCEVENTOUTDIR() : "";

        if (outFileDir.isEmpty() || !Files.exists(Paths.get(outFileDir))) {
            logger.error(String.format("out file dir not find"));
            return;
        }

        String newFileName = GetEventFileName(event.getIEVENTNPP(), localTaskContext);

        if (newFileName == null || newFileName.isEmpty()) {
            logger.error(String.format("The PL/SQL packet not return file name!"));
            return;
        }

        InputStream inputStream = null;

        try {
            inputStream = GetFile(localTaskContext).getBinaryStream();
        } catch (SQLException e) {
            logger.error(String.format("Error read file stream from data base"));
            return;
        }

        File fileOut = new File(outFileDir.concat(File.separator).concat(newFileName));
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOut);
            for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
                fileOutputStream.write(c);
            }

        } catch (IOException e) {
            logger.error(String.format("Error for get file out stream"));
            return;
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
