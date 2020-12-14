package ru.inversion.plshed;

import java.util.Collections;
import java.util.Map;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.fx.app.BaseApp;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.plshed.mainWin.ViewIkpTasksController;
import ru.inversion.tc.TaskContext;

/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class PLShedMain extends BaseApp
{
    
    @Override
    protected void showMainWindow () 
    {
        showViewIkpTasksa (getPrimaryViewContext (), new TaskContext (), Collections.emptyMap ());
    }

    @Override
    public String getAppID () 
    {
        return "XXI.PLSHED";
    }
    
    public static void main (String[] args) 
    {
        launch (args);
    }

    public static void showViewIkpTasksa (ViewContext vc, TaskContext tc, Map<String, Object> p) 
    {
        System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
        new FXFormLauncher<> (tc, vc, ViewIkpTasksController.class)
            .initProperties (p)
            .show ();
    }
    
}

