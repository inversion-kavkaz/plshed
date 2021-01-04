package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXBrowserController;
import ru.inversion.fx.form.controls.JInvTextArea;

/**
 * @author Dmitry Hvastunov
 * @created 31 Декабрь 2020 - 11:35
 * @project plshed
 */

public class HelpWindowController extends JInvFXBrowserController {


    @FXML private JInvTextArea HELPTEXTAREA;

    private final String EVENT_SCRIPT_START_TEXT =
            " в данную функцию передаются следующие переменные: \n" +
            " eventnpp - порядковый номер события в задании \n" +
            " eventid - ид события \n" +
            " eventresult - результат работы предыдущего события тип Object\n" +
            " connection - conection текущего события\n" +
            " результат выполнения можно записать в return - для передачи следующему событию. \n" +
            " код выполняется по синтаксическим правилам языка java. ";


    @Override
    protected void init() throws Exception {
        super.init();
        HELPTEXTAREA.setText(EVENT_SCRIPT_START_TEXT);
    }
}
