package ru.inversion.plshed.utils;

import ru.inversion.bicomp.util.ParamMap;
import ru.inversion.db.expr.SQLExpressionException;
import ru.inversion.fx.form.AbstractBaseController;
import ru.inversion.tc.TaskContext;

import java.net.URL;

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
            return null;
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
            return;
        }
    }



}
