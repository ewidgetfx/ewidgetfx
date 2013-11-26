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
package org.ewidgetfx.core;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.ewidgetfx.util.DragStagePane;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public final class WidgetStage extends Stage {

    private Point2D anchorPt;
    private Point2D previousLocation;
    private boolean dragging;
    private Widget widget;
    private Parent root;
    private boolean hover = false;

    public WidgetStage(Stage owner, Widget widget) {
        if (widget.getDecoration() != Widget.DECORATION.STAGED_OS_TITLE_BAR) {
            initStyle(StageStyle.TRANSPARENT);
        }
        // Possible bug, On MacOSX dragging the widget will drag the parent at the same time.
        // This doesn't happen on Windows 7
        //initOwner(owner);
        //initModality(Modality.NONE);
        this.widget = widget;
        widget.setParentStage(this);
        Widget.DECORATION decoration = widget.getDecoration();
        Group group = new Group();
        root = group;
        Node titleNode = null;
        switch (decoration) {
            case STAGED_CONFIG_CLOSE:
                VBox vBox1 = new VBox();
                titleNode = createTitleConfigClose();
                vBox1.getChildren().add(titleNode);
                vBox1.getChildren().add(widget.getAsNode());
                group.getChildren().add(vBox1);
                break;
            case STAGED_CLOSE:
                VBox vBox = new VBox();
                titleNode = createTitleClose();
                vBox.getChildren().add(titleNode);
                vBox.getChildren().add(widget.getAsNode());
                group.getChildren().add(vBox);
                break;
            case STAGED_UNDECORATED:
                group.getChildren().add(widget.getAsNode());
                root = group;
                break;
        }

        // drag start
        root.setOnMousePressed(me -> {
            anchorPt = new Point2D(me.getScreenX(), me.getScreenY());
        });

        // dragging
        root.setOnMouseDragged(me -> {
            dragging = true;
            if (anchorPt != null && previousLocation != null) {
                setX(previousLocation.getX() + me.getScreenX() - anchorPt.getX());
                setY(previousLocation.getY() + me.getScreenY() - anchorPt.getY());
            }
        });

        // drag stopped
        root.setOnMouseReleased(me -> {
            dragging = false;
            previousLocation = new Point2D(getX(), getY());
        });

        if (titleNode != null) {
            final Node finalTitleNode = titleNode;
            FadeTransition fadeInTransition = new FadeTransition();
            fadeInTransition.setDuration(Duration.millis(250));
            fadeInTransition.setFromValue(0);
            fadeInTransition.setToValue(1);
            fadeInTransition.setNode(titleNode);

            FadeTransition fadeOutTransition = new FadeTransition();
            fadeOutTransition.setDelay(Duration.millis(2000));
            fadeOutTransition.setDuration(Duration.millis(250));
            fadeOutTransition.setFromValue(1);
            fadeOutTransition.setToValue(0);
            fadeOutTransition.setNode(titleNode);
            EventHandler mouseEntered = me -> {
                //if (!hover) {
                if (me.getSource() == finalTitleNode) {
                    fadeOutTransition.stop();
                    System.out.println("title is detected");
                } else {
                    System.out.println("root is detected");
                }
                fadeInTransition.stop();
                fadeInTransition.play();
                //hover = true;
                //}
            };
            EventHandler mouseExited = me -> {
                //if (hover) {
                fadeOutTransition.stop();
                fadeOutTransition.play();
                //hover = false;
                //}
            };
            titleNode.setOnMouseEntered(mouseEntered);
            titleNode.setOnMouseExited(mouseExited);
            root.setOnMouseEntered(mouseEntered);
            root.setOnMouseExited(mouseExited);
        }
        // close the widget
        root.setOnMouseClicked(me -> {
            if (me.getClickCount() == 3) {
                close();
            }
        });
        xProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            if (!dragging) {
                previousLocation = new Point2D(getX(), getY());
            }
        });
        yProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            if (!dragging) {
                previousLocation = new Point2D(getX(), getY());
            }
        });
        final Scene scene = new Scene(root, widget.getAsNode().getPrefWidth(), widget.getAsNode().getPrefHeight(), null);

        setScene(scene);

        // initialize for initial drag of region
        addEventHandler(WindowEvent.WINDOW_SHOWN, (e) -> {
            previousLocation = new Point2D(getX(), getY());
        });

        addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> {
            close();
        });

    }

    public Node createTitleClose() {

        // basic design
        // +----------------------+---+
        // |   /  /  //  /// //// | X |
        // +----------------------+---+
        //   1) black translucent background color
        //   2)
        //   3) white X in the 2nd column
        //   4) 1st column is draggable
        Node title = null;
        DragStagePane dragStagePane = new DragStagePane(this);

        ReadOnlyDoubleProperty widgetWidth = widget.getAsNode().prefWidthProperty();
        HBox closeContainer = createCloseButton();
        HBox hBox = new HBox();

        String bgStyling = "-fx-background-color: rgba(0,0,0,.70); "
                + "-fx-background-radius: 2.0;"
                + "-fx-border-color: rgba(255,255,255,.70);"
                + "-fx-background-insets: 3;"
                + //"-fx-border-insets: 3;" +
                "-fx-border-width: 1;"
                + "-fx-border-radius: 2;"
                + "";
//        String styling = "-fx-background-color: linear (0%,0%) to (100%,100%) stops (0.0, rgba(0,0,0,.70)) (1.0,rgba(255,255,255,.70)); " +
//                "-fx-background-radius: 4.0;" +
//                "-fx-border-color: rgba(255,255,255,.70);" +
//                "-fx-background-insets: 3;"
//                ;
        double insetOffset = hBox.getInsets().getLeft() + hBox.getInsets().getRight();
        dragStagePane.setPrefWidth(widget.getAsNode().getPrefWidth() + insetOffset - 20);
        hBox.setMinHeight(20);
        hBox.setMaxHeight(20);

        hBox.setStyle(bgStyling);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(dragStagePane, closeContainer);

        return hBox;
    }

    public Node createTitleConfigClose() {

        // basic design
        // +----------------------+---+
        // |   /  /  //  /// //// | X |
        // +----------------------+---+
        //   1) black translucent background color
        //   2)
        //   3) white X in the 2nd column
        //   4) 1st column is draggable
        Node title = null;
        DragStagePane dragStagePane = new DragStagePane(this);

        ReadOnlyDoubleProperty widgetWidth = widget.getAsNode().prefWidthProperty();

        HBox closeContainer = createCloseButton();
        HBox configContainer = createConfigButton();
        HBox hBox = new HBox();

        String bgStyling = "-fx-background-color: rgba(0,0,0,.70); "
                + "-fx-background-radius: 2.0;"
                + "-fx-border-color: rgba(255,255,255,.70);"
                + "-fx-background-insets: 3;"
                + //"-fx-border-insets: 3;" +
                "-fx-border-width: 1;"
                + "-fx-border-radius: 2;"
                + "";
//        String styling = "-fx-background-color: linear (0%,0%) to (100%,100%) stops (0.0, rgba(0,0,0,.70)) (1.0,rgba(255,255,255,.70)); " +
//                "-fx-background-radius: 4.0;" +
//                "-fx-border-color: rgba(255,255,255,.70);" +
//                "-fx-background-insets: 3;"
//                ;
        double insetOffset = hBox.getInsets().getLeft() + hBox.getInsets().getRight();
        dragStagePane.setPrefWidth(widget.getAsNode().getPrefWidth() + insetOffset - 20);
        hBox.setMinHeight(20);
        hBox.setMaxHeight(20);

        hBox.setStyle(bgStyling);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(dragStagePane, configContainer, closeContainer);

        return hBox;
    }

    private HBox createCloseButton() {
        SVGPath closeButton = new SVGPath();
        closeButton.setContent("M24.778,21.419 19.276,15.917 24.777,10.415 21.949,7.585 16.447,13.087 10.945,7.585 8.117,10.415 13.618,15.917 8.116,21.419 10.946,24.248 16.447,18.746 21.948,24.248z");
        closeButton.setFill(Color.WHITE);

        Group closeButtonBackground = new Group();

        closeButtonBackground.getChildren().add(closeButton);
        closeButton.setScaleX(closeButtonBackground.getScaleX() * .4);
        closeButton.setScaleY(closeButtonBackground.getScaleY() * .4);
        HBox closeContainer = new HBox(3);
        String closeContainerStyle = "-fx-background-color: rgba(0,0,0,.70); "
                + "-fx-background-radius: 1.0;"
                + "-fx-border-color: rgba(255,255,255,.70);"
                + "-fx-background-insets: 1;"
                + //"-fx-border-insets: 3;" +
                "-fx-border-width: 1;"
                + "-fx-border-radius: 1;"
                + "";
        closeContainer.setStyle(closeContainerStyle);
        closeContainer.setMinWidth(20);
        closeContainer.setMaxWidth(20);

        closeContainer.setAlignment(Pos.CENTER);
        closeContainer.getChildren().add(closeButtonBackground);
        closeContainer.setOnMousePressed(mouseEvent -> {
            close();
        });
        return closeContainer;
    }

    private HBox createConfigButton() {
        SVGPath configButton = new SVGPath();
        configButton.setContent("M31.229,17.736c0.064-0.571,0.104-1.148,0.104-1.736s-0.04-1.166-0.104-1.737l-4.377-1.557c-0.218-0.716-0.504-1.401-0.851-2.05l1.993-4.192c-0.725-0.91-1.549-1.734-2.458-2.459l-4.193,1.994c-0.647-0.347-1.334-0.632-2.049-0.849l-1.558-4.378C17.165,0.708,16.588,0.667,16,0.667s-1.166,0.041-1.737,0.105L12.707,5.15c-0.716,0.217-1.401,0.502-2.05,0.849L6.464,4.005C5.554,4.73,4.73,5.554,4.005,6.464l1.994,4.192c-0.347,0.648-0.632,1.334-0.849,2.05l-4.378,1.557C0.708,14.834,0.667,15.412,0.667,16s0.041,1.165,0.105,1.736l4.378,1.558c0.217,0.715,0.502,1.401,0.849,2.049l-1.994,4.193c0.725,0.909,1.549,1.733,2.459,2.458l4.192-1.993c0.648,0.347,1.334,0.633,2.05,0.851l1.557,4.377c0.571,0.064,1.148,0.104,1.737,0.104c0.588,0,1.165-0.04,1.736-0.104l1.558-4.377c0.715-0.218,1.399-0.504,2.049-0.851l4.193,1.993c0.909-0.725,1.733-1.549,2.458-2.458l-1.993-4.193c0.347-0.647,0.633-1.334,0.851-2.049L31.229,17.736zM16,20.871c-2.69,0-4.872-2.182-4.872-4.871c0-2.69,2.182-4.872,4.872-4.872c2.689,0,4.871,2.182,4.871,4.872C20.871,18.689,18.689,20.871,16,20.871z");
        configButton.setFill(Color.WHITE);

        Group closeButtonBackground = new Group();

        closeButtonBackground.getChildren().add(configButton);
        configButton.setScaleX(closeButtonBackground.getScaleX() * .4);
        configButton.setScaleY(closeButtonBackground.getScaleY() * .4);
        HBox container = new HBox(3);
        String closeContainerStyle = "-fx-background-color: rgba(0,0,0,.70); "
                + "-fx-background-radius: 1.0;"
                + "-fx-border-color: rgba(255,255,255,.70);"
                + "-fx-background-insets: 1;"
                + //"-fx-border-insets: 3;" +
                "-fx-border-width: 1;"
                + "-fx-border-radius: 1;"
                + "";
        container.setStyle(closeContainerStyle);
        container.setMinWidth(20);
        container.setMaxWidth(20);

        container.setAlignment(Pos.CENTER);
        container.getChildren().add(closeButtonBackground);
        container.setOnMousePressed(mouseEvent -> {

        });
        return container;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    @Override
    public void close() {
        super.close();
        widget.stop();
        widget.setParentStage(null);

        Widget.DECORATION decoration = widget.getDecoration();
        Group group = (Group) root;
        group.getChildren().clear();
//        switch (decoration) {
//            case STAGED_CLOSE:
//
//                group.getChildren().clear();
//                break;
//            case STAGED_UNDECORATED:
//
//                group.getChildren().clear();
//                break;
//        }

        widget.getAsNode().getChildren().clear();
        this.widget = null;
        //System.out.println("stage's close");
    }
}
