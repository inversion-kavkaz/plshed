package ru.inversion.plshed.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ru.inversion.fx.form.controls.JInvButton;
import ru.inversion.fx.form.controls.JInvTextField;
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


}
