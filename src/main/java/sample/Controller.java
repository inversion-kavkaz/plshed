package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

class Controller {
    @FXML
    GridPane GRID;
    @FXML
    Button BUTTON;
    @FXML
    Label lab1;
    @FXML
    TextField text1;
    @FXML
    AnchorPane ANCHOR;

    private boolean isRowVisible = false;
    private static double maxRowHeight = 30;
    private static SimpleDoubleProperty ph = new SimpleDoubleProperty(30);
    private static SimpleDoubleProperty aph = new SimpleDoubleProperty(275);
    private static  int sleepProp = 10;
    private static double prefH = 275;

    @FXML
    public void initialize(){
        GRID.getRowConstraints().get(1).prefHeightProperty().bind(ph);


        GRID.getChildren().forEach(n -> {
            if(GridPane.getRowIndex(n) != null && GridPane.getRowIndex(n) == 1)
                n.opacityProperty().bind(ph.divide(30));
        });
    }

    public void onButton(ActionEvent actionEvent) throws InterruptedException {
        isRowVisible = !isRowVisible;
        if(isRowVisible){
            new Thread(() -> {
                while (maxRowHeight > 0) {
                    ph.set(maxRowHeight --);
                    ANCHOR.getScene().getWindow().setHeight(prefH - 30 + maxRowHeight);
                    try {
                        Thread.sleep(sleepProp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else{
            new Thread(() -> {
                while (maxRowHeight < 30) {
                    ph.set(maxRowHeight ++);
                    ANCHOR.getScene().getWindow().setHeight(prefH - 30 + maxRowHeight);
                    try {
                        Thread.sleep(sleepProp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

//        lab1.setVisible(!isRowVisible);
//        text1.setVisible(!isRowVisible);
        BUTTON.setText(isRowVisible ? "invisible" : "visible");



    }
}
