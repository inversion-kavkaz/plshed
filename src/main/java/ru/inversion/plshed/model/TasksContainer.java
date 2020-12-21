package ru.inversion.plshed.model;

import org.slf4j.Logger;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.tc.TaskContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Hvastunov
 * @created 18 Декабрь 2020 - 12:16
 * @project plshed
 */

public class TasksContainer {

    private Map<Long, Task> tasksList = new HashMap<>();
    private Logger logger;
    TaskContext taskContext;
    TaskCallBack taskCallBack;

    public TasksContainer(Logger logger,TaskContext taskContext,TaskCallBack taskCallBac) {
        this.logger = logger;
        this.taskContext = taskContext;
        this.taskCallBack = taskCallBac;
    }

    public void initTask(PIkpTasks pIkpTasks) {
        if(tasksList.containsKey(pIkpTasks.getITASKID()))
            return;

        tasksList.put(pIkpTasks.getITASKID(), Task.taskFactory(pIkpTasks,logger,taskContext, taskCallBack));
    }

    public Task startTask(Long taskId){
        return tasksList.get(taskId).startTask();
    }

    public void updateTask(PIkpTasks pIkpTasks) {
        if(tasksList.containsKey(pIkpTasks.getITASKID())) {
            tasksList.replace(pIkpTasks.getITASKID(), Task.taskFactory(pIkpTasks,logger,taskContext,taskCallBack));
        } else
            initTask(pIkpTasks);
    }

    public void deleteTask(Long taskId){
        tasksList.remove(taskId);
    }


}
