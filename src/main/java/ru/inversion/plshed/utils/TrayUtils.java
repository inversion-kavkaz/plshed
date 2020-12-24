package ru.inversion.plshed.utils;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import ru.inversion.fx.form.AbstractBaseController;
import ru.inversion.fx.form.ViewContext;
import ru.inversion.plshed.userInterfaces.mainui.ViewIkpTasksController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import static ru.inversion.fx.app.es.JInvErrorService.logger;

/**
 * @author Dmitry Hvastunov
 * @created 21 Декабрь 2020 - 10:55
 * @project plshed
 */

public class TrayUtils {

    private static TrayIcon trayIcon;
    private static SystemTray tray;
    private static PopupMenu popup;


    public static void initTray(ViewContext vc, AbstractBaseController controller) {
        URL url = System.class.getResource("/ru/inversion/plshed/image/trayLogo.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        trayIcon = new TrayIcon(image);
        tray = SystemTray.getSystemTray();
        popup = new PopupMenu();
        trayIcon.setToolTip(controller.getBundleString("TRAY.TOOLTYPE"));
        //Добавляем меню трея
        MenuItem exit = new MenuItem(controller.getBundleString("TRAY.MENU.EXIT"));
        popup.add(exit);
        trayIcon.setPopupMenu(popup);
        exit.addActionListener(a -> {
            tray.remove(trayIcon);
            Platform.runLater(() -> {
                ((ViewIkpTasksController) controller).exit();
            });

        });

        trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int countClick = e.getClickCount();
                if (countClick == 2) {
                    Platform.runLater(() -> {
                        controller.getViewContext().getStage().show();
                        vc.getStage().show();
                        tray.remove(trayIcon);
                    });
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        vc.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                vc.getStage().hide();
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    logger.error(controller.getBundleString("TRAY.ERROR.NOTADDED"));
                }
            }
        });
    }
}
