package ru.inversion.plshed.userInterfaces.mainui;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import ru.inversion.dataset.DataLinkBuilder;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.IDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.aggr.AggrFuncEnum;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.*;
import ru.inversion.fx.form.controls.*;
import ru.inversion.fx.form.controls.dsbar.DSInfoBar;
import ru.inversion.fx.form.controls.table.toolbar.AggregatorType;
import ru.inversion.icons.enums.FontAwesome;
import ru.inversion.plshed.entity.PIkpLog;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;
import ru.inversion.plshed.interfaces.TaskCallBack;
import ru.inversion.plshed.interfaces.callFunc;
import ru.inversion.plshed.model.TasksContainer;
import ru.inversion.plshed.utils.ButtonUtils;
import ru.inversion.utils.ConnectionStringFormatEnum;

import java.io.IOException;
import java.util.Comparator;

import static lovUtils.LovUtils.convertTableValue;
import static lovUtils.LovUtils.initCombobox;
import static manifest.ManifestData.*;
import static ru.inversion.plshed.utils.TrayUtils.initTray;
import static ru.inversion.plshed.utils.dataSetUtils.dataSetToStream;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


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

    @FXML private JInvTable<PIkpLog> IKP_LOG;

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
    private JInvComboBox<Long, String> LOGLEVEL;
    @FXML
    private JInvCalendarTime STARTDATETIME;
    @FXML
    private HBox SHEDINFOBOX;


    private final XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS = new XXIDataSet<>(getTaskContext(), PIkpTaskEvents.class);
    private final XXIDataSet<PIkpTasks> dsIKP_TASKS = new XXIDataSet<>(getTaskContext(), PIkpTasks.class);
    private final XXIDataSet<PIkpLog> dsIKP_LOG = new XXIDataSet<> (getTaskContext(),PIkpLog.class);


    {
        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_TASK_EVENTS, PIkpTasks::getITASKID, "IEVENTTASKID");
        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_LOG, PIkpTasks::getITASKID, "TASKID");
    }

    private TasksContainer tasksContainer = new TasksContainer(logger,getTaskContext(),this);

    @Override
    protected void init() throws Exception {


        initTitle();
        DSFXAdapter<PIkpTasks> pIkpTasksDSFXAdapter = initDataSetAdapter(dsIKP_TASKS, IKP_TASKS, IKP_TASKS$MARK, true);
        initDataSetAdapter(dsIKP_TASK_EVENTS, IKP_TASK_EVENTS, IKP_TASK_EVENTS$MARK, true);
        initDataSetAdapter(dsIKP_LOG, IKP_LOG, null, true);
        initTray(getViewContext(), this);
        initTableAndFilterConverters();
        initToolBar(toolBar, toolBarEvents);
        initToolBarAction(toolBar, IKP_TASKS, dsIKP_TASKS, this::doOperation);
        initToolBarAction(toolBarEvents, IKP_TASK_EVENTS, dsIKP_TASK_EVENTS, this::doOperationEvents);
        initCustomButtons();
        doRefreshAllTables();
        initTasks();
        initComboBinding(pIkpTasksDSFXAdapter);

        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/
        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/
    }

    private void initComboBinding(DSFXAdapter<PIkpTasks> pIkpTasksDSFXAdapter) throws DataSetException {
        pIkpTasksDSFXAdapter.bindControl(LOGLEVEL);
        initCombobox(getTaskContext(),LOGLEVEL, PIkpLogLevelTextValue.class).setOnAction(event -> {
            dsIKP_TASKS.getCurrentRow().setLOGLEVEL((Long) ((JInvComboBox)event.getTarget()).getValue());
        });
        pIkpTasksDSFXAdapter.bindControl(STARTDATETIME);
    }

    private void initTasks() throws DataSetException {
        if(dsIKP_TASKS.getRows().size() < 1)
            dsIKP_TASKS.execute();
        dsIKP_TASKS.getRows().stream().forEach(p -> {
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
                (a) -> startTask(dsIKP_TASKS.getCurrentRow().getITASKID())));
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
        if(dsInfoBar != null) {
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
        convertTableValue(BTASKRUNNING, PIkpRunningTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKPERIOD, PIkpPeriodTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKSIDE, PIkpRunningSideTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKFREQUENCY, PIkpFrequencyTextValue.class, getTaskContext(), true);
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


    @Override
    public void onTaskFinish(Long code) {
        doRefresh(dsIKP_LOG);
    }

    @Override
    public void onEventFinish(Long enentID) {

    }
}

