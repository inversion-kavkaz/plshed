package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import ru.inversion.fx.app.AppException;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvCalendar;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvLongField;
import ru.inversion.fx.form.controls.JInvTimeField;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;

import java.util.Arrays;
import java.util.stream.IntStream;

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
    @FXML HBox DAYCHECK;
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
        Arrays.asList(getFXEntity().getValue("EXCEPTDAY").toString().split(""))
                .forEach(a -> {
                    if(!a.isEmpty())
                        ((CheckBox)DAYCHECK.getChildren().get(Integer.valueOf(a) - 1)).setSelected(true);
                });
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
        IntStream.range(0,7)
                .forEach(p -> {
                    if(((CheckBox)DAYCHECK.getChildren().get(p)).isSelected())
                        resultDayString.append(p + 1);
                });
        getFXEntity().setValue("EXCEPTDAY",resultDayString.toString());
        return super.onOK();
    }
}

