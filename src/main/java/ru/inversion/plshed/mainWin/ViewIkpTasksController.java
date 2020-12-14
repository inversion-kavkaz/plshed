package ru.inversion.plshed.mainWin;

import javafx.fxml.FXML;
import ru.inversion.dataset.DataLinkBuilder;
import ru.inversion.dataset.IDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.aggr.AggrFuncEnum;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.*;
import ru.inversion.fx.form.controls.JInvTable;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.fx.form.controls.dsbar.DSInfoBar;
import ru.inversion.fx.form.controls.table.toolbar.AggregatorType;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;
import ru.inversion.utils.ConnectionStringFormatEnum;

import static manifest.ManifestData.loadDataFromManifestFile;
import static ru.inversion.plshed.utils.LovUtils.convertTableValue;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class ViewIkpTasksController extends JInvFXBrowserController {
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


    private final XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS = new XXIDataSet<>();
    private final XXIDataSet<PIkpTasks> dsIKP_TASKS = new XXIDataSet<>();
    private final XXIDataSet<PIkpRunningTextValue> dsIKP_RUNNING_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpRunningSideTextValue> dsIKP_RUNNING_SIDE_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpPeriodTextValue> dsIKP_PERIOD_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpFrequencyTextValue> dsIKP_FREQUENCY_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpEventTypeTextValue> dsIKP_EVENTTYPE_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpEventFileTypeTextValue> dsIKP_EVENTFILETYPE_TEXT_VALUE = new XXIDataSet<>();
    private final XXIDataSet<PIkpEventEnebledTextValue> dsIKP_EVENTENEBLED_TEXT_VALUE = new XXIDataSet<>();


    private void initDataSet() throws Exception {
        dsIKP_TASKS.setTaskContext(getTaskContext());
        dsIKP_TASKS.setRowClass(PIkpTasks.class);

        dsIKP_TASK_EVENTS.setTaskContext(getTaskContext());
        dsIKP_TASK_EVENTS.setRowClass(PIkpTaskEvents.class);

        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_TASK_EVENTS, PIkpTasks::getITASKID, "IEVENTTASKID");
    }

    @Override
    protected void init() throws Exception {
        initTitle();
        initDataSet();
        initDataSetAdapter(dsIKP_TASKS, IKP_TASKS, IKP_TASKS$MARK);
        initDataSetAdapter(dsIKP_TASK_EVENTS, IKP_TASK_EVENTS, IKP_TASK_EVENTS$MARK);
        initTableAndFilterConverters();
        initToolBar();
        initToolBarAction();
        doRefreshAllTables();
    }

    private void initTitle() {
        String filePath = ViewIkpTasksController.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String version = loadDataFromManifestFile(ViewIkpTasksController.class).get("version");
        String date = loadDataFromManifestFile(ViewIkpTasksController.class).get("date");

        setTitle(getBundleString("VIEW.TITLE")
                .concat(" (")
                .concat(getBundleString("VERSION"))
                .concat(version)
                .concat(") ")
                .concat(getTaskContext().getConnectionString(ConnectionStringFormatEnum.SQL_SIMPLE)));
    }

    private void doRefreshAllTables() {
        doRefresh();
        doRefreshEvents();
    }

    private <T> DSFXAdapter<T> initDataSetAdapter(XXIDataSet<T> dataSet, JInvTable<T> table, DSInfoBar dsInfoBar) throws Exception {
        DSFXAdapter<T> dsfx = DSFXAdapter.bind(dataSet, table, null, true);
        dsfx.setEnableFilter(true);
        dsInfoBar.init(table.getDataSetAdapter());
        dsInfoBar.addAggregator("1", AggrFuncEnum.COUNT, AggregatorType.MARK, null, null);
        return dsfx;
    }

    private void initToolBarAction() {
        IKP_TASKS.setToolBar(toolBar);
        IKP_TASKS.setAction(ActionFactory.ActionTypeEnum.CREATE, (a) -> doOperation(FormModeEnum.VM_INS));
        IKP_TASKS.setAction(ActionFactory.ActionTypeEnum.VIEW, (a) -> doOperation(FormModeEnum.VM_SHOW));
        IKP_TASKS.setAction(ActionFactory.ActionTypeEnum.UPDATE, (a) -> doOperation(FormModeEnum.VM_EDIT));
        IKP_TASKS.setAction(ActionFactory.ActionTypeEnum.DELETE, (a) -> doOperation(FormModeEnum.VM_DEL));
        IKP_TASKS.setAction(ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh());

        IKP_TASK_EVENTS.setToolBar(toolBarEvents);
        IKP_TASK_EVENTS.setAction(ActionFactory.ActionTypeEnum.CREATE, (a) -> doOperationEvents(FormModeEnum.VM_INS));
        IKP_TASK_EVENTS.setAction(ActionFactory.ActionTypeEnum.VIEW, (a) -> doOperationEvents(FormModeEnum.VM_SHOW));
        IKP_TASK_EVENTS.setAction(ActionFactory.ActionTypeEnum.UPDATE, (a) -> doOperationEvents(FormModeEnum.VM_EDIT));
        IKP_TASK_EVENTS.setAction(ActionFactory.ActionTypeEnum.DELETE, (a) -> doOperationEvents(FormModeEnum.VM_DEL));
        IKP_TASK_EVENTS.setAction(ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefreshEvents());
    }

    private void initTableAndFilterConverters() throws ru.inversion.dataset.DataSetException {
        /** Таблица заданий */
        convertTableValue(BTASKRUNNING, dsIKP_RUNNING_TEXT_VALUE, PIkpRunningTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKPERIOD, dsIKP_PERIOD_TEXT_VALUE, PIkpPeriodTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKSIDE, dsIKP_RUNNING_SIDE_TEXT_VALUE, PIkpRunningSideTextValue.class, getTaskContext(), true);
        convertTableValue(ITASKFREQUENCY, dsIKP_FREQUENCY_TEXT_VALUE, PIkpFrequencyTextValue.class, getTaskContext(), true);
        /** Таблица событий */
        convertTableValue(IEVENTTYPE, dsIKP_EVENTTYPE_TEXT_VALUE, PIkpEventTypeTextValue.class, getTaskContext(), true);
        convertTableValue(IEVENTFILEDIR, dsIKP_EVENTFILETYPE_TEXT_VALUE, PIkpEventFileTypeTextValue.class, getTaskContext(), true);
        convertTableValue(BEVENTENABLED, dsIKP_EVENTENEBLED_TEXT_VALUE, PIkpEventEnebledTextValue.class, getTaskContext(), true);
    }

    private void doRefreshEvents() {
        IKP_TASK_EVENTS.executeQuery();
    }

    private void doRefresh() {
        IKP_TASKS.executeQuery();
    }

    private void initToolBar() {
        toolBar.setStandartActions(ActionFactory.ActionTypeEnum.CREATE,
                ActionFactory.ActionTypeEnum.VIEW,
                ActionFactory.ActionTypeEnum.UPDATE,
                ActionFactory.ActionTypeEnum.DELETE,
                ActionFactory.ActionTypeEnum.REFRESH);

        toolBarEvents.setStandartActions(ActionFactory.ActionTypeEnum.CREATE,
                ActionFactory.ActionTypeEnum.VIEW,
                ActionFactory.ActionTypeEnum.UPDATE,
                ActionFactory.ActionTypeEnum.DELETE,
                ActionFactory.ActionTypeEnum.REFRESH);


//        toolBar.getItems ().add (ActionFactory.createButton(ActionFactory.ActionTypeEnum.SETTINGS, (a) -> JInvMainFrame.showSettingsPane ()));
    }

    private void doOperation(JInvFXFormController.FormModeEnum mode) {
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
            new FXFormLauncher<PIkpTasks>(getTaskContext(), getViewContext(), EditIkpTasksController.class)
                    .dataObject(p)
                    .dialogMode(mode)
                    .initProperties(getInitProperties())
                    .callback(this::doFormResult)
                    .modal(true)
                    .show();
    }

    private void doFormResult(JInvFXFormController.FormReturnEnum ok, JInvFXFormController<PIkpTasks> dctl) {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok) {
            EditIkpTasksController controller = (EditIkpTasksController) dctl;
            switch (dctl.getFormMode()) {
                case VM_INS:
                    dsIKP_TASKS.insertRow(dctl.getDataObject(), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    doRefresh();
                    break;
                case VM_EDIT:
                    dsIKP_TASKS.updateCurrentRow(dctl.getDataObject());
                    break;
                case VM_DEL:
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
                    logger.info("gurrent task id = ".concat(dsIKP_TASKS.getCurrentRow().getITASKID().toString()));
                } else {
                    Alerts.error(this, "Ошибка", "Не выбрано задание");
                    return;
                }

                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = dsIKP_TASK_EVENTS.getCurrentRow();
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
                    break;
                case VM_EDIT:
                    dsIKP_TASK_EVENTS.updateCurrentRow(dctl.getDataObject());
                    break;
                case VM_DEL:
                    dsIKP_TASK_EVENTS.removeCurrentRow();
                    break;
                default:
                    break;
            }
        }

        IKP_TASK_EVENTS.requestFocus();
    }

}

