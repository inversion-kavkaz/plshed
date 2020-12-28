package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvCalendar;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvLongField;
import ru.inversion.fx.form.controls.JInvTimeField;
import ru.inversion.plshed.entity.lovEntity.*;
import ru.inversion.plshed.entity.PIkpTasks;

import static lovUtils.LovUtils.initCombobox;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class EditIkpTasksController extends JInvFXFormController <PIkpTasks>
{  
//    @FXML JInvLongField ITASKID;
//    @FXML JInvTextField CTASKNAME;
    @FXML JInvCalendar DTASKFROMDT;
//    @FXML JInvTextField DTASKFROMTM;
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

    private void initComboBox() throws ru.inversion.dataset.DataSetException {
        initCombobox(getTaskContext(),BTASKRUNNING, PIkpRunningTextValue.class);
        initCombobox(getTaskContext(),ITASKSIDE, PIkpRunningSideTextValue.class);
        initCombobox(getTaskContext(),ITASKFREQUENCY, PIkpFrequencyTextValue.class);
        initCombobox(getTaskContext(),ITASKPERIOD, PIkpPeriodTextValue.class).setOnAction(event -> {
            ITASKINTERVAL.setDisable((((JInvComboBox) event.getSource()).getValue() == DISABLED_INTERVAL_FIELD));
        });
        initCombobox(getTaskContext(),RUNNINGEVENT, PIkpRunningEventTextValue.class).setOnAction(event -> {
            DTASKFROMTMV.setDisable((((JInvComboBox) event.getSource()).getValue() == DISABLED_TIME_FIELD));
            DTASKFROMDT.setDisable((((JInvComboBox) event.getSource()).getValue() == DISABLED_TIME_FIELD));
        });;

    }

}

