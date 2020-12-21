package ru.inversion.plshed.model;

import lombok.Data;
import org.slf4j.Logger;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.tc.TaskContext;

import static ru.inversion.plshed.utils.SqlUtils.initTask;
import static ru.inversion.plshed.utils.SqlUtils.runTask;

/**
 * @author Dmitry Hvastunov
 * @created 18 Декабрь 2020 - 12:17
 * @project plshed
 */


@Data
public class Task {
    private final Long START_LOG_LEVEL = 3L;

    private PIkpTasks pIkpTasks;
    private Boolean isLaunch;
    private Boolean isWork= false;
    private TaskContext localTaskContext;
    private TaskContext taskContext;
    private Logger logger;
    private TaskCallBack taskCallBack;
    private Boolean isPeriod;

    private XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS;

    public static Task taskFactory(PIkpTasks pIkpTasks, Logger logger,TaskContext taskContext,TaskCallBack taskCallBack){
        return new Task(pIkpTasks, logger, taskContext, taskCallBack);
    }


    private Task(PIkpTasks pIkpTasks, Logger logger,TaskContext taskContext,TaskCallBack taskCallBack)  {
        this.pIkpTasks = pIkpTasks;
        this.taskContext = taskContext;
        this.logger = logger;
        this.taskCallBack = taskCallBack;
        this.isPeriod = pIkpTasks.getITASKPERIOD() == 1L;
        this.isLaunch = pIkpTasks.getBTASKRUNNING() == 1 ;
        pIkpTasks.setLOGLEVEL(START_LOG_LEVEL);
        initDataSet(pIkpTasks, taskContext);

    }

    private void initDataSet(PIkpTasks pIkpTasks, TaskContext taskContext) {
        /**Инициализируем датасет с событиями*/
        this.dsIKP_TASK_EVENTS = new XXIDataSet<>(taskContext, PIkpTaskEvents.class);
        this.dsIKP_TASK_EVENTS.setFilter(String.format("IEVENTTASKID = %d",pIkpTasks.getITASKID()), false,false);
        try {
            dsIKP_TASK_EVENTS.execute();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
    }


    public Task startTask(){
        isWork = true;
        localTaskContext =  new TaskContext();
        new Thread(() -> {
            logger.info(String.format("Start task id: %d sessionID: %d", this.pIkpTasks.getITASKID()), localTaskContext.getSessionID());

            /**Инициализация процесса*/
            Long initResult = initTask(pIkpTasks.getITASKID(),localTaskContext);
            if(initResult == -1){
                logger.info("Task already running");
            } else if(initResult == -100){
                logger.info("Task error");
            }
            /**Запуск процесса*/
            runTask(pIkpTasks.getITASKID(),localTaskContext);


            taskCallBack.onTaskFinish(initResult);
            logger.info(String.format("Stop task id: %d sessionID: %d is closed", this.pIkpTasks.getITASKID()), localTaskContext.getSessionID());
            localTaskContext.close();
        }).run();
        return this;
    }




}
