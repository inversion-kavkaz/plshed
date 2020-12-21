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

    @Override
    protected void showMainWindow() {
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
        new FXFormLauncher(tc, vc, ViewIkpTasksController.class)
                .initProperties(p)
                .show();
    }

}

