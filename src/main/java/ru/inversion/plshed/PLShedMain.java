package ru.inversion.plshed;

import java.util.Collections;
import java.util.Map;

import lombok.SneakyThrows;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.fx.app.BaseApp;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.plshed.mainWin.ViewIkpTasksController;
import ru.inversion.plshed.utils.dataSetUtils;
import ru.inversion.tc.TaskContext;

/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class PLShedMain extends BaseApp
{

    void run(){
        System.out.println("asadsdas");
    }

    @Override
    protected void showMainWindow () 
    {
        showViewIkpTasksa (getPrimaryViewContext (), new TaskContext (), Collections.emptyMap ());
    }

    @Override
    public String getAppID () 
    {
        return "PLShed";
    }
    
    public static void main (String[] args) 
    {
        launch (args);
    }

    public static void showViewIkpTasksa (ViewContext vc, TaskContext tc, Map<String, Object> p)
    {
        dataSetUtils.runDI(tc);

        new FXFormLauncher<> (tc, vc, ViewIkpTasksController.class)
            .initProperties (p)
            .show ();

    }
    
}

