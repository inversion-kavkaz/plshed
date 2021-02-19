package ru.inversion.plshed.utils;

import javafx.animation.Transition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * @author Dmitry Hvastunov
 * @created 17.02.2021
 * @project plshed
 */

public class GridPaneRowAnim {
    private boolean isCollapse;
    private final double prefRowHeight;
    private final SimpleDoubleProperty deltaHeight = new SimpleDoubleProperty();

    private final int duration = 250;
    private final double prefMainWinHeight;
    private final GridPane gridPane;
    private final Window mainWin;
    private final int rowIndex;
    private Transition gridRowTransition;
    private final int COLLAPSE = 1;
    private final int UNCOLLAPSE = -1;

    public GridPaneRowAnim(GridPane gridPane, Window mainWin, int rowIndex) {
        this.gridPane = gridPane;
        this.mainWin = mainWin;
        this.rowIndex = rowIndex;
        this.prefRowHeight = gridPane.getRowConstraints().get(rowIndex).getPrefHeight();
        this.prefMainWinHeight = mainWin.getHeight();
        this.isCollapse = false;
        initProps();
    }

    private void initProps() {
        deltaHeight.set(prefRowHeight);
        this.gridPane.getRowConstraints().get(this.rowIndex).prefHeightProperty().bind(deltaHeight);
        this.gridPane.getChildren().forEach(n -> {
            if(GridPane.getRowIndex(n) != null && GridPane.getRowIndex(n) == this.rowIndex)
                n.opacityProperty().bind(deltaHeight.divide(prefRowHeight));
        });

        gridRowTransition = new Transition() {
            {setCycleDuration(Duration.millis(duration));}
            @Override
            protected void interpolate(double frac) {
                deltaHeight.set(prefRowHeight - prefRowHeight * frac);
            }
        };
    }

    public void setcollapse(boolean collapse){
        if(isCollapse && collapse) return;
            gridRowTransition.setRate(collapse ? COLLAPSE : UNCOLLAPSE);
            gridRowTransition.play();
            isCollapse = collapse;
     }


}
