package ru.inversion.plshed.interfaces;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 17:09
 * @project plshed
 */


public interface TaskCallBack {
    void onTaskFinish(Long code);
    void onEventFinish(Long enentID);
}
