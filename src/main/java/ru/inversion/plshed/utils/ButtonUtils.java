package ru.inversion.plshed.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ru.inversion.fx.form.controls.JInvButton;
import ru.inversion.fx.form.controls.JInvTextField;
import ru.inversion.fx.form.controls.JInvToolBar;
import ru.inversion.icons.IconFactory;
import ru.inversion.icons.enums.FontAwesome;

/**
 * @author Dmitry Hvastunov
 * @created 13 Декабрь 2020 - 14:32
 * @project plshed
 */

public class ButtonUtils {
    public static void setInnerGraphicButton(JInvTextField textFied,
                                               FontAwesome graphic,
                                               EventHandler<ActionEvent> handle,
                                               EventHandler<ActionEvent> validate) {
        JInvButton choiseButton = new JInvButton();
        choiseButton.setGraphic(IconFactory.getLabel(graphic));
        choiseButton.setOnAction(event -> {
            handle.handle(event);
        });
        textFied.setInnerButton(choiseButton);

        if(validate != null){
            validate.handle(null);
        }
    }

    public static void addCustomButton(FontAwesome icon, String buttonText, String tooltipText, JInvToolBar ltoolbar, EventHandler<ActionEvent> handle) {
        JInvButton newButton = new JInvButton();
        if(icon != null)
            newButton.setGraphic(IconFactory.getLabel(icon));
        newButton.setText(buttonText);
        newButton.setToolTipText(tooltipText);
        newButton.setOnAction(handle);
        ltoolbar.getItems().add(newButton);
    }

    public static JInvButton addCustomButton(FontAwesome icon, String buttonText, String tooltipText, EventHandler<ActionEvent> handle) {
        JInvButton newButton = new JInvButton();
        if(icon != null)
            newButton.setGraphic(IconFactory.getLabel(icon));
        newButton.setText(buttonText);
        newButton.setToolTipText(tooltipText);
        newButton.setOnAction(handle);
        return newButton;
    }


}
