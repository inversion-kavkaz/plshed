package ru.inversion.plshed.model;

import org.slf4j.Logger;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.tc.TaskContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Hvastunov
 * @created 18 Декабрь 2020 - 12:16
 * @project plshed
 */

public class TasksContainer {
    private final int WAIT_SECOND = 60;
    private final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    private Map<Long, Task> tasksList = new HashMap<>();
    private Logger logger;
    TaskContext taskContext;
    TaskCallBack taskCallBack;
    Thread taskSheduler = null;

    public TasksContainer(Logger logger, TaskContext taskContext, TaskCallBack taskCallBac) {
        this.logger = logger;
        this.taskContext = taskContext;
        this.taskCallBack = taskCallBac;
        initTaskScheduler();
    }

    public void initTask(PIkpTasks pIkpTasks) {
        if (tasksList.containsKey(pIkpTasks.getITASKID()))
            return;

        tasksList.put(pIkpTasks.getITASKID(), Task.taskFactory(pIkpTasks, logger, taskContext, taskCallBack));
    }

    public Task startTask(Long taskId) {
        return tasksList.get(taskId).startTask(Task.StartType.Forced);
    }

    public void updateTask(PIkpTasks pIkpTasks) {
        if (tasksList.containsKey(pIkpTasks.getITASKID())) {
            tasksList.replace(pIkpTasks.getITASKID(), Task.taskFactory(pIkpTasks, logger, taskContext, taskCallBack));
        } else
            initTask(pIkpTasks);
    }


    public void deleteTask(Long taskId) {
        tasksList.remove(taskId);
    }

    public void initTaskScheduler() {
        taskSheduler = new Thread(() -> {
            logger.info("Start scheduler");
            while (!Thread.currentThread().isInterrupted()) {
                threadWait(WAIT_SECOND);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
                String nowDate = now.format(formatter);
                tasksList.forEach((id, task) -> {
                    if (task.getNextStart() != null) {
                        String nextDate = task.getNextStart().format(formatter);
//                        logger.info(String.format("nextDate: %s nowDate: %s exceptday: %s nowDay: %s",nextDate,nowDate,
//                                task.getPIkpTasks().getEXCEPTDAY(),String.valueOf(now.getDayOfWeek().getValue())));
                        if (nowDate.equals(nextDate)
                                && !task.getPIkpTasks().getEXCEPTDAY().contains(String.valueOf(now.getDayOfWeek().getValue()))
                                && (task.getPIkpTasks().getDTASKTODT() == null
                                || task.getPIkpTasks().getDTASKTODT().isAfter(now.toLocalDate()))) {
                            logger.info(String.format("Start task %d",task.getPIkpTasks().getITASKID()));
                            task.startTask(Task.StartType.Timer);
                        }
                    }
                });
            }
        });
        taskSheduler.setDaemon(true);
        taskSheduler.start();
    }

    private void threadWait(int waitTimeSecond){
        try {
            Thread.sleep(waitTimeSecond * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
