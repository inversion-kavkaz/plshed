package ru.inversion.plshed.userInterfaces.presetsview;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import ru.inversion.dataset.DataLinkBuilder;
import ru.inversion.dataset.IDataSet;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.ActionFactory;
import ru.inversion.fx.form.FXFormLauncher;
import ru.inversion.fx.form.JInvFXBrowserController;
import ru.inversion.fx.form.JInvFXFormController;
import ru.inversion.fx.form.controls.JInvTable;
import ru.inversion.fx.form.controls.JInvTableColumn;
import ru.inversion.fx.form.controls.JInvTextArea;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.meta.EntityMetadataFactory;
import ru.inversion.meta.IEntityProperty;
import ru.inversion.plshed.entity.PIkpEventPresets;
import ru.inversion.plshed.entity.PIkpPresetGr;
import ru.inversion.plshed.entity.PIkpPresetParams;
import ru.inversion.plshed.entity.lovEntity.PIkpEventFileTypeTextValue;
import ru.inversion.plshed.entity.lovEntity.PIkpPeriodTextValue;

import static lovUtils.LovUtils.convertTableValue;

/**
 * @author Dmitry Hvastunov
 * @created 17.02.2021
 * @project plshed
 */

public class ViewPresetsController extends JInvFXBrowserController {

    @FXML private JInvTable<PIkpEventPresets> IKP_EVENT_PRESETS;
    @FXML private JInvTable<PIkpPresetParams> IKP_PRESET_PARAMS;
    @FXML private JInvTable<PIkpPresetGr> IKP_PRESET_GR;
    @FXML private JInvToolBar toolBar;
    @FXML private ButtonBar downToolBar;

    @FXML private JInvTextArea CPRESETTEXT;
    @FXML private JInvTextArea CSPRSQL;
    @FXML private JInvTableColumn<PIkpEventPresets, Long> IEVENTFILEDIR;
    @FXML private JInvTableColumn<PIkpPresetParams, Long> IS_SPR;
    @FXML private JInvTableColumn<PIkpPresetParams, Long> IS_MULTI;




    private final XXIDataSet<PIkpPresetGr> dsIKP_PRESET_GR = new XXIDataSet<> ();
    public final XXIDataSet<PIkpEventPresets> dsIKP_EVENT_PRESETS = new XXIDataSet<> ();
    private final XXIDataSet<PIkpPresetParams> dsIKP_PRESET_PARAMS = new XXIDataSet<> ();



    private void initDataSet ()
    {
        dsIKP_PRESET_GR.setTaskContext (getTaskContext ());
        dsIKP_PRESET_GR.setRowClass (PIkpPresetGr.class);


        dsIKP_EVENT_PRESETS.setTaskContext (getTaskContext ());
        dsIKP_EVENT_PRESETS.setRowClass (PIkpEventPresets.class);

        dsIKP_PRESET_PARAMS.setTaskContext (getTaskContext ());
        dsIKP_PRESET_PARAMS.setRowClass (PIkpPresetParams.class);

        DataLinkBuilder.linkDataSet(dsIKP_PRESET_GR, dsIKP_EVENT_PRESETS, PIkpPresetGr::getIPRESETGRID, "IPRESETGR");
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
        initConvertersForTableValue();

        downToolBar.setVisible(!getFormMode().equals(FormModeEnum.VM_EDIT));
        //toolBar.setVisible(getFormMode().equals(FormModeEnum.VM_EDIT));
        doRefresh ();
    }

    private void initConvertersForTableValue() throws ru.inversion.dataset.DataSetException {
        convertTableValue(IEVENTFILEDIR, PIkpEventFileTypeTextValue.class, getTaskContext(), true);
        convertTableValue(IS_SPR, PIkpPeriodTextValue.class, getTaskContext(), true);
        convertTableValue(IS_MULTI, PIkpPeriodTextValue.class, getTaskContext(), true);
    }

    private void bindAdapters() throws Exception {
        DSFXAdapter.bind (dsIKP_PRESET_GR, IKP_PRESET_GR, null, false);
        DSFXAdapter.bind (dsIKP_EVENT_PRESETS, IKP_EVENT_PRESETS, null, false)
                .bindControl(CPRESETTEXT,"CPRESETTEXT");

        DSFXAdapter.bind (dsIKP_PRESET_PARAMS, IKP_PRESET_PARAMS, null, false)
        .bindControl(CSPRSQL,"CSPRSQL");
    }

