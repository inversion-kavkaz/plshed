package ru.inversion.plshed.userInterfaces.mainui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import ru.inversion.bicomp.action.StopExecuteActionBiCompException;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.*;
import ru.inversion.fx.form.lov.JInvDirectoryChooserLov;
import ru.inversion.fx.form.valid.Validator;
import ru.inversion.fxn3d.action.ActionCheckSQL;
import ru.inversion.fxn3d.action.PSQL;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.lovEntity.PIkpEventEnebledTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventTypeTextValue;
import ru.inversion.plshed.model.ScriptRunner;
import ru.inversion.plshed.utils.ButtonUtils;
import ru.inversion.plshed.utils.JavaKeywords;

import java.io.File;
import java.nio.file.Files;

import static lovUtils.LovUtils.initCombobox;
import static ru.inversion.plshed.utils.ButtonUtils.setInnerGraphicButton;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */

public class EditIkpTaskEventsController extends JInvFXFormController<PIkpTaskEvents> {
    //    @FXML JInvLongField IEVENTID;
//    @FXML JInvLongField IEVENTTASKID;
//    @FXML JInvLongField IEVENTNPP;
//    @FXML JInvTextField CEVENTNAME;
    @FXML
    GridPane MAINGRID;
    @FXML
    JInvLabel lblIEVENTPRESETID;
    @FXML
    JInvLongField IEVENTPRESETID;
    @FXML
    JInvLabel lblCEVENTINDIR;
    @FXML
    JInvLabel lblCEVENTOUTDIR;
    @FXML
    JInvLabel lblCEVENTARHDIR;
    @FXML
    JInvLabel lblLEVENTTEXT;


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
    @FXML
    AnchorPane TEXTANCHORPANE;

    private Boolean isDebugOpen = false;
    private final Long PRESET = 0L;
    private final Long JAVA_SCRIPT = 2L;
    private final Long PLSQL_SCRIPT = 1L;
    private final double OPEN_DEBUG = 0.5d;
    private final double CLOSE_DEBUG = 0.9225d;
    private final Long FILE_UPLOAD = 1L;
    private final Long FILE_DOWNLOAD = 0L;
    private final Long FILE_NONE = -1L;
    private final SimpleBooleanProperty isPrest = new SimpleBooleanProperty();




    @Override
    protected void init() throws Exception {
        super.init();
        initComboBox();
        initInnerButton();
        initCustomButtons();
        initRichText();
        initDeviderPosition();
        initBinding();
    }

    private void initBinding() {
        lblCEVENTINDIR.visibleProperty().bind(CEVENTINDIR.visibleProperty());
        lblCEVENTOUTDIR.visibleProperty().bind(CEVENTOUTDIR.visibleProperty());
        lblCEVENTARHDIR.visibleProperty().bind(CEVENTARHDIR.visibleProperty());
        lblIEVENTPRESETID.visibleProperty().bind(IEVENTPRESETID.visibleProperty());

        IEVENTFILEDIR.disableProperty().bind(isPrest);
        IEVENTPRESETID.visibleProperty().bind(isPrest);
        COMMANDBAR.visibleProperty().bind(isPrest.not());
        SCRIPTSPLIT.visibleProperty().bind(isPrest.not());

    }

    private void initDeviderPosition() {
        SCRIPTSPLIT.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> {
            SCRIPTSPLIT.getDividers().get(0).setPosition(isDebugOpen ? OPEN_DEBUG : CLOSE_DEBUG);
            //System.out.println(String.format("observable: %s  oldValue: %s  newValue: %s",observable.getValue(),oldValue,newValue));
        });
    }

    private void initRichText() {
        StackPane pane = JavaKeywords.getCodeArea(getDataObject().getLEVENTTEXT() != null ? getDataObject().getLEVENTTEXT() : "");
        AnchorPane.setTopAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);
        AnchorPane.setBottomAnchor(pane, 0d);
        AnchorPane.setLeftAnchor(pane, 0d);
        TEXTANCHORPANE.getChildren().add(pane);
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
        initCombobox(getTaskContext(), IEVENTTYPE, PIkpEventTypeTextValue.class).setOnAction(event -> {
            isPrest.setValue(((JInvComboBox) event.getSource()).getValue() == PRESET);
            if(isPrest.get()) IEVENTFILEDIR.setValue(-1L);
            COMMANDBARANCHOR.setStyle(isPrest.get() ? "-fx-border-color: GREEN" : "-fx-border-color: NONE");
            lblLEVENTTEXT.setText(isPrest.get() ? getBundleString("LEVENTTEXT2"): getBundleString("LEVENTTEXT"));
            }
        );
        initCombobox(getTaskContext(), IEVENTFILEDIR, PIkpEventFileTypeTextValue.class).setOnAction(event -> {
            CEVENTOUTDIR.setVisible((((JInvComboBox) event.getSource()).getValue() == FILE_UPLOAD));
            CEVENTINDIR.setVisible((((JInvComboBox) event.getSource()).getValue() == FILE_DOWNLOAD));
            CEVENTARHDIR.setVisible((((JInvComboBox) event.getSource()).getValue() == FILE_DOWNLOAD));
        });
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
        if (IEVENTTYPE.getValue() == JAVA_SCRIPT)
            javaCodeTest();
        if (IEVENTTYPE.getValue() == PLSQL_SCRIPT)
            plsqlCodeTest();
    }

    private void plsqlCodeTest() {
        ActionCheckSQL checkSQL = new ActionCheckSQL(this::preSQL);
        checkSQL.setActionContext(getViewContext(), getTaskContext());
        checkSQL.handle();
    }

    private void javaCodeTest() {
        Object result = null;
        String compileText = "";
        String codeText = JavaKeywords.getCodeText();
        if (!codeText.isEmpty()) {
            ScriptRunner scriptRunner = new ScriptRunner(
                    logger,
                    codeText,
                    getDataObject(),
                    !codeText.isEmpty() ? codeText : null,
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

    @Override
    protected boolean onOK() {
        if (JavaKeywords.isCodeChange())
            LEVENTTEXT.setText(JavaKeywords.getCodeText());
        return super.onOK();
    }

    @FXML
    private void openCloseDebugWin() {
        SCRIPTSPLIT.setDividerPositions(isDebugOpen ? CLOSE_DEBUG : OPEN_DEBUG);
        isDebugOpen = !isDebugOpen;
    }

    private void preSQL(PSQL p) {
        String codeText = JavaKeywords.getCodeText();
        if (codeText.isEmpty()) throw new StopExecuteActionBiCompException();
        p.setName("PL/SQL CODE");
        p.setStatement(codeText);
        p.setType(PSQL.TypeEnum.PLSQL);
    }

}

