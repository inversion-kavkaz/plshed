package ru.inversion.plshed.userInterfaces.presetsview;

import javafx.fxml.FXML;
import ru.inversion.dataset.DataLinkBuilder;
import ru.inversion.dataset.IDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.*;
import ru.inversion.fx.form.controls.JInvTable;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.JInvTextArea;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.plshed.entity.PIkpEventPresets;
import ru.inversion.plshed.entity.PIkpPresetParams;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;

import java.util.Optional;

import static lovUtils.LovUtils.convertTableValue;

/**
 * @author Dmitry Hvastunov
 * @created 17.02.2021
 * @project plshed
 */

public class ViewPresetsController extends JInvFXBrowserController {

    @FXML private JInvTable<PIkpEventPresets> IKP_EVENT_PRESETS;
    @FXML private JInvTable<PIkpPresetParams> IKP_PRESET_PARAMS;
    @FXML private JInvToolBar toolBar;
    @FXML private JInvToolBar toolBarParams;
    @FXML private JInvTextArea CPRESETTEXT;
    @FXML private JInvTableColumn<PIkpEventPresets, Long> IEVENTFILEDIR;

    public final XXIDataSet<PIkpEventPresets> dsIKP_EVENT_PRESETS = new XXIDataSet<> ();
    private final XXIDataSet<PIkpPresetParams> dsIKP_PRESET_PARAMS = new XXIDataSet<> ();



    private void initDataSet () throws Exception
    {
        dsIKP_EVENT_PRESETS.setTaskContext (getTaskContext ());
        dsIKP_EVENT_PRESETS.setRowClass (PIkpEventPresets.class);

        dsIKP_PRESET_PARAMS.setTaskContext (getTaskContext ());
        dsIKP_PRESET_PARAMS.setRowClass (PIkpPresetParams.class);

        DataLinkBuilder.linkDataSet(dsIKP_EVENT_PRESETS, dsIKP_PRESET_PARAMS, PIkpEventPresets::getIPRESETID, "ID_PRESET");

    }

    @Override
    protected void init() throws Exception
    {
        setTitle (getBundleString ("VIEW.TITLE"));

        initDataSet ();
        bindAdapters();
        initToolBar ();
        initActionBars();
        initCodeText();

        convertTableValue(IEVENTFILEDIR, PIkpEventFileTypeTextValue.class, getTaskContext(), true);


        doRefresh ();
    }

    private void bindAdapters() throws Exception {
        DSFXAdapter.bind (dsIKP_EVENT_PRESETS, IKP_EVENT_PRESETS, null, false);
        DSFXAdapter.bind (dsIKP_PRESET_PARAMS, IKP_PRESET_PARAMS, null, false);
    }

    private void initCodeText() {
        dsIKP_EVENT_PRESETS.addNavigationListener(dataSetNavigationEvent -> {
            CPRESETTEXT.setText(Optional.of(dataSetNavigationEvent.getNewRow().getCPRESETTEXT()).orElse(""));
        });
    }

    private void initActionBars() {
        IKP_EVENT_PRESETS.setToolBar (toolBar);
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.CREATE, (a) -> doOperation (FormModeEnum.VM_INS));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.VIEW, (a) -> doOperation (FormModeEnum.VM_SHOW));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.UPDATE, (a) -> doOperation (FormModeEnum.VM_EDIT));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.DELETE, (a) -> doOperation (FormModeEnum.VM_DEL));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh ());

        IKP_PRESET_PARAMS.setToolBar (toolBarParams);
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.CREATE, (a) -> doOperationParam (FormModeEnum.VM_INS));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.VIEW, (a) -> doOperationParam (FormModeEnum.VM_SHOW));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.UPDATE, (a) -> doOperationParam (FormModeEnum.VM_EDIT));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.DELETE, (a) -> doOperationParam (FormModeEnum.VM_DEL));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefreshParam ());
    }

    private void doRefreshParam() {
        IKP_PRESET_PARAMS.executeQuery ();
    }

    private void doRefresh ()
    {
        IKP_EVENT_PRESETS.executeQuery ();
    }

    private void initToolBar ()
    {
        toolBar.setStandartActions (ActionFactory.ActionTypeEnum.CREATE,
                ActionFactory.ActionTypeEnum.VIEW,
                ActionFactory.ActionTypeEnum.UPDATE,
                ActionFactory.ActionTypeEnum.DELETE,
                ActionFactory.ActionTypeEnum.REFRESH);

        toolBarParams.setStandartActions (ActionFactory.ActionTypeEnum.CREATE,
                ActionFactory.ActionTypeEnum.VIEW,
                ActionFactory.ActionTypeEnum.UPDATE,
                ActionFactory.ActionTypeEnum.DELETE,
                ActionFactory.ActionTypeEnum.REFRESH);


//        toolBar.getItems ().add (ActionFactory.createButton(ActionFactory.ActionTypeEnum.SETTINGS, (a) -> JInvMainFrame.showSettingsPane ()));
    }

    private void doOperation ( JInvFXFormController.FormModeEnum mode )
    {
        PIkpEventPresets p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpEventPresets ();
                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = dsIKP_EVENT_PRESETS.getCurrentRow ();
                break;
        }

        if (p != null)
            new FXFormLauncher<PIkpEventPresets>(getTaskContext (), getViewContext (), EditIkpEventPresetsController.class)
                    .dataObject (p)
                    .dialogMode (mode)
                    .initProperties (getInitProperties ())
                    .callback (this::doFormResult)
                    .modal (true)
                    .show ();
    }

    private void doFormResult ( JInvFXFormController.FormReturnEnum ok, JInvFXFormController<PIkpEventPresets> dctl )
    {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok)
        {
            switch (dctl.getFormMode ())
            {
                case VM_INS:
                    dsIKP_EVENT_PRESETS.insertRow (dctl.getDataObject (), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    break;
                case VM_EDIT:
                    dsIKP_EVENT_PRESETS.updateCurrentRow (dctl.getDataObject ());
                    break;
                case VM_DEL:
                    dsIKP_EVENT_PRESETS.removeCurrentRow ();
                    break;
                default:
                    break;
            }
        }

        IKP_EVENT_PRESETS.requestFocus ();
    }

    private void doOperationParam ( JInvFXFormController.FormModeEnum mode )
    {
        PIkpPresetParams p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpPresetParams ();
                p.setID_PRESET(dsIKP_EVENT_PRESETS.getCurrentRow().getIPRESETID());
                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = dsIKP_PRESET_PARAMS.getCurrentRow ();
                break;
        }

        if (p != null)
            new FXFormLauncher<PIkpPresetParams> (getTaskContext (), getViewContext (),EditIkpPresetParamsController.class)
                    .dataObject (p)
                    .dialogMode (mode)
                    .initProperties (getInitProperties ())
                    .callback (this::doFormResultParam)
                    .modal (true)
                    .show ();
    }

    private void doFormResultParam ( JInvFXFormController.FormReturnEnum ok, JInvFXFormController<PIkpPresetParams> dctl )
    {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok)
        {
            switch (dctl.getFormMode ())
            {
                case VM_INS:
                    dsIKP_PRESET_PARAMS.insertRow (dctl.getDataObject (), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    break;
                case VM_EDIT:
                    dsIKP_PRESET_PARAMS.updateCurrentRow (dctl.getDataObject ());
                    break;
                case VM_DEL:
                    dsIKP_PRESET_PARAMS.removeCurrentRow ();
                    break;
                default:
                    break;
            }
        }

        IKP_PRESET_PARAMS.requestFocus ();
    }

    @Override
    protected boolean onOK() {
        return super.onOK();
    }
}