    private void initActionBars() {
        IKP_EVENT_PRESETS.setToolBar (toolBar);
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.CREATE, (a) -> doEventPresetOperation(FormModeEnum.VM_INS));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.CREATE_BY, (a) -> doEventPresetOperation(FormModeEnum.VM_NONE));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.VIEW, (a) -> doEventPresetOperation(FormModeEnum.VM_SHOW));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.UPDATE, (a) -> doEventPresetOperation(FormModeEnum.VM_EDIT));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.DELETE, (a) -> doEventPresetOperation(FormModeEnum.VM_DEL));
        IKP_EVENT_PRESETS.setAction (ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh ());

        IKP_PRESET_PARAMS.setToolBar (toolBar);
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.CREATE, (a) -> doOperationParam (FormModeEnum.VM_INS));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.CREATE_BY, (a) -> doOperationParam (FormModeEnum.VM_NONE));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.VIEW, (a) -> doOperationParam (FormModeEnum.VM_SHOW));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.UPDATE, (a) -> doOperationParam (FormModeEnum.VM_EDIT));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.DELETE, (a) -> doOperationParam (FormModeEnum.VM_DEL));
        IKP_PRESET_PARAMS.setAction (ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh ());

        IKP_PRESET_GR.setToolBar (toolBar);
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.CREATE, (a) -> doGrOperation (FormModeEnum.VM_INS));
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.CREATE_BY, (a) -> doGrOperation (FormModeEnum.VM_NONE));
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.VIEW, (a) -> doGrOperation (FormModeEnum.VM_SHOW));
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.UPDATE, (a) -> doGrOperation (FormModeEnum.VM_EDIT));
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.DELETE, (a) -> doGrOperation (FormModeEnum.VM_DEL));
        IKP_PRESET_GR.setAction (ActionFactory.ActionTypeEnum.REFRESH, (a) -> doRefresh ());
    }




    private void doRefresh ()
    {
        IKP_PRESET_GR.executeQuery ();
        IKP_EVENT_PRESETS.executeQuery ();
        IKP_PRESET_PARAMS.executeQuery ();
    }

    private void initToolBar ()
    {
        toolBar.setStandartActions (
                ActionFactory.ActionTypeEnum.CREATE,
                ActionFactory.ActionTypeEnum.CREATE_BY,
                ActionFactory.ActionTypeEnum.VIEW,
                ActionFactory.ActionTypeEnum.UPDATE,
                ActionFactory.ActionTypeEnum.DELETE,
                ActionFactory.ActionTypeEnum.REFRESH);

//        toolBar.getItems ().add (ActionFactory.createButton(ActionFactory.ActionTypeEnum.SETTINGS, (a) -> JInvMainFrame.showSettingsPane ()));
    }

    private void doEventPresetOperation(JInvFXFormController.FormModeEnum mode )  {
        PIkpEventPresets p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpEventPresets ();
                p.setIPRESETGR(dsIKP_PRESET_GR.getCurrentRow().getIPRESETGRID());
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
                    //dsIKP_EVENT_PRESETS.insertRow (dctl.getDataObject (), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    doRefresh();
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

    private void doGrOperation ( JInvFXFormController.FormModeEnum mode )
    {
        PIkpPresetGr p = null;

        switch (mode) {
            case VM_INS:
                p = new PIkpPresetGr ();
                break;
            case VM_NONE:
                if (dsIKP_PRESET_GR.getCurrentRow () == null)
                    break;
                mode = FormModeEnum.VM_INS;
                p = new PIkpPresetGr ();
                for (IEntityProperty<PIkpPresetGr, ?> ep : EntityMetadataFactory.getEntityMetaData (PIkpPresetGr.class).getPropertiesMap ().values ())
                    if (! (ep.isTransient () || ep.isId ()))
                        ep.invokeSetter (p, ep.invokeGetter (dsIKP_PRESET_GR.getCurrentRow ()));
                break;
            case VM_EDIT:
            case VM_SHOW:
            case VM_DEL:
                p = dsIKP_PRESET_GR.getCurrentRow ();
                break;
        }

        if (p != null)
            new FXFormLauncher<PIkpPresetGr> (this, (Class<? extends JInvFXFormController<? super PIkpPresetGr>>) EditIkpPresetGrController.class)
                    .dataObject (p)
                    .dialogMode (mode)
                    .initProperties (getInitProperties ())
                    .callback (this::doGRFormResult)
                    .doModal ();
    }

    //
// doFormResult
//
    private void doGRFormResult ( FormReturnEnum ok, JInvFXFormController<PIkpPresetGr> dctl )
    {
        if (JInvFXFormController.FormReturnEnum.RET_OK == ok)
        {
            switch (dctl.getFormMode ())
            {
                case VM_INS:
                    //dsIKP_PRESET_GR.insertRow (dctl.getDataObject (), IDataSet.InsertRowModeEnum.AFTER_CURRENT, true);
                    doRefresh();
                    break;
                case VM_EDIT:
                    dsIKP_PRESET_GR.updateCurrentRow (dctl.getDataObject ());
                    break;
                case VM_DEL:
                    dsIKP_PRESET_GR.removeCurrentRow ();
                    break;
                default:
                    break;
            }
        }

        IKP_PRESET_GR.requestFocus ();
    }


    @Override
    protected boolean onOK() {
        return super.onOK();
    }
}
