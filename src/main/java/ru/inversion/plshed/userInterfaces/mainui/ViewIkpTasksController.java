package ru.inversion.plshed.userInterfaces.mainui;

import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import ru.inversion.bicomp.action.JInvButtonPrint;
import ru.inversion.dataset.*;
import ru.inversion.dataset.aggr.AggrFuncEnum;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.dataset.fx.F7FilterGroup;
import ru.inversion.dataset.fx.F7FilterItem;
import ru.inversion.fx.form.*;
import ru.inversion.fx.form.controls.*;
import ru.inversion.fx.form.controls.dsbar.DSInfoBar;
import ru.inversion.fx.form.controls.renderer.Colorizer;
import ru.inversion.fx.form.controls.table.toolbar.AggregatorType;
import ru.inversion.fx.form.lov.JInvEntityLov;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpLog;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.plshed.interfaces.callFunc;
import ru.inversion.plshed.model.TasksContainer;
import ru.inversion.plshed.userInterfaces.presetsview.ViewPresetsController;
import ru.inversion.plshed.utils.ButtonUtils;
import ru.inversion.plshed.utils.dataSetUtils;
import ru.inversion.utils.ConnectionStringFormatEnum;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static lovUtils.LovUtils.convertTableValue;
import static lovUtils.LovUtils.initCombobox;
import static manifest.ManifestData.*;
import static ru.inversion.plshed.utils.SqlUtils.SetEventNPP;
import static ru.inversion.plshed.utils.TrayUtils.initTray;
import static ru.inversion.plshed.utils.dataSetUtils.dataSetToStream;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */
@Getter
@Setter
public class ViewIkpTasksController extends JInvFXBrowserController implements callFunc, TaskCallBack {

    @FXML
    private JInvTable<PIkpTasks> IKP_TASKS;
    @FXML
    private JInvToolBar toolBar;
    @FXML
    private DSInfoBar IKP_TASKS$MARK;
    @FXML
    private JInvTable<PIkpTaskEvents> IKP_TASK_EVENTS;
    @FXML
    private JInvToolBar toolBarEvents;
    @FXML
    private DSInfoBar IKP_TASK_EVENTS$MARK;
    @FXML
    private JInvTable<PIkpLog> IKP_LOG;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> BTASKRUNNING;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> ITASKSIDE;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> ITASKPERIOD;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> ITASKFREQUENCY;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> IEVENTTYPE;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> IEVENTFILEDIR;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> BEVENTENABLED;
    @FXML
    private JInvTableColumn<PIkpTasks, Long> RUNNINGEVENT;
    @FXML
    private JInvComboBox<Long, String> LOGLEVEL;
    @FXML
    private JInvCalendarTime STARTDATETIME;
    @FXML
    private HBox SHEDINFOBOX;
    @FXML
    private JInvCalendar DATEFILTER;
    @FXML
    private JInvTextField SESSIONID;
    @FXML
    private HBox FILTERPANE;

    private int dateFilter;
    private int sessionFilter;
    private SimpleStringProperty sessionString = new SimpleStringProperty("");
    private long sessionFilterTimer = 0;
    private final int SESSION_FILTER_WITE = 1000;
    private Boolean isEventMoved = false;
    private final int reportTypeId = 1310001;


    private final XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS = new XXIDataSet<>(getTaskContext(), PIkpTaskEvents.class);
    private final XXIDataSet<PIkpTasks> dsIKP_TASKS = new XXIDataSet<>(getTaskContext(), PIkpTasks.class);
    private final XXIDataSet<PIkpLog> dsIKP_LOG = new XXIDataSet<>(getTaskContext(), PIkpLog.class);

    private final PublishSubject sessionSubject = PublishSubject.create();



