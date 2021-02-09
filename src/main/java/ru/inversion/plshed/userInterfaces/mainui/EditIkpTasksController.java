package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import ru.inversion.fx.app.AppException;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.*;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;

import static lovUtils.LovUtils.initCombobox;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class EditIkpTasksController extends JInvFXFormController <PIkpTasks>{

    @FXML CheckBox MON;
    @FXML CheckBox TUE;
    @FXML CheckBox WED;
    @FXML CheckBox THU;
    @FXML CheckBox FRI;
    @FXML CheckBox SAT;
    @FXML CheckBox SUN;
    @FXML JInvTextField EXCEPTDAY;
    @FXML JInvCalendar DTASKFROMDT;
    @FXML  JInvLongField ITASKINTERVAL;
    @FXML JInvComboBox<Long, String> ITASKPERIOD;
    @FXML JInvTimeField DTASKFROMTMV;
    @FXML JInvComboBox<Long, String> ITASKFREQUENCY;
    @FXML JInvComboBox<Long, String> ITASKSIDE;
    @FXML JInvComboBox<Long, String> BTASKRUNNING;
    @FXML JInvComboBox<Long, String> RUNNINGEVENT;

    private final Long DISABLED_INTERVAL_FIELD = 0l;
    private final Long DISABLED_TIME_FIELD = 1l;

    @Override
    protected void init () throws Exception 
    {
        super.init ();
        initComboBox();
    }

    @Override
    protected void afterInit() throws AppException {
        super.afterInit();
        String str = EXCEPTDAY.getText();
        if(str.contains("1")) MON.setSelected(true);
        if(str.contains("2")) TUE.setSelected(true);
        if(str.contains("3")) WED.setSelected(true);
        if(str.contains("4")) THU.setSelected(true);
        if(str.contains("5")) FRI.setSelected(true);
        if(str.contains("6")) SAT.setSelected(true);
        if(str.contains("7")) SUN.setSelected(true);
    }

    private void initComboBox() throws ru.inversion.dataset.DataSetException {
        initCombobox(getTaskContext(),BTASKRUNNING, PIkpRunningTextValue.class);
        initCombobox(getTaskContext(),ITASKSIDE, PIkpRunningSideTextValue.class);
        initCombobox(getTaskContext(),ITASKFREQUENCY, PIkpFrequencyTextValue.class);
        initCombobox(getTaskContext(),ITASKPERIOD, PIkpPeriodTextValue.class).setOnAction(event -> {
            boolean isInterval = (((JInvComboBox) event.getSource()).getValue() == DISABLED_INTERVAL_FIELD);
            ITASKINTERVAL.setDisable(isInterval);
            ITASKFREQUENCY.setDisable(isInterval);
            ITASKINTERVAL.setRequired(!isInterval);
        });
        initCombobox(getTaskContext(),RUNNINGEVENT, PIkpRunningEventTextValue.class).setOnAction(event -> {
            DTASKFROMTMV.setDisable((((JInvComboBox) event.getSource()).getValue() == DISABLED_TIME_FIELD));
            DTASKFROMDT.setDisable((((JInvComboBox) event.getSource()).getValue() == DISABLED_TIME_FIELD));
        });;
    }

    @Override
    protected boolean onOK() {
        StringBuilder resultDayString = new StringBuilder();
        if(MON.isSelected()) resultDayString.append(1);
        if(TUE.isSelected()) resultDayString.append(2);
        if(WED.isSelected()) resultDayString.append(3);
        if(THU.isSelected()) resultDayString.append(4);
        if(FRI.isSelected()) resultDayString.append(5);
        if(SAT.isSelected()) resultDayString.append(6);
        if(SUN.isSelected()) resultDayString.append(7);
        EXCEPTDAY.setText(resultDayString.toString());
        return super.onOK();
    }
}

