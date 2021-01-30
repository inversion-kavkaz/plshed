package ru.inversion.plshed.interfaces;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 17:09
 * @project plshed
 */


public interface TaskCallBack {
    void onTaskStart(Long taskId);
    void onTaskFinish(Long taskId,Long code);
    void onEventStart(Long enentID);
    void onEventFinish(Long enentID);
}
