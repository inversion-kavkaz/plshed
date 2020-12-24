package ru.inversion.plshed.utils;

import ru.inversion.bicomp.util.ParamMap;
import ru.inversion.db.expr.SQLExpressionException;
import ru.inversion.fx.form.AbstractBaseController;
import ru.inversion.tc.TaskContext;

import java.io.File;
import java.net.URL;
import java.sql.Blob;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 16:11
 * @project plshed
 */

public class SqlUtils extends AbstractBaseController {

    private static URL location = SqlUtils.class.getResource("plsql/def.xml");;


    public static Long initTask(Long taskID,TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("taskID", taskID)
                    .exec(taskContext, location,"initTask");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return -100L;
        }
        return p.getLong("result");
    }

    public static void runTask(Long taskID,TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("taskID", taskID)
                    .exec(taskContext, location,"runTask");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return;
        }
    }

    public static Long initEvent(Long eventPP,TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("eventPP", eventPP)
                    .exec(taskContext, location,"initEvent");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return -1L;
        }
        return p.getLong("result");
    }

    public static Long execEvent(Long eventPP,TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("eventPP", eventPP)
                    .exec(taskContext, location,"execEvent");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return -1L;
        }
        return p.getLong("result");
    }

    public static void finishTask(Long taskID,TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("taskID", taskID)
                    .exec(taskContext, location,"finishTask");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return ;
        }
    }

    public static void fileSave(File lFile, TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("lFile", lFile)
                    .exec(taskContext, location,"fileSave");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return ;
        }
    }

    public static void setEventFileName(Long eventPP,String fileName, TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("eventPP", eventPP)
                    .add("fileName", fileName)
                    .exec(taskContext, location,"SetEventFileName");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return ;
        }
    }

    public static String GetEventFileName(Long eventPP, TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .add("eventPP", eventPP)
                    .exec(taskContext, location,"GetFileName");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return "";
        }
        return p.getString("result");
    }

    public static Blob GetFile(TaskContext taskContext) {
        ParamMap p;
        try {
            p = new ParamMap()
                    .exec(taskContext, location,"GetFile");
        } catch (SQLExpressionException ex) {
            logger.error(ex.getContentText());
            return null;
        }
        return (Blob) p.getParam("result");
    }

}
