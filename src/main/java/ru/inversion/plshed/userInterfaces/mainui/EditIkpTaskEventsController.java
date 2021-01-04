package ru.inversion.plshed.userInterfaces.mainui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvButton;
import ru.inversion.fx.form.controls.JInvComboBox;
import ru.inversion.fx.form.controls.JInvTextArea;
import ru.inversion.fx.form.controls.JInvTextField;
import ru.inversion.fx.form.lov.JInvDirectoryChooserLov;
import ru.inversion.fx.form.valid.Validator;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.lovEntity.PIkpEventEnebledTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventTypeTextValue;
import ru.inversion.plshed.model.ScriptRunner;
import ru.inversion.plshed.utils.ButtonUtils;

import java.io.File;
import java.nio.file.Files;

import static lovUtils.LovUtils.initCombobox;
import static ru.inversion.plshed.utils.ButtonUtils.setInnerGraphicButton;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */

public class EditIkpTaskEventsController extends JInvFXFormController <PIkpTaskEvents> {
//    @FXML JInvLongField IEVENTID;
//    @FXML JInvLongField IEVENTTASKID;
//    @FXML JInvLongField IEVENTNPP;
//    @FXML JInvTextField CEVENTNAME;
//    @FXML JInvLongField IEVENTPRESETID;

    @FXML
    private TextArea TESTAREA;
    @FXML
    private TextField EVENTRESULT;
    @FXML
    private VBox TESTPANE;
    @FXML
    private JInvButton TESTBUTTON;
    @FXML
    private SplitPane SCRIPTSPLIT;
    @FXML
    private HBox COMMANDBAR;
    @FXML
    private AnchorPane COMMANDBARANCHOR;


    @FXML
    JInvTextArea LEVENTTEXT;
    @FXML
    JInvComboBox<Long, String> IEVENTTYPE;
    @FXML
    JInvTextField CEVENTINDIR;
    @FXML
    JInvTextField CEVENTOUTDIR;
    @FXML
    JInvTextField CEVENTARHDIR;
    @FXML
    JInvComboBox<Long, String> BEVENTENABLED;
    @FXML
    JInvComboBox<Long, String> IEVENTFILEDIR;

    private Boolean isDebugOpen = false;
    private final Long ENABLED_SCRIPT = 2L;


    @Override
    protected void init() throws Exception {
        super.init();
        initComboBox();
        initInnerButton();
        initCustomButtons();
    }

    private void initCustomButtons() {
        JInvButton helpButton = ButtonUtils.addCustomButton(
                FontAwesome.fa_question,
                "",
                "Help",
                (a) -> helpWindow()
        );
        AnchorPane.setRightAnchor(helpButton, 5.0);
        AnchorPane.setTopAnchor(helpButton, 5.0);
        COMMANDBARANCHOR.getChildren().add(helpButton);
    }

    private void helpWindow() {
        new FXFormLauncher<>(getTaskContext(), getViewContext(), HelpWindowController.class)
                .modal(true)
                .show();
    }

    private void initInnerButton() {
        setInnerGraphicButton(CEVENTINDIR, FontAwesome.fa_search, event -> chooser(CEVENTINDIR), event -> validate(CEVENTINDIR));
        setInnerGraphicButton(CEVENTOUTDIR, FontAwesome.fa_search, event -> chooser(CEVENTOUTDIR), event -> validate(CEVENTOUTDIR));
        setInnerGraphicButton(CEVENTARHDIR, FontAwesome.fa_search, event -> chooser(CEVENTARHDIR), event -> validate(CEVENTARHDIR));
    }

    private void initComboBox() throws ru.inversion.dataset.DataSetException {
        initCombobox(getTaskContext(), IEVENTTYPE, PIkpEventTypeTextValue.class).setOnAction(event ->
                TESTBUTTON.setDisable(!(((JInvComboBox) event.getSource()).getValue() == ENABLED_SCRIPT))
        );
        initCombobox(getTaskContext(), IEVENTFILEDIR, PIkpEventFileTypeTextValue.class);
        initCombobox(getTaskContext(), BEVENTENABLED, PIkpEventEnebledTextValue.class);
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

    private void validate(JInvTextField field) {
        getValidMan().bindControls2Validator(o -> {
            if (o != null && !Files.exists(new File((String) o).toPath()))
                return new Validator.Result(getBundleString("DIRCHOOCE.ERROR1"), getBundleString("DIRCHOOCE.ERROR2"));
            return null;
        }, field);
    }

    public void testCode(ActionEvent actionEvent) {
        TESTAREA.clear();
        Object result = null;
        String compileText = "";
        if (!LEVENTTEXT.getText().isEmpty()) {
            ScriptRunner scriptRunner = new ScriptRunner(
                    logger,
                    LEVENTTEXT.getText(),
                    getDataObject(),
                    !EVENTRESULT.getText().isEmpty() ? EVENTRESULT.getText() : null,
                    getTaskContext().getConnection()
            );

            result = "Result:\n" + scriptRunner.startScript() + "\n";
            compileText = "Compile:\n" + scriptRunner.checkCodeResult + "\n";
            TESTAREA.setText(compileText.concat(String.valueOf(result)));
        }

    }

    public void onTabSelect(Event event) {
        isDebugOpen = isDebugOpen ? !isDebugOpen : isDebugOpen;
    }

    @FXML
    private void openCloseDebugWin() {
        SCRIPTSPLIT.setDividerPositions(isDebugOpen ? 0.9225d : 0.5d);
        isDebugOpen = !isDebugOpen;
    }

}

