package ru.inversion.plshed;

import javafx.application.Platform;
import ru.inversion.fx.app.BaseApp;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.plshed.userInterfaces.mainui.ViewIkpTasksController;
import ru.inversion.tc.TaskContext;

import java.util.Collections;
import java.util.Map;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class PLShedMain extends BaseApp {

//    public static List<ProcessInfo> processesList;
//    public static boolean isRunning = false;

    @Override
    protected void showMainWindow() {

/**Это читает все процессы в памяти и исключает повторный запуск модуля
 * решили отказаться на терминалке не вариант
 * код оставлю здесь вдруг понядобится
 * */
//        Long startTime = System.currentTimeMillis();
//        processesList = JProcesses.getProcessList("java.exe");
//        isRunning = processesList.stream().filter(p -> p.getCommand().contains(BaseApp.APP().getAppID())).count() > 1;
//        Long endTime = System.currentTimeMillis();
//        appLog.info(String.format("Processed time = %d",(endTime - startTime)));


        Platform.setImplicitExit(false);
        showViewIkpTasksa(getPrimaryViewContext(), new TaskContext(), Collections.emptyMap());
    }

    @Override
    public String getAppID() {
        return "PLShed";
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showViewIkpTasksa(ViewContext vc, TaskContext tc, Map<String, Object> p) {
//        TestClass.connection = tc.getConnection();
//        TestClass.CustomClass.CustomFunction("555");


        new FXFormLauncher(tc, vc, ViewIkpTasksController.class)
                .initProperties(p)
                .show();
    }


}

