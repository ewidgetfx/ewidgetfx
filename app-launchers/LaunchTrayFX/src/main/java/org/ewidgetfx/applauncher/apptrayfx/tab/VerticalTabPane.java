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
package org.ewidgetfx.applauncher.apptrayfx.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class VerticalTabPane extends StackPane {

    private static final Logger logger = Logger.getLogger(VerticalTabPane.class);

    public static enum Side {

        LEFT, RIGHT
    };

    private Side side = Side.LEFT;

    private int selectIndex = -1;
    private List<VerticalTab> tabList = new ArrayList<>();
    private ObservableList<VerticalTab> tabs = FXCollections.observableList(tabList);
    protected EventHandler<MouseEvent> filterMousePressed = null;

    public VerticalTabPane(Side side, double prefWidth, double prefHeight) {

        this.side = side;
        setId("VerticalTabPane");
        setStyle("-fx-background-color: transparent;");
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
        // when user mouse pressed the app icon.
        filterMousePressed = (mouseEvent) -> {

            //if (mouseEvent.getSource() instanceof VerticalTab) {
            Point2D pt = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            tabs.stream().filter((tab) -> (tab.getTabRectangle().contains(pt) && mouseEvent.getX() < VerticalTab.TAB_WIDTH)).filter((tab) -> (selectIndex != tab.getSelectIndex())).map((tab) -> {
                tabList.get(selectIndex).unselect();
                return tab;
            }).map((tab) -> {
                logger.info("Selected: " + tab.getName());
                return tab;
            }).map((tab) -> {
                selectIndex = tab.getSelectIndex();
                return tab;
            }).map((tab) -> {
                tab.select();
                return tab;
            }).map((tab) -> {
                tab.toFront();
                return tab;
            }).map((tab) -> {
                tab.requestLayout();
                return tab;
            }).forEach((_item) -> {
                requestLayout();
            });
        };
        addEventHandler(MouseEvent.MOUSE_PRESSED, filterMousePressed);

    }

    public void addTab(VerticalTab newTab) {
        tabs.stream().forEach((tab) -> {
            tab.unselect();
        });
        tabs.add(newTab);
        getChildren().add(newTab);
        newTab.select();
        selectIndex = tabs.size() - 1;
    }

    public int getNumberOfTabs() {
        return tabs.size();
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public ObservableList<VerticalTab> getTabs() {
        return tabs;
    }

    public void selectTab(int selectIndex) {
        getTabs().get(getSelectIndex()).unselect();
        VerticalTab tab = getTabs().get(selectIndex);
        tab.select();
        tab.toFront();
        this.selectIndex = selectIndex;
    }

}
