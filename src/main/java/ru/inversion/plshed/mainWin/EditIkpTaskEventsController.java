package ru.inversion.plshed.mainWin;

import javafx.fxml.FXML;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvTextField;
import ru.inversion.fx.form.lov.JInvDirectoryChooserLov;
import ru.inversion.fx.form.valid.Validator;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.lovEntity.PIkpEventEnebledTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventTypeTextValue;
import java.io.File;
import java.nio.file.Files;
import static ru.inversion.plshed.utils.ButtonUtils.setInnerGraphicButton;
import static ru.inversion.plshed.utils.LovUtils.initCombobox;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */

public class EditIkpTaskEventsController extends JInvFXFormController <PIkpTaskEvents>
{  
//
//
//
//    @FXML JInvLongField IEVENTID;
//    @FXML JInvLongField IEVENTTASKID;
//    @FXML JInvLongField IEVENTNPP;
//    @FXML JInvTextField CEVENTNAME;
    @FXML JInvComboBox<Long, String> IEVENTTYPE;
//    @FXML JInvLongField IEVENTPRESETID;
//    @FXML JInvTextField LEVENTTEXT;
    @FXML JInvTextField CEVENTINDIR;
    @FXML JInvTextField CEVENTOUTDIR;
    @FXML JInvTextField CEVENTARHDIR;
    @FXML JInvComboBox<Long, String> BEVENTENABLED;
    @FXML JInvComboBox<Long, String> IEVENTFILEDIR;

//
// Initializes the controller class.
//
    @Override
    protected void init () throws Exception 
    {
        super.init ();

        initCombobox(getTaskContext(),IEVENTTYPE,  PIkpEventTypeTextValue.class);
        initCombobox(getTaskContext(),IEVENTFILEDIR, PIkpEventFileTypeTextValue.class);
        initCombobox(getTaskContext(),BEVENTENABLED, PIkpEventEnebledTextValue.class);

//        initCombobox(getTaskContext(),IEVENTTYPE, PIkpEventTypeTextValue.class, PIkpEventTypeTextValue.class);
//        initCombobox(getTaskContext(),IEVENTFILEDIR, PIkpEventFileTypeTextValue.class, PIkpEventFileTypeTextValue.class);
//        initCombobox(getTaskContext(),BEVENTENABLED, PIkpEventEnebledTextValue.class, PIkpEventEnebledTextValue.class);
//

        setInnerGraphicButton(CEVENTINDIR,FontAwesome.fa_search, event -> chooser(CEVENTINDIR), event -> validate(CEVENTINDIR));
        setInnerGraphicButton(CEVENTOUTDIR,FontAwesome.fa_search, event -> chooser(CEVENTOUTDIR), event -> validate(CEVENTOUTDIR));
        setInnerGraphicButton(CEVENTARHDIR,FontAwesome.fa_search, event -> chooser(CEVENTARHDIR), event -> validate(CEVENTARHDIR));

    }

    private void chooser(JInvTextField field) {
        JInvDirectoryChooserLov jInvDirectoryChooserLov = new JInvDirectoryChooserLov();
        if (field.getText() != null && !field.getText().trim().isEmpty()) {
            jInvDirectoryChooserLov.setInitialDirectory(new File(field.getText()));
        }
        jInvDirectoryChooserLov.showChoiceList(getViewContext(), null, (aBoolean, fileILov) -> {
            if (aBoolean && fileILov.getValue() != null)
                field.setText(fileILov.getValue().getAbsolutePath());
        });
    }

    private void validate(JInvTextField field){
        getValidMan().bindControls2Validator(o -> {
            if (o != null && !Files.exists(new File((String) o).toPath()))
                return new Validator.Result(getBundleString("DIRCHOOCE.ERROR1"), getBundleString("DIRCHOOCE.ERROR2"));
            return null;
        },field);
    }



}

