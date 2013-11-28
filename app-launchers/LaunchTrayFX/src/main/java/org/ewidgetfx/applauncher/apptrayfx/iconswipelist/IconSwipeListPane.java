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
package org.ewidgetfx.applauncher.apptrayfx.iconswipelist;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public final class IconSwipeListPane extends Group {

    public static enum SwipeAxis {

        VERTICAL, HORIZONTAL
    };

    protected DoubleProperty scroll = new SimpleDoubleProperty(0.0);
    protected DoubleProperty clickPos = new SimpleDoubleProperty();
    protected DoubleProperty scrollOrigin = new SimpleDoubleProperty();
    protected BooleanProperty buttonDown = new SimpleBooleanProperty(false);
    protected DoubleProperty dragDelta = new SimpleDoubleProperty();
    protected Timeline dragTimeLine = null;
    protected BooleanProperty noScroll = new SimpleBooleanProperty();
    protected Pane iconList;
    private SwipeAxis swipeAxis = SwipeAxis.HORIZONTAL;

    /* mouse swipe events */
    protected EventHandler<MouseEvent> filterMousePressed;
    protected EventHandler<MouseEvent> filterMouseDragged;
    protected EventHandler<MouseEvent> filterMouseReleased;
    private final IconSwipeListInfo iconSwipeListInfo;

    public IconSwipeListPane(IconSwipeListInfo iconSwipeListInfo, Pane iconList, SwipeAxis swipeAxis) {
        this.iconSwipeListInfo = iconSwipeListInfo;
        this.iconList = iconList;
        this.swipeAxis = swipeAxis;
        init();
    }

    protected void init() {

        // when user mouse pressed the app icon.
        filterMousePressed = (mouseEvent) -> {
            animStop();
            if (swipeAxis == SwipeAxis.HORIZONTAL) {
                clickPos.set(mouseEvent.getSceneX());
            } else {
                clickPos.set(mouseEvent.getSceneY());
            }
            scrollOrigin.set(scroll.get());
            buttonDown.set(true);
        };

        // when user drags the app icons vertically up or down
        filterMouseDragged = (mouseEvent) -> {
            if (swipeAxis == SwipeAxis.HORIZONTAL) {
                if (iconList.getWidth() > (iconSwipeListInfo.getWidth() - 40)) {
                    double prevX = scroll.get();
                    updateScrollAxis(mouseEvent.getX());
                    dragDelta.set(scroll.subtract(prevX).doubleValue());
                }
            } else {
                if (iconList.getHeight() > (iconSwipeListInfo.getHeight() - 40)) {
                    double prevY = scroll.get();
                    updateScrollAxis(mouseEvent.getY());
                    dragDelta.set(scroll.subtract(prevY).doubleValue());
                }
            }
        };

        // when the user finishes dragging and releases (inertia)
        filterMouseReleased = (mouseEvent) -> {
            if (swipeAxis == SwipeAxis.HORIZONTAL) {
                if (iconList.getWidth() > (iconSwipeListInfo.getWidth() - 40)) {
                    updateScrollAxis(mouseEvent.getX());
                    animStart(dragDelta.doubleValue());
                }
            } else {
                if (iconList.getHeight() > (iconSwipeListInfo.getHeight() - 40)) {
                    updateScrollAxis(mouseEvent.getY());
                    animStart(dragDelta.doubleValue());
                }
            }

            dragDelta.set(0);
            buttonDown.set(false);
        };

        // bind scrollY property
        final Node iconList = this.iconList;
        ChangeListener<Number> scrollListener = null;
        if (swipeAxis == SwipeAxis.HORIZONTAL) {
            scrollListener = (observableValue, number, number2) -> {
                if (iconList != null) {
                    iconList.setTranslateX(0 - number2.doubleValue());

                }
            };
        } else {
            scrollListener = (observableValue, number, number2) -> {
                if (iconList != null) {
                    iconList.setTranslateY(0 - number2.doubleValue());
                }
            };
            scroll.addListener((observableValue, number, number2) -> {
                if (iconList != null) {
                    iconList.setTranslateY(0 - number2.doubleValue());
                }
            });
        }

        scroll.addListener(scrollListener);

        noScroll.set(false);
        // Build tray area

        // add icon list
        getChildren().add(iconList); // add icon list

        addEventHandler(MouseEvent.MOUSE_PRESSED, filterMousePressed);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, filterMouseDragged);
        addEventHandler(MouseEvent.MOUSE_RELEASED, filterMouseReleased);

        setClip(iconSwipeListInfo.getScrollViewPort());
    }

    protected void updateScrollAxis(double axisPoint) {
        if (noScroll.get()) {
            return;
        }
        scroll.set(restrainScrolling(scrollOrigin.doubleValue() - (axisPoint - clickPos.doubleValue())));
    }

    protected double restrainScrolling(double yOrXAxisCoord) {

        double dist = 0;
        if (swipeAxis == SwipeAxis.HORIZONTAL) {
            dist = getLayoutBounds().getWidth() - iconSwipeListInfo.getWidth() + 10;
        } else {
            dist = getLayoutBounds().getHeight() - iconSwipeListInfo.getHeight() + 10;
        }

        if (yOrXAxisCoord < 0) // upper coordinate bound
        {
            return 0;
        } else if (yOrXAxisCoord > dist) {
            return dist + 10;
        } else {
            return yOrXAxisCoord;
        }
    }

    protected void animStart(double delta) {
        if (dragDelta.doubleValue() > 5 && dragDelta.doubleValue() < -5) {
            return;
        }
        if (noScroll.get()) {
            return;
        }
        DoubleProperty endScrollValue = new SimpleDoubleProperty();
        endScrollValue.set(restrainScrolling(scroll.doubleValue() + delta * 15));
        if (dragTimeLine != null) {
            dragTimeLine.stop();
        }
        dragTimeLine = new Timeline(
                new KeyFrame(Duration.millis(800),
                        new KeyValue(scroll, endScrollValue.get(), Interpolator.EASE_OUT)));
        dragTimeLine.playFromStart();
    }

    protected void animStop() {
        if (dragTimeLine != null) {
            dragTimeLine.stop();
        }
    }
}
