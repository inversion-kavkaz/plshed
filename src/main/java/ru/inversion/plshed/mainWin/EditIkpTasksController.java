package ru.inversion.plshed.mainWin;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvTextField;
import ru.inversion.fx.form.controls.JInvTimeField;
import ru.inversion.plshed.entity.lovEntity.PIkpFrequencyTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpPeriodTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpRunningSideTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpRunningTextValue;
import ru.inversion.plshed.entity.PIkpTasks;

import static ru.inversion.plshed.utils.LovUtils.initCombobox;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class EditIkpTasksController extends JInvFXFormController <PIkpTasks>
{  
//
//
//
//    @FXML JInvLongField ITASKID;
//    @FXML JInvTextField CTASKNAME;
    @FXML JInvComboBox<Long, String> ITASKPERIOD;
//    @FXML JInvTextField DTASKFROMDT;
    @FXML JInvTextField DTASKFROMTM;

    @FXML JInvTimeField DTASKFROMTMV;
    @FXML JInvComboBox<Long, String> ITASKFREQUENCY;
//    @FXML JInvComboBox<Long, String> ITASKINTERVAL;
    @FXML JInvComboBox<Long, String> ITASKSIDE;
    @FXML JInvComboBox<Long, String> BTASKRUNNING;


    @Override
    protected void init () throws Exception 
    {
        super.init ();
        initCombobox(getTaskContext(),BTASKRUNNING, PIkpRunningTextValue.class);
        initCombobox(getTaskContext(),ITASKSIDE, PIkpRunningSideTextValue.class);
        initCombobox(getTaskContext(),ITASKFREQUENCY, PIkpFrequencyTextValue.class);
        initCombobox(getTaskContext(),ITASKPERIOD, PIkpPeriodTextValue.class);

//        initCombobox(getTaskContext(),BTASKRUNNING, PIkpRunningTextValue.class, PIkpRunningTextValue.class);
//        initCombobox(getTaskContext(),ITASKSIDE, PIkpRunningSideTextValue.class, PIkpRunningSideTextValue.class);
//        initCombobox(getTaskContext(),ITASKFREQUENCY, PIkpFrequencyTextValue.class, PIkpFrequencyTextValue.class);
//        initCombobox(getTaskContext(),ITASKPERIOD, PIkpPeriodTextValue.class, PIkpPeriodTextValue.class);
//
    }

    @Override
    protected boolean onOK() {
        getFXEntity().setValue("DTASKFROMTM",DTASKFROMTMV.getText());
        return super.onOK();
    }
}

