package ru.inversion.plshed.interfaces;

import ru.inversion.fx.form.JInvFXFormController;

/**
 * @author Dmitry Hvastunov
 * @created 15 Декабрь 2020 - 17:48
 * @project plshed
 */


@FunctionalInterface
public
interface callFunc {
    void doOperation(JInvFXFormController.FormModeEnum mode) ;
}
