/*
 * Copyright 2013 eWidgetFX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ewidgetfx.applauncher.apptrayfx.tileselector;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class TileSelectorSkin extends BehaviorSkinBase<TileSelector, TileSelectorBehavior> {

    private boolean isDirty;
    private boolean initialized;
    private HashMap<String, Node> tileNodeMap = new HashMap<>();

    public TileSelectorSkin(final TileSelector tileSelector) {
        super(tileSelector, new TileSelectorBehavior(tileSelector));
        initialized = false;
        isDirty = false;
        init();
    }

    private void init() {
        System.out.println(this.getClass().getName() + " init()");
        TileSelector control = getSkinnable();

        //setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        addHandlers();

        initialized = true;
        repaint();
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        System.out.println(this.getClass().getName() + " handleControlPropertyChanged()");
        super.handleControlPropertyChanged(PROPERTY);
    }

    public final void repaint() {
        System.out.println(this.getClass().getName() + " repaint()");
        isDirty = true;
        getSkinnable().requestLayout();
    }

    @Override
    public void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);

        if (!isDirty) {
            return;
        }
        System.out.println(this.getClass().getName() + " layoutChildren()");
        if (!initialized) {
            init();
        }
        if (getSkinnable().getScene() != null) {
            drawControl();
        }
        isDirty = false;
    }

    @Override
    public void dispose() {
        System.out.println(this.getClass().getName() + " dispose()");
        getChildren().clear();
    }

    private void addHandlers() {
        System.out.println(this.getClass().getName() + " addHandlers()");
//        // MouseEvents
//        setOnMousePressed(mouseHandler);
//        setOnMouseDragged(mouseHandler);
//        setOnMouseReleased(mouseHandler);
//        // TouchEvents
//        setOnTouchPressed(touchHandler);
//        setOnTouchMoved(touchHandler);
//        setOnTouchReleased(touchHandler);
    }

    // ******************** Drawing related ***********************************
    private void drawControl() {
        System.out.println(this.getClass().getName() + " drawControl()");
        getChildren().clear();
        TileSelector control = getSkinnable();
//        final double WIDTH          = control.getPrefWidth();
//        final double HEIGHT         = control.getPrefHeight();
//        final double SCALE_FACTOR_X = WIDTH / PREFERRED_WIDTH;
//        final double SCALE_FACTOR_Y = HEIGHT / PREFERRED_HEIGHT;
//        final Scale SCALE           = new Scale();
//        SCALE.setX(SCALE_FACTOR_X);
//        SCALE.setY(SCALE_FACTOR_Y);
//        SCALE.setPivotX(0);
//        SCALE.setPivotY(0);
/*
         .tile-selector .hbox {
         -fx-grid-lines-visible: true;
         -fx-border-color: white;
         -fx-border-width: 1;
         }
         */
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        //gridPane.setStyle("-fx-grid-lines-visible: true; -fx-border-color: white; -fx-border-width: 1;");
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefSize(108, 108);
        gridPane.setPrefWidth(108);
        gridPane.setPrefHeight(108);

        // add tile pane to surface
        int cellNumber = 0;

        for (int r = 0; r < control.getNumRows(); r++) {
            for (int c = 0; c < control.getNumCols(); c++) {
                cellNumber++;
                Label cellText = new Label(cellNumber + "");
                cellText.setAlignment(Pos.CENTER);
                cellText.setId("tile-selector-label-" + cellNumber);
                tileNodeMap.put(cellText.getId(), cellText);
                cellText.setPrefWidth(gridPane.getPrefHeight() / control.getNumCols());
                cellText.setFont(Font.font("SanSerif", (gridPane.getPrefHeight() / control.getNumRows()) - 4));
                final int cellNumberFinal = cellNumber;
                cellText.setOnMousePressed((MouseEvent mouseEvent) -> {
                    control.cellNumberProperty().set(cellNumberFinal);
                });
                HBox cell = new HBox();
                cell.setOnMousePressed((MouseEvent mouseEvent) -> {
                    control.cellNumberProperty().set(cellNumberFinal);
                });
                cell.getStyleClass().clear();

                cell.getStyleClass().add("hbox");
                cell.getStyleClass().add("tile-selector hbox:pressed");
                cell.getStyleClass().add("tile-selector hbox:hover");
                cell.setId("tile-selector-hbox-" + cellNumber);
                tileNodeMap.put(cell.getId(), cell);

                //cell.setStyle("-fx-border-color: white; -fx-border-width: 1;");
                cell.getChildren().add(cellText);
                gridPane.add(cell, c, r);
                gridPane.setAlignment(Pos.CENTER);
            }
        }

        // default to number 1
        HBox newCellHBox = (HBox) tileNodeMap.get("tile-selector-hbox-1");
        newCellHBox.getStyleClass().clear();
        newCellHBox.getStyleClass().add("selected-hbox");
        newCellHBox.getStyleClass().add("tile-selector hbox:pressed");
        newCellHBox.getStyleClass().add("tile-selector hbox:hover");

        Label newCellLabel = (Label) tileNodeMap.get("tile-selector-label-1");
        newCellLabel.getStyleClass().clear();
        newCellLabel.getStyleClass().add("selected-label");
        control.cellNumberProperty().set(1);

        control.cellNumberProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldCell, Number newCell) {

                // set cell
                HBox oldCellHBox = (HBox) tileNodeMap.get("tile-selector-hbox-" + oldCell.intValue());
                HBox newCellHBox = (HBox) tileNodeMap.get("tile-selector-hbox-" + newCell.intValue());
                if (oldCellHBox != null) {
                    oldCellHBox.getStyleClass().clear();
                    oldCellHBox.getStyleClass().add("hbox");
                    oldCellHBox.getStyleClass().add("tile-selector hbox:pressed");
                    oldCellHBox.getStyleClass().add("tile-selector hbox:hover");
                }
                newCellHBox.getStyleClass().clear();
                newCellHBox.getStyleClass().add("selected-hbox");
                newCellHBox.getStyleClass().add("tile-selector hbox:pressed");
                newCellHBox.getStyleClass().add("tile-selector hbox:hover");

                // set label
                Label oldCellLabel = (Label) tileNodeMap.get("tile-selector-label-" + oldCell.intValue());
                Label newCellLabel = (Label) tileNodeMap.get("tile-selector-label-" + newCell.intValue());
                if (oldCellLabel != null) {
                    oldCellLabel.getStyleClass().clear();
                    oldCellLabel.getStyleClass().add("label");
                }
                newCellLabel.getStyleClass().clear();
                newCellLabel.getStyleClass().add("selected-label");
            }
        });

        getChildren().add(gridPane);
    }

    public static String colorToCssColor(final Color COLOR) {
        final StringBuilder CSS_COLOR = new StringBuilder(19);
        CSS_COLOR.append("rgba(");
        CSS_COLOR.append((int) (COLOR.getRed() * 255)).append(", ");
        CSS_COLOR.append((int) (COLOR.getGreen() * 255)).append(", ");
        CSS_COLOR.append((int) (COLOR.getBlue() * 255)).append(", ");
        CSS_COLOR.append(COLOR.getOpacity()).append(");");
        return CSS_COLOR.toString();
    }

}
