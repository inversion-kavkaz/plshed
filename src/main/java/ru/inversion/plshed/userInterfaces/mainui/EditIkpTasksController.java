package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvComboBox;
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
//    @FXML JInvTextField DTASKFROMDT;
//    @FXML JInvTextField DTASKFROMTM;
//    @FXML JInvComboBox<Long, String> ITASKINTERVAL;

    @FXML JInvComboBox<Long, String> ITASKPERIOD;
    @FXML JInvTimeField DTASKFROMTMV;
    @FXML JInvComboBox<Long, String> ITASKFREQUENCY;
    @FXML JInvComboBox<Long, String> ITASKSIDE;
    @FXML JInvComboBox<Long, String> BTASKRUNNING;
    @FXML JInvComboBox<Long, String> RUNNINGEVENT;


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
        initCombobox(getTaskContext(),ITASKPERIOD, PIkpPeriodTextValue.class);
        initCombobox(getTaskContext(),RUNNINGEVENT, PIkpRunningEventTextValue.class);

    }

}

