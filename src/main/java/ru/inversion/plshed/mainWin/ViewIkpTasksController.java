package ru.inversion.plshed.mainWin;

import javafx.fxml.FXML;
import org.reflections.Reflections;
import ru.inversion.dataset.DataLinkBuilder;
import ru.inversion.dataset.DataSetException;
import ru.inversion.dataset.IDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.aggr.AggrFuncEnum;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.*;
import ru.inversion.fx.form.controls.JInvTable;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.JInvTableColumnDate;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.fx.form.controls.dsbar.DSInfoBar;
import ru.inversion.fx.form.controls.table.toolbar.AggregatorType;
import ru.inversion.plshed.PLShedMain;
import ru.inversion.plshed.entity.PIkpTaskEvents;
import ru.inversion.plshed.entity.PIkpTasks;
import ru.inversion.plshed.entity.lovEntity.*;
import ru.inversion.plshed.interfaces.callFunc;
import ru.inversion.utils.ConnectionStringFormatEnum;
import java.util.Comparator;

import static lovUtils.LovUtils.convertTableValue;
import static manifest.ManifestData.loadDataFromManifestFile;
import static ru.inversion.plshed.utils.dataSetUtils.dataSetToStream;


/**
 * @author Dmitry Hvastunov
 * @created 10 Декабрь 2020 - 15:02
 * @project plshed
 */


public class ViewIkpTasksController extends JInvFXBrowserController implements callFunc {

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

    private XXIDataSet<PIkpTaskEvents> dsIKP_TASK_EVENTS = new XXIDataSet<>(getTaskContext(),PIkpTaskEvents.class);
    private XXIDataSet<PIkpTasks> dsIKP_TASKS = new XXIDataSet<>(getTaskContext(),PIkpTasks.class);

    {
        DataLinkBuilder.linkDataSet(dsIKP_TASKS, dsIKP_TASK_EVENTS, PIkpTasks::getITASKID, "IEVENTTASKID");
    }

    @Override
    protected void init() throws Exception {
        initTitle();
        initDataSetAdapter(dsIKP_TASKS, IKP_TASKS, IKP_TASKS$MARK);
        initDataSetAdapter(dsIKP_TASK_EVENTS, IKP_TASK_EVENTS, IKP_TASK_EVENTS$MARK);
        initTableAndFilterConverters();
        initToolBar(toolBar, toolBarEvents);
        initToolBarAction(toolBar, IKP_TASKS, dsIKP_TASKS, this::doOperation);
        initToolBarAction(toolBarEvents, IKP_TASK_EVENTS, dsIKP_TASK_EVENTS, this::doOperationEvents);

        doRefreshAllTables();



        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/

//        Class<?> entityClass = Class.forName("ru.inversion.plshed.entity.PIkpTasks");
//        int i = 0;

        /** test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__test__*/

    }

    private void initTitle() {

        String version = loadDataFromManifestFile(ViewIkpTasksController.class, "Implementation-Build").get("version");
        String date = loadDataFromManifestFile(ViewIkpTasksController.class, "Implementation-Build").get("date");

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

    private <T> DSFXAdapter<T> initDataSetAdapter(XXIDataSet<T> dataSet, JInvTable<T> table, DSInfoBar dsInfoBar) throws Exception {
        DSFXAdapter<T> dsfx = DSFXAdapter.bind(dataSet, table, null, true);
        dsfx.setEnableFilter(true);
        dsInfoBar.init(table.getDataSetAdapter());
        dsInfoBar.addAggregator("1", AggrFuncEnum.COUNT, AggregatorType.MARK, null, null);
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
                    p.setIEVENTNPP(getNextPP());
                    logger.info(String.format("current task id = %d",dsIKP_TASKS.getCurrentRow().getITASKID()));
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

    private long getNextPP() {
        return dataSetToStream(dsIKP_TASK_EVENTS)
                .max(Comparator.comparing(PIkpTaskEvents::getIEVENTNPP))
                .map(p -> p.getIEVENTNPP())
                .orElse(0l) + 1;
    }

}

