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
        return tasksList.get(taskId).startTask(Task.StartType.Timer);
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

    public void stopScheduler() {
        logger.info(String.format("Sheduler is stoped"));
        taskSheduler.interrupt();
    }

    public void initTaskScheduler() {
        taskSheduler = new Thread(() -> {
            logger.info("Start scheduler");
            while (!Thread.currentThread().isInterrupted()) {
                threadWait(WAIT_SECOND);
                LocalDateTime now = LocalDateTime.now();
                logger.info("search task for run");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                String nowDate = now.format(formatter);
                logger.info(String.format("now: %s", nowDate));
                tasksList.forEach((id, task) -> {
                    if (task.getNextStart() != null) {
                        String nextDate = task.getNextStart().format(formatter);
                        logger.info(String.format("task id: %d next start: %s ", id, nextDate));
                        if (nowDate.equals(nextDate)) {
                            logger.info(String.format("Start Task %d", id));
                            task.startTask(Task.StartType.Timer);
                        }
                    }
                });
            }
        });
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
