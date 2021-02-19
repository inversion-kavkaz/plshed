package ru.inversion.plshed.userInterfaces.presetsview;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.plshed.entity.PIkpEventPresets;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;

import static lovUtils.LovUtils.initCombobox;

/**
 * @author  XDWeloper
 * @since   Wed Feb 17 12:29:57 MSK 2021
 */
public class EditIkpEventPresetsController extends JInvFXFormController <PIkpEventPresets>
{  
//
//
//
//    @FXML JInvTextField IPRESETID;
//    @FXML JInvTextField CPRESETNAME;
//    @FXML JInvTextField CPRESETTEXT;
    @FXML JInvComboBox<Long, String> IEVENTFILEDIR;
//
// Initializes the controller class.
//
    @Override
    protected void init () throws Exception 
    {
        super.init ();
        initCombobox(getTaskContext(), IEVENTFILEDIR, PIkpEventFileTypeTextValue.class);
    }    

}

