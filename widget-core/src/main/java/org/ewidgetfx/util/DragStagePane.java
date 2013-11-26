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
package org.ewidgetfx.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class DragStagePane extends Pane {
    private Point2D anchorPt;
    private Point2D previousLocation;
    private Stage draggableStage;
    private boolean dragging;

    private final BooleanProperty filterEvents = new SimpleBooleanProperty(false);


    protected EventHandler<MouseEvent> mouseEntered = e -> {
        if (filterEvents.get()) {
            e.consume();
        }
        System.out.println(e);
    };
    protected EventHandler<MouseEvent> mouseExit = e ->  {
        if (filterEvents.get()) {
            e.consume();
        }
        System.out.println(e);
    };
    protected EventHandler<MouseEvent> mousePressed = e -> {
        if (filterEvents.get()) {
            e.consume();
        }
        anchorPt = new Point2D(e.getScreenX(), e.getScreenY());
    };
    protected EventHandler<MouseEvent> mouseClicked = e -> {
        if (filterEvents.get()) {
            e.consume();
        }
        System.out.println(e);
    };
    protected EventHandler<MouseEvent> mouseDragged = e -> {
        dragging = true;
        if (filterEvents.get()) {
            e.consume();
        }
        if (anchorPt != null && previousLocation != null) {
            draggableStage.setX(previousLocation.getX() + e.getScreenX() - anchorPt.getX());
            draggableStage.setY(previousLocation.getY() + e.getScreenY() - anchorPt.getY());
        }
    };

    protected EventHandler<MouseEvent> mouseReleased = e -> {
        dragging = false;
        if (filterEvents.get()) {
            e.consume();
        }
        previousLocation = new Point2D(draggableStage.getX(), draggableStage.getY());
    };

    protected static Map<Stage, Set<DragStagePane>> stageMapNodes = new HashMap<>();
    public DragStagePane(Stage stage) {
        draggableStage = stage;
        this.filterEvents.setValue(false);
        stage.xProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            if (!dragging) {
                previousLocation = new Point2D(draggableStage.getX(), draggableStage.getY());
            }
        });
        stage.yProperty().addListener((ObservableValue<? extends Number> observableValue, Number number, Number number2) -> {
            if (!dragging) {
                previousLocation = new Point2D(draggableStage.getX(), draggableStage.getY());
            }
        });
        init();
    }
    public DragStagePane(Stage stage, boolean filterEvents) {
      draggableStage = stage;
      this.filterEvents.setValue(filterEvents);
      init();
    }
    protected void init() {
        // build filters to intercept mouse events.
        if (filterEvents.get()) {
            addAllFilterEvents();
        } else {
            addAllEvents();
        }


        // when the stage is shown initialize the previous location
        // app developer will invoke the stage.show()
        draggableStage.addEventHandler(WindowEvent.WINDOW_SHOWN, (e) -> {
            previousLocation = new Point2D(draggableStage.getX(), draggableStage.getY());
        });
        //draggableStage.addEventFilter(WindowEvent.);

    }
    public void removeAllFilterEvents() {
        removeEventFilter(MouseEvent.MOUSE_ENTERED, mouseEntered);
        removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClicked);
        removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        removeEventFilter(MouseEvent.MOUSE_EXITED, mouseExit);
        removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);

    }
    public void removeAllEvents() {
        removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
        removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExit);
        removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);

    }
    public void addAllFilterEvents() {
        // build filters to intercept mouse events
        addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEntered);
        addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClicked);
        addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        addEventFilter(MouseEvent.MOUSE_EXITED, mouseExit);
        addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }

    public void addAllEvents() {
        // build filters to intercept mouse events
        addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEntered);
        addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
        addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClicked);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragged);
        addEventHandler(MouseEvent.MOUSE_EXITED, mouseExit);
        addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleased);
    }
//    public static void wrap(Node node, Stage stage) {
//        new DragStagePane(node, stage);
//    }
}