    {
        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_TASK_EVENTS, PIkpTasks::getITASKID, "IEVENTTASKID");
        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_LOG, PIkpTasks::getITASKID, "TASKID");
        dsIKP_TASKS.pageSize(200);
    }

    private TasksContainer tasksContainer;


    @Override
    protected void init() throws Exception {
        //checkRunning();
        tasksContainer = new TasksContainer(logger, getTaskContext(), getViewContext(), this);
        initTitle();
        initTableColorized();
        DSFXAdapter<PIkpTasks> pIkpTasksDSFXAdapter = initDataSetAdapter(dsIKP_TASKS, IKP_TASKS, IKP_TASKS$MARK, true);
        initDataSetAdapter(dsIKP_TASK_EVENTS, IKP_TASK_EVENTS, IKP_TASK_EVENTS$MARK, true);
        initDataSetAdapter(dsIKP_LOG, IKP_LOG, null, true);
        initTray(getViewContext(), this);
        initTableAndFilterConverters();
        initToolBar(toolBar, toolBarEvents);
        initAltPrint();
        initToolBarAction(toolBar, IKP_TASKS, dsIKP_TASKS, this::doOperation);
        initToolBarAction(toolBarEvents, IKP_TASK_EVENTS, dsIKP_TASK_EVENTS, this::doOperationEvents);
        initCustomButtons();
        doRefreshAllTables();
        initTasks();
        initComboBinding(pIkpTasksDSFXAdapter);
        initLogFilterBinding();

        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/
//        AtomicReference<Long> ID = new AtomicReference<>(null);
//
//        pIkpTasksDSFXAdapter.setFilterModifier(new DSFXAdapter.IFilterModifier() {
//            @Override
//            public void modifyBefore(Map<F7FilterGroup, Set<F7FilterItem>> filterMap) {
//                F7FilterItem itaskperiod = filterMap.entrySet().stream().flatMap(p -> p.getValue().stream())
//                        .filter(p -> p.getColumn().equalsIgnoreCase("ITASKPERIOD")).findAny().get();
//
//                JInvEntityLov jInvEntityLov = new JInvEntityLov(PIkpPeriodTextValue.class,"VALUE", param -> {
//                    ID.set(((PIkpPeriodTextValue)param).getID());
//                    return ((PIkpPeriodTextValue)param).getVALUE();
//                });
//                itaskperiod.setType(String.class);
//                //itaskperiod.setLov(jInvEntityLov);
//                JInvTextField jInvTextField = new JInvTextField();
//                jInvTextField.setLOV(jInvEntityLov, true);
//                itaskperiod.setControl(jInvTextField);
//            }
//
//            @Override
//            public List<? extends IFilterItem> modifyAfter(List<F7FilterItem> filterList) {
//                F7FilterItem itaskperiod = filterList.stream().filter(p -> p.getColumn().equalsIgnoreCase("ITASKPERIOD")).findAny().get();
//                itaskperiod.setType(Long.class);
//                itaskperiod.setValue(String.format("# = %s",ID.get()));
//                itaskperiod.setControlValue(String.format("%s",ID.get()));
//                return filterList;
//            }
//        });
        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/
    }

    private void initTableColorized() {
        IKP_TASKS.addColor(cell -> {
            PIkpTasks pojo = cell.getPojo();
            if (pojo != null && pojo.getFTASKRUN() == 1)
                return new Colorizer(null, Color.DARKGREEN, Colorizer.TextStyle.BOLD);
            else
                return null;
        });
    }

    private void initAltPrint() {
        JInvButtonPrint btPRINT_AP = new JInvButtonPrint();
        btPRINT_AP.getAction().setActionPreCallback(apReport -> {
            apReport.setTypeID(Long.valueOf(reportTypeId));
            apReport.setParam(
                    dsIKP_TASKS.getMarkerID() != null ? dsIKP_TASKS.getMarkerID().toString(): "",
                    dsIKP_TASKS.getCurrentRow() != null ? dsIKP_TASKS.getCurrentRow().getITASKID().toString(): ""
            );
        });

        toolBar.getItems().add(btPRINT_AP);
    }

    /**Проверка запуска второго экземпляра приложения пока отказались */
    private void checkRunning() {

//        PLShedMain.processesList.stream().filter(p -> p.getCommand().contains(BaseApp.APP().getAppID())).forEach(c -> {
//            logger.info(String.format("\n----------------------------------------------" +
//                            "\n process command: %s \n process name: %s \n process user: %s \n process ID: %s" +
//                            "\n--------------------------------------------------\n"
//                    , c.getCommand(), c.getName(), c.getUser(), c.getPid()));
//        });
//
//        if (PLShedMain.isRunning) {
//            Alerts.error(this, getBundleString("RUNNINGTEXT"));
//            stopApp();
//        }
    }

    private void initLogFilterBinding() {
        sessionSubject.filter((a) -> a != null).debounce(1, TimeUnit.SECONDS).subscribe((a) -> setCustomFilter());
        SESSIONID.textProperty().bindBidirectional(sessionString);
        sessionString.addListener((observable, oldValue, newValue) -> {
            sessionSubject.onNext(newValue);
        });
    }

    private void initComboBinding(DSFXAdapter<PIkpTasks> pIkpTasksDSFXAdapter) throws DataSetException {
        pIkpTasksDSFXAdapter.bindControl(LOGLEVEL);
        initCombobox(getTaskContext(), LOGLEVEL, PIkpLogLevelTextValue.class).setOnAction(event -> {
            dsIKP_TASKS.getCurrentRow().setLOGLEVEL((Long) ((JInvComboBox) event.getTarget()).getValue());
        });
        pIkpTasksDSFXAdapter.bindControl(STARTDATETIME);

//        LOGLEVEL.setCellFactory(param -> new ListCell(){
//            @Override
//            protected void updateItem(Object item, boolean empty) {
//                super.updateItem(item, empty);
//            }
//        });

    }

    private void initTasks() throws DataSetException {
        if (dsIKP_TASKS.getRows().size() < 1)
            dsIKP_TASKS.execute();
        dsIKP_TASKS.getRows().stream()
                .forEach(p -> {
                    tasksContainer.initTask(p);
                });
    }

    private void startTask(Long taskID) {
        tasksContainer.startTask(taskID);
    }

    private void initCustomButtons() {
        SHEDINFOBOX.getChildren().add(
                ButtonUtils.addCustomButton(FontAwesome.fa_hourglass_start,
                        getBundleString("START"),
                        getBundleString("START.TOOLTIP"),
                        (a) -> startTask(dsIKP_TASKS.getCurrentRow().getITASKID()))
        );

        JInvButton resetFilterButton = ButtonUtils.addCustomButton(
                FontAwesome.fa_times,
                "",
                getBundleString("RESET.FILTER"),
                (a) -> {
                    sessionString.setValue("");
                    DATEFILTER.getEditor().clear();
                    dsIKP_LOG.removeFilter(dateFilter);
                    dsIKP_LOG.removeFilter(sessionFilter);
                    doRefresh(dsIKP_LOG);
                });
        HBox.setMargin(resetFilterButton, new Insets(5, 5, 5, 0));
        FILTERPANE.getChildren().add(resetFilterButton);

    }

    private void initTitle() {
        String version = "";
        String buildDate = "";
        String buildNumber = "";

        try {
            loadDataFromManifestFile(ViewIkpTasksController.class);
        } catch (IOException e) {
            logger.error(getBundleString("ERROR.MANIFEST_FILE"));
        }
        if (isManifestDataLoad()) {
            version = getManifestData("Version");
            buildDate = getManifestData("Build-date");
            buildNumber = getManifestData("Build");
            version = version.substring(0, version.lastIndexOf(".") + 1).concat(buildNumber);
        }

        setTitle(getBundleString("VIEW.TITLE")
                .concat(" (")
                .concat(getBundleString("VERSION"))
                .concat(version)
                .concat(") ")
                .concat(getTaskContext().getConnectionString(ConnectionStringFormatEnum.SQL_SIMPLE)));
    }

    private void doRefreshAllTables() {
        doRefresh(dsIKP_TASKS);
        doRefresh(dsIKP_TASK_EVENTS);
    }

    private <T> DSFXAdapter<T> initDataSetAdapter(XXIDataSet<T> dataSet, JInvTable<T> table, DSInfoBar dsInfoBar, Boolean isFilter) throws Exception {
        DSFXAdapter<T> dsfx = DSFXAdapter.bind(dataSet, table, null, true);
        dsfx.setEnableFilter(isFilter);
        if (dsInfoBar != null) {
            dsInfoBar.init(table.getDataSetAdapter());
            dsInfoBar.addAggregator("1", AggrFuncEnum.COUNT, AggregatorType.MARK, null, null);
        }
        return dsfx;
    }

    private void initToolBarAction(JInvToolBar lToolBar, JInvTable lTable, XXIDataSet dataSet, callFunc handle) {
        lTable.setToolBar(lToolBar);
        lTable.setAction(ActionFactory.ActionTypeEnum.CREATE, (a) -> handle.doOperation(FormModeEnum.VM_INS));
        lTable.setAction(ActionFactory.ActionTypeEnum.VIEW, (a) -> handle.doOperation(FormModeEnum.VM_SHOW));
        lTable.setAction(ActionFactory.ActionTypeEnum.UPDATE, (a) -> handle.doOperation(FormModeEnum.VM_EDIT));
        lTable.setAction(ActionFactory.ActionTypeEnum.DELETE, (a) -> handle.doOperation(FormModeEnum.VM_DEL));
        lTable.setAction(ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh(dataSet));
    }

    private void initTableAndFilterConverters() throws ru.inversion.dataset.DataSetException {

        /** Таблица заданий */
        dataSetUtils.convertTableValue(BTASKRUNNING, PIkpRunningTextValue.class, getTaskContext(), true);
        dataSetUtils.convertTableValue(ITASKPERIOD, PIkpPeriodTextValue.class, getTaskContext(), true);
        dataSetUtils.convertTableValue(ITASKSIDE, PIkpRunningSideTextValue.class, getTaskContext(), true);
        dataSetUtils.convertTableValue(ITASKFREQUENCY, PIkpFrequencyTextValue.class, getTaskContext(), true);
//        dataSetUtils.convertTableValue(RUNNINGEVENT, PIkpRunningEventTextValue.class, getTaskContext(), true);

        /** Таблица событий */
        convertTableValue(IEVENTTYPE, PIkpEventTypeTextValue.class, getTaskContext(), true);
        convertTableValue(IEVENTFILEDIR, PIkpEventFileTypeTextValue.class, getTaskContext(), true);
        convertTableValue(BEVENTENABLED, PIkpEventEnebledTextValue.class, getTaskContext(), true);
    }

    private void doRefresh(XXIDataSet dataSet) {
        try {

            dataSet.execute();
        } catch (DataSetException e) {
            e.printStackTrace();
        }
    }

    private void initToolBar(JInvToolBar... toolBarList) {
        for (int i = 0; i < toolBarList.length; i++) {
            toolBarList[i].setStandartActions(ActionFactory.ActionTypeEnum.CREATE,
                    ActionFactory.ActionTypeEnum.VIEW,
                    ActionFactory.ActionTypeEnum.UPDATE,
                    ActionFactory.ActionTypeEnum.DELETE,
                    ActionFactory.ActionTypeEnum.REFRESH);
        }
//        toolBar.getItems ().add (ActionFactory.createButton(ActionFactory.ActionTypeEnum.SETTINGS, (a) -> JInvMainFrame.showSettingsPane ()));
    }

    public void doOperation(JInvFXFormController.FormModeEnum mode) {
        PIkpTasks p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpTasks();
                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = dsIKP_TASKS.getCurrentRow();
                break;
        }

        if (p != null)
            new FXFormLauncher<>(getTaskContext(), getViewContext(), EditIkpTasksController.class)
                    .dataObject(p)
                    .dialogMode(mode)
                    .initProperties(getInitProperties())
                    .callback(this::doFormResult)
                    .modal(true)
                    .show();
    }

    private void doFormResult(JInvFXFormController.FormReturnEnum ok, JInvFXFormController<PIkpTasks> dctl) {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok) {
            switch (dctl.getFormMode()) {
                case VM_INS:
                    dsIKP_TASKS.insertRow(dctl.getDataObject(), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    doRefresh(dsIKP_TASKS);
                    tasksContainer.initTask(dctl.getDataObject());
                    break;
                case VM_EDIT:
                    dsIKP_TASKS.updateCurrentRow(dctl.getDataObject());
                    tasksContainer.updateTask(dctl.getDataObject());
                    break;
                case VM_DEL:
                    tasksContainer.deleteTask(dsIKP_TASKS.getCurrentRow().getITASKID());
                    dsIKP_TASKS.removeCurrentRow();
                    break;
                default:
                    break;
            }
        }

        IKP_TASKS.requestFocus();
    }

    private void doOperationEvents(JInvFXFormController.FormModeEnum mode) {
        PIkpTaskEvents p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpTaskEvents();

                if (dsIKP_TASKS.getCurrentRow() != null && dsIKP_TASKS.getCurrentRow().getITASKID() != null) {
                    p.setIEVENTTASKID(dsIKP_TASKS.getCurrentRow().getITASKID());
                    p.setIEVENTNPP(getNextPP());
                    logger.info(String.format("current task id = %d", dsIKP_TASKS.getCurrentRow().getITASKID()));
                } else {
                    Alerts.error(this, getBundleString("ERROR.NAME"), getBundleString("ERROR.TEXT"));
                    return;
                }

                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = (PIkpTaskEvents) dsIKP_TASK_EVENTS.getCurrentRow();
                break;
        }

        if (p != null)
            new FXFormLauncher<PIkpTaskEvents>(getTaskContext(), getViewContext(), EditIkpTaskEventsController.class)
                    .dataObject(p)
                    .dialogMode(mode)
                    .initProperties(getInitProperties())
                    .callback(this::doFormResultEvents)
                    .modal(true)
                    .show();
    }

    private void doFormResultEvents(JInvFXFormController.FormReturnEnum ok, JInvFXFormController<PIkpTaskEvents> dctl) {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok) {
            switch (dctl.getFormMode()) {
                case VM_INS:
                    dsIKP_TASK_EVENTS.insertRow(dctl.getDataObject(), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    tasksContainer.initTask(dsIKP_TASKS.getCurrentRow());
                    break;
                case VM_EDIT:
                    dsIKP_TASK_EVENTS.updateCurrentRow(dctl.getDataObject());
                    tasksContainer.updateTask(dsIKP_TASKS.getCurrentRow());
                    break;
                case VM_DEL:
                    dsIKP_TASK_EVENTS.removeCurrentRow();
                    tasksContainer.updateTask(dsIKP_TASKS.getCurrentRow());
                    break;
                default:
                    break;
            }
        }

        IKP_TASK_EVENTS.requestFocus();
    }

    private long getNextPP() {
        return dataSetToStream(dsIKP_TASK_EVENTS)
                .max(Comparator.comparing(PIkpTaskEvents::getIEVENTNPP))
                .map(p -> p.getIEVENTNPP())
                .orElse(0l) + 1;
    }

    private void stopApp() {
        Platform.exit();
    }

    @Override
    public void onTaskStart(Long taskId) {
        markedRunningRow(taskId, 1L);
    }

    private void markedRunningRow(Long taskId, long l) {
        PIkpTasks sellectedRow = dsIKP_TASKS.getCurrentRow();
        dsIKP_TASKS.setCurrentRow(dataSetToStream(dsIKP_TASKS).filter(p -> p.getITASKID() == taskId).findFirst().get());
        PIkpTasks tasks = dsIKP_TASKS.getCurrentRow();
        tasks.setFTASKRUN(l);
        dsIKP_TASKS.updateCurrentRow(tasks);
        dsIKP_TASKS.setCurrentRow(sellectedRow);
    }

    @Override
    public void onTaskFinish(Long taskId,Long code) {
        markedRunningRow(taskId, 0L);
    }

    @Override
    public void onEventStart(Long enentID) {
        if(dataSetToStream(dsIKP_TASK_EVENTS).anyMatch(p -> p.getIEVENTTASKID() == enentID))
            dsIKP_TASK_EVENTS.setCurrentRow(dataSetToStream(dsIKP_TASK_EVENTS).filter(p -> p.getIEVENTID() == enentID).findFirst().get());
    }

    @Override
    public void onEventFinish(Long enentID) {
        Platform.runLater(() -> {
            doRefresh(dsIKP_LOG);
        });
    }

    @FXML
    public void exit() {
        if (!Alerts.yesNo(this, getBundleString("ERROR.EXIT"), getBundleString("ERROR.EXIT_TEXT")))
            return;
        stopApp();
    }

    @FXML
    private void setCustomFilter() {
        System.out.println(String.format("setCustomFilter "));
        if (DATEFILTER.getValue() != null) {
            dsIKP_LOG.removeFilter(dateFilter);
            dateFilter = dsIKP_LOG.setFilter(String.format("trunc(DT) = to_date('%s','yyyy-MM-dd')", DATEFILTER.getValue().toString()), false, true);
        }
        if (SESSIONID.getText() != null) {
            dsIKP_LOG.removeFilter(sessionFilter);
            sessionFilter = dsIKP_LOG.setFilter(String.format("SESSID like '%s%%'", SESSIONID.getText()), false, true);
        }

        doRefresh(dsIKP_LOG);
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.isShiftDown()){
            if(keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN){
                int rowSize = dsIKP_TASK_EVENTS.getRows().size();
                int currentRowNum = dsIKP_TASK_EVENTS.getCurrentRowNum();
                int preRowNum = currentRowNum > 1 ? currentRowNum - 1: 0;
                int postRowNum =  currentRowNum < (rowSize -1) ? currentRowNum + 1: rowSize - 1;

                PIkpTaskEvents currentRow = dsIKP_TASK_EVENTS.getCurrentRow();
                PIkpTaskEvents preCurRow = dsIKP_TASK_EVENTS.getRow(preRowNum);
                PIkpTaskEvents postCurRow = dsIKP_TASK_EVENTS.getRow(postRowNum);
                Long savedEventPP = currentRow.getIEVENTNPP();


                if(keyEvent.getCode() == KeyCode.UP){
                    changeRows(preRowNum, currentRow, preCurRow, savedEventPP);
                }
                if(keyEvent.getCode() == KeyCode.DOWN){
                    changeRows(postRowNum, currentRow, postCurRow, savedEventPP);
                }
                isEventMoved = true;
            }
        }
    }

    private void changeRows(int preRowNum, PIkpTaskEvents currentRow, PIkpTaskEvents distRow, Long savedEventPP) {
        currentRow.setIEVENTNPP(distRow.getIEVENTNPP());
        distRow.setIEVENTNPP(savedEventPP);
        dsIKP_TASK_EVENTS.updateCurrentRow(distRow);
        dsIKP_TASK_EVENTS.setCurrentRowNum(preRowNum);
        dsIKP_TASK_EVENTS.updateCurrentRow(currentRow);
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        System.out.println("key released = " + keyEvent.getCode());
        if(keyEvent.getCode() == KeyCode.SHIFT && isEventMoved){
            isEventMoved = false;

            if(Alerts.yesNo(this, getBundleString("SAVED.CAHNGED"))){
                AtomicReference<Long> eventPPNValue = new AtomicReference<>(256987456321L);
                dsIKP_TASK_EVENTS.getRows().forEach(event -> {
                    SetEventNPP(getTaskContext(),event.getIEVENTID(), eventPPNValue.getAndSet(eventPPNValue.get() + 1));
                });
                dsIKP_TASK_EVENTS.getRows().forEach(event -> {
                    SetEventNPP(getTaskContext(),event.getIEVENTID(), event.getIEVENTNPP());
                });
            }
        doRefresh(dsIKP_TASK_EVENTS);
        }
    }

    public void presetProps(ActionEvent actionEvent) {
            new FXFormLauncher<>(getTaskContext(), getViewContext(), ViewPresetsController.class)
                    .dialogMode(FormModeEnum.VM_EDIT)
                    .modal(true)
                    .show();
        }

}

