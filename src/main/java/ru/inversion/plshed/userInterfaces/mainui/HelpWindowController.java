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

    @Override
    protected void init() throws Exception {
        super.init();
        HELPTEXTAREA.setText(getBundleString("EVENT_SCRIPT_START_TEXT"));
    }
}
