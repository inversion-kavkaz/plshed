package ru.inversion.plshed.userInterfaces.mainui.lovPackage;

import javafx.fxml.FXML;
import ru.inversion.dataset.XXIDataSet;
import ru.inversion.dataset.aggr.AggrFuncEnum;
import ru.inversion.dataset.fx.DSFXAdapter;
import ru.inversion.fx.form.JInvFXBrowserController;
import ru.inversion.fx.form.controls.JInvTable;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.fx.form.controls.dsbar.DSInfoBar;
import ru.inversion.fx.form.controls.table.toolbar.AggregatorType;
import ru.inversion.plshed.entity.lovEntity.PDual;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  XDWeloper
 * @since   Mon Apr 19 12:25:19 MSK 2021
 */
public class ViewDualController extends JInvFXBrowserController 
{
    @FXML private JInvTable<PDual> DUAL;
    @FXML private JInvToolBar toolBar;

    @FXML private DSInfoBar DUAL$MARK;


    private final XXIDataSet<PDual> dsDUAL = new XXIDataSet<> ();
    public List<String> resultValues = new ArrayList<>();
    private String isMulti;

    private void initDataSet () throws Exception
    {
        dsDUAL.setTaskContext (getTaskContext ());
        dsDUAL.setRowClass (PDual.class);
        String sqlQuery = getInitParameter("sql");
        dsDUAL.setSQL(sqlQuery);
    }

    @Override
    protected void init() throws Exception
    {
        setTitle (getBundleString ("VIEW.TITLE"));

        isMulti = getInitParameter("multi") != null ? getInitParameter("multi") : "N";
        initDataSet ();
        DSFXAdapter<PDual> dsfx = DSFXAdapter.bind (dsDUAL, DUAL, null, true); 

        dsfx.setEnableFilter (true);
        DUAL$MARK.init (DUAL.getDataSetAdapter ());
        DUAL$MARK.addAggregator ("1", AggrFuncEnum.COUNT, AggregatorType.MARK, null, null);
        DUAL.showMarkColumnProperty().setValue(isMulti.equalsIgnoreCase("Y"));

        DUAL.setOnMousePressed(event -> {
            if(event.getClickCount() == 2)
                close(FormReturnEnum.RET_OK);
        });
        doRefresh ();
    }        

    private void doRefresh ()
    {
        DUAL.executeQuery ();
    }

    @Override
    protected boolean onOK() {
        if(dsDUAL.computeNumberMarkedRows() > 0)
            dsDUAL.getMarkedRowIterator(PDual::isMark).forEachRemaining(r -> {
                resultValues.add(r.getVAL());
            });
        else {
                resultValues.add(dsDUAL.getCurrentRow().getVAL());
            }
            return super.onOK();
        }



}

