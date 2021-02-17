package ru.inversion.plshed.interfaces;

import lovUtils.LovInterface;
import ru.inversion.dataset.SQLDataSet;
import ru.inversion.fx.form.controls.renderer.JInvTableCell;

/**
 * @author Dmitry Hvastunov
 * @created 28 Январь 2021 - 17:13
 * @project plshed
 */


public interface CellRefreshFunc {
    void refresh(JInvTableCell cell, Object o, SQLDataSet<LovInterface> lovDataSet);
}
