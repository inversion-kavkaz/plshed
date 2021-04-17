package ru.inversion.plshed.userInterfaces.mainui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import ru.inversion.bicomp.action.StopExecuteActionBiCompException;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.app.AppException;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.*;
import ru.inversion.fx.form.lov.JInvDirectoryChooserLov;
import ru.inversion.fx.form.lov.JInvEntityLov;
import ru.inversion.fx.form.valid.Validator;
import ru.inversion.fxn3d.action.ActionCheckSQL;
import ru.inversion.fxn3d.action.PSQL;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpEventParams;
import ru.inversion.plshed.entity.PIkpPresetParams;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.lovEntity.PIkpEventEnebledTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpEventTypeTextValue;
import ru.inversion.plshed.model.ScriptRunner;
import ru.inversion.plshed.userInterfaces.presetsview.ViewPresetsController;
import ru.inversion.plshed.utils.ButtonUtils;
import ru.inversion.plshed.utils.GridPaneRowAnim;
import ru.inversion.plshed.utils.JavaKeywords;
import ru.inversion.plshed.utils.SqlUtils;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

import static lovUtils.LovUtils.initCombobox;
import static ru.inversion.plshed.utils.ButtonUtils.setInnerGraphicButton;
import static ru.inversion.plshed.utils.ConstantParams.classString;
import static ru.inversion.plshed.utils.dataSetUtils.getClassFromString;


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
    @FXML GridPane MAINGRID;
    @FXML JInvLabel lblIEVENTPRESETID;
    @FXML JInvLongField IEVENTPRESETID;
    @FXML JInvLabel lblCEVENTINDIR;
    @FXML JInvLabel lblCEVENTOUTDIR;
    @FXML JInvLabel lblCEVENTARHDIR;
    @FXML JInvLabel lblLEVENTTEXT;
    @FXML JInvLabel lblPRESETNAME;
    @FXML private TextArea TESTAREA;
    @FXML private TextField EVENTRESULT;
    @FXML private JInvTextField PRESETNAME;
    @FXML private VBox TESTPANE;
    @FXML private JInvButton TESTBUTTON;
    @FXML private JInvButton PRESETBUTTON;
    @FXML private SplitPane SCRIPTSPLIT;
    @FXML private HBox COMMANDBAR;
    @FXML private AnchorPane COMMANDBARANCHOR;
    @FXML JInvTextArea LEVENTTEXT;
    @FXML JInvComboBox<Long, String> IEVENTTYPE;
    @FXML JInvTextField CEVENTINDIR;
    @FXML JInvTextField CEVENTOUTDIR;
    @FXML JInvTextField CEVENTARHDIR;
    @FXML JInvComboBox<Long, String> BEVENTENABLED;
    @FXML JInvComboBox<Long, String> IEVENTFILEDIR;
    @FXML AnchorPane TEXTANCHORPANE;
    @FXML JInvTable IKP_EVENT_PARAMS;
    @FXML JInvTableColumn<PIkpEventParams, String> CPARAMVALUE;

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
    private GridPaneRowAnim presetGridRow;
    private GridPaneRowAnim outDirGridRow;
    private GridPaneRowAnim inDirGridRow;
    private GridPaneRowAnim archDirGridRow;

    private final XXIDataSet<PIkpEventParams> dsIKP_EVENT_PARAMS = new XXIDataSet<>(getTaskContext(), PIkpEventParams.class);
    private final XXIDataSet<PIkpPresetParams> dsIKP_PRESET_PARAMS = new XXIDataSet<>(getTaskContext(), PIkpPresetParams.class);

    private Map<String,String> paramsList = new HashMap<>();

    private final Long EVENT_TYPE_PRESET = 0L;

    @Override
    protected void init() throws Exception {
        super.init();

        initParamsData();
        initComboBox();
        initInnerButton();
        initCustomButtons();
        initRichText();
        initDeviderPosition();
        initBinding();

        dsIKP_PRESET_PARAMS.setWherePredicat(String.format("ID_PRESET = %s",getDataObject().getIEVENTPRESETID()));
        dsIKP_PRESET_PARAMS.executeQuery();


        if(getDataObject().getIEVENTTYPE() == EVENT_TYPE_PRESET)
            doRefresh();

        initCellFactory();
        initEventParamsTable();
    }

    @Override
    protected void afterInit() throws AppException {
        super.afterInit();
        dsIKP_EVENT_PARAMS.setParameter("PRESET_ID",IEVENTPRESETID.getValue());
    }

    private void initCellFactory() {
        CPARAMVALUE.setEditable(true);
        CPARAMVALUE.setCellRenderer((cell, val) -> {

            if(cell.getPojo() == null) return;
            cell.setText("");
            String cParamName = cell.getPojo().getCPARAMNAME();

            JInvTextField textField = new JInvTextField();

            PIkpPresetParams pIkpPresetParams = dsIKP_PRESET_PARAMS.getRows()
                    .stream()
                    .filter(p -> !StringUtils.isEmpty(cParamName)  && p.getCPARAMNAME().equalsIgnoreCase(cParamName))
                    .findFirst()
                    .get();

            if(pIkpPresetParams != null && pIkpPresetParams.getIS_SPR() != 0){
                String sqlLovRequest = pIkpPresetParams.getCSPRSQL();
                for(Map.Entry es : paramsList.entrySet()){
                    sqlLovRequest = sqlLovRequest.replaceAll(String.format("#%s#",es.getKey()), String.valueOf(es.getValue()));
                }

                if(!sqlLovRequest.contains("#")){
                    setLov("VAL",textField, false,sqlLovRequest)
                    .valueProperty().addListener((observable, oldValue, newValue) -> {
                        paramsList.put(pIkpPresetParams.getCPARAMNAME(), (String) newValue);
                        IKP_EVENT_PARAMS.refresh();
                    });
                } else
                    textField.setEditable(false);
            }
            if(!StringUtils.isEmpty(paramsList.get(cParamName)))
                val = paramsList.get(cParamName);
            textField.setText(val);
            cell.setGraphic(textField);
        });
    }

    @SneakyThrows
    private JInvEntityLov setLov(String valueColumnName,JInvTextField field, Boolean required, String lovSql)  {

        final JInvEntityLov[] lov_field = new JInvEntityLov[1];
        String className = "PDual";
        lovSql = lovSql.replaceAll("\n"," ");
        lovSql = classString.replaceAll(":QUERY",lovSql).replaceAll(":CLASSNAME",className);

        Optional.ofNullable(getClassFromString(lovSql,className,logger)).ifPresent(cls -> {
            lov_field[0] = new JInvEntityLov<>(cls,valueColumnName);
            lov_field[0].setSkipFilterString(true);
            lov_field[0].setTaskContext(getTaskContext());
            field.setLOV(lov_field[0]);
            field.setRequired(required);
            field.setValidateFromLOV(true);
        });
        return lov_field[0];
    }

    private void initEventParamsTable() {
        IEVENTPRESETID.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null || oldValue == newValue) return;
                dsIKP_PRESET_PARAMS.setWherePredicat(String.format("ID_PRESET = %s",newValue));
            try {
                dsIKP_EVENT_PARAMS.getRows().clear();
                dsIKP_PRESET_PARAMS.executeQuery();
                dsIKP_PRESET_PARAMS.getRows().forEach(row -> {
                    PIkpEventParams newPIkpEventParams = new PIkpEventParams();
                    newPIkpEventParams.setIEVENTID(BigDecimal.valueOf(getDataObject().getIEVENTID()));
                    newPIkpEventParams.setCPARAMFULLNAME(row.getCPARAMFULLNAME());
                    newPIkpEventParams.setCPARAMNAME(row.getCPARAMNAME());
                    if(!dsIKP_EVENT_PARAMS.getRows().stream().filter(p -> p.getCPARAMNAME().equalsIgnoreCase(row.getCPARAMNAME())).findAny().isPresent())
                        dsIKP_EVENT_PARAMS.getRows().add(newPIkpEventParams);
                });
            } catch (DataSetException e) {
                e.printStackTrace();
            }
            IKP_EVENT_PARAMS.refresh();
        });
    }

    private void initParamsData() throws Exception {
        DSFXAdapter dsfx = DSFXAdapter.bind(dsIKP_EVENT_PARAMS, IKP_EVENT_PARAMS, null, false);
        dsIKP_EVENT_PARAMS.setWherePredicat(String.format(" IEVENTID = %s",getDataObject().getIEVENTID()));
        doRefresh();
    }

    private void doRefresh(){
        dsIKP_EVENT_PARAMS.setParameter("PRESET_ID",getDataObject().getIEVENTPRESETID());
        IKP_EVENT_PARAMS.executeQuery();
    }

    private void initBinding() {
        presetGridRow = new GridPaneRowAnim(MAINGRID,MAINGRID.getScene().getWindow(),5);
        inDirGridRow = new GridPaneRowAnim(MAINGRID,MAINGRID.getScene().getWindow(),7);
        outDirGridRow = new GridPaneRowAnim(MAINGRID,MAINGRID.getScene().getWindow(),8);
        archDirGridRow = new GridPaneRowAnim(MAINGRID,MAINGRID.getScene().getWindow(),9);

        lblPRESETNAME.visibleProperty().bind(isPrest);
        IEVENTFILEDIR.disableProperty().bind(isPrest);
        COMMANDBAR.visibleProperty().bind(isPrest.not());
        SCRIPTSPLIT.visibleProperty().bind(isPrest.not());
        IKP_EVENT_PARAMS.visibleProperty().bind(isPrest);
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
        helpButton.visibleProperty().bind(isPrest.not());
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
            presetGridRow.setcollapse(!isPrest.get());
            lblLEVENTTEXT.setText(isPrest.get() ? getBundleString("LEVENTTEXT2"): getBundleString("LEVENTTEXT"));
            }
        );
        initCombobox(getTaskContext(), IEVENTFILEDIR, PIkpEventFileTypeTextValue.class).setOnAction(event -> {
            Long fileType = (Long)((JInvComboBox) event.getSource()).getValue();
            inDirGridRow.setcollapse(fileType == FILE_UPLOAD || fileType == FILE_NONE);
            archDirGridRow.setcollapse(fileType == FILE_UPLOAD || fileType == FILE_NONE);
            outDirGridRow.setcollapse(fileType == FILE_DOWNLOAD || fileType == FILE_NONE);
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
                    getTaskContext().getConnection(),
                    viewContext, taskContext);

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
        SqlUtils.clearPresetParams(getTaskContext(),getDataObject().getIEVENTID());
        for(PIkpEventParams item : dsIKP_EVENT_PARAMS.getRows()){
            item.setCPARAMVALUE(paramsList.get(item.getCPARAMNAME()));
            SqlUtils.savePresetParams(getTaskContext(),getDataObject().getIEVENTID(),item.getCPARAMNAME(),item.getCPARAMVALUE());
        }

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

    public void checkPreset(ActionEvent actionEvent) {
        new FXFormLauncher<>(getTaskContext(), getViewContext(), ViewPresetsController.class)
                .dialogMode(FormModeEnum.VM_CHOICE)
                .callback(this::doFormResult)
                .modal(true)
                .show();
    }

    private void doFormResult(FormReturnEnum formReturnEnum, JInvFXFormController<Object> presetObject) {
        if (JInvFXFormController.FormReturnEnum.RET_OK == formReturnEnum){
            Optional
                .of(((ViewPresetsController) presetObject).dsIKP_EVENT_PRESETS.getCurrentRow())
                .ifPresent(pIkpEventPresets -> {
                    PRESETNAME.setText(pIkpEventPresets.getCPRESETNAME());
                    IEVENTPRESETID.setValue(pIkpEventPresets.getIPRESETID());
            });
            paramsList.clear();
        }
    }
}

