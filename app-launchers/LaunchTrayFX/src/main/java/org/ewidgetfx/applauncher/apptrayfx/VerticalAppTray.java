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
package org.ewidgetfx.applauncher.apptrayfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.ewidgetfx.applauncher.apptrayfx.iconswipelist.IconSwipeListInfo;
import org.ewidgetfx.applauncher.apptrayfx.iconswipelist.IconSwipeListPane;
import org.ewidgetfx.applauncher.apptrayfx.tab.VerticalTab;
import org.ewidgetfx.applauncher.apptrayfx.tab.VerticalTabPane;
import org.ewidgetfx.applauncher.apptrayfx.tileselector.TileSelector;
import org.ewidgetfx.core.Widget;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.DragStagePane;
import org.ewidgetfx.util.WidgetFactory;

import java.util.*;
import javafx.scene.effect.DropShadow;
import org.apache.log4j.Logger;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public final class VerticalAppTray extends AppTray {

    private static final Logger logger = Logger.getLogger(VerticalAppTray.class);

    private VBox vBox;
    Text appNameText;
    Dimension2D iconDim = new Dimension2D(50, 50);

    // Tab 2 workspaces. Maps the jar filename with the Widget.
    TileSelector tileSelector = new TileSelector(2, 2);
    Map<Integer, Set<Widget>> workspaceWidgetMap = new HashMap<>();
    Map<Widget, WidgetInfo> widgetWidgetInfoMap = new HashMap<>();

    public VerticalAppTray(Stage primaryStage, String[][] icons) {
        super(primaryStage, icons);
        init();
    }

    public void init() {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        // create 4 workspaces.
        workspaceWidgetMap.put(1, new HashSet<>());
        workspaceWidgetMap.put(2, new HashSet<>());
        workspaceWidgetMap.put(3, new HashSet<>());
        workspaceWidgetMap.put(4, new HashSet<>());

        // build tab control # of tabs
        VerticalTabPane tabPane = new VerticalTabPane(VerticalTabPane.Side.LEFT, 220, 300);
        // add draggable title

        // applications
        tabPane.addTab(createTab1(tabPane));

        // WorkSpaces
        tabPane.addTab(createTab2(tabPane));

        // Settings
        tabPane.addTab(createTab3(tabPane));

        tabPane.selectTab(0);
        // place the drag area (dragStagePane) and the application tray thingy.
        getChildren().addAll(tabPane);
    }

    public void addIconToTray(Widget widget) {
        sizeWidgetIcon(widget, iconDim, appNameText);
        getWidgetIconList().add(0, widget.getWidgetIcon());
    }

    private VerticalTab createTab1(VerticalTabPane tabPane) {

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(USE_PREF_SIZE);
        dropShadow.setOffsetY(USE_PREF_SIZE);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));

        appNameText = new Text(5, 140, "");
        appNameText.setFill(Color.YELLOW);
        appNameText.setEffect(dropShadow);
        appNameText.setFont(Font.font("Serif", 20));

        ObservableList<WidgetIcon> nodes = buildIcons2(primaryStage, iconDim, appNameText);

        // Vertical
        //IconListPaneVertical iconListPane = new IconListPaneVertical(new IconListVertical(nodes));
        IconSwipeListInfo info = new IconSwipeListInfo();
        // size of the surface of the icon swipe list.
        info.widthProperty().set(50);
        info.heightProperty().set(300);

        // create a view port for the swipe area.
        info.getScrollViewPort().xProperty().set(10);
        info.getScrollViewPort().yProperty().set(10);
        info.getScrollViewPort()
                .widthProperty()
                .bind(info.widthProperty());

        info.getScrollViewPort()
                .heightProperty().bind(info.heightProperty()
                        .subtract(info.getScrollViewPort()
                                .yProperty()
                                .multiply(2)));

        vBox = new VBox();
        vBox.setMaxWidth(50);
        vBox.setMinWidth(50);
        vBox.setMaxHeight(50);
        vBox.setMinWidth(50);
        vBox.setLayoutX(10);
        vBox.setLayoutY(5);

        vBox.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {

                if (change.next() && change.wasAdded()) {
                    change.getAddedSubList().forEach(o -> {
                        WidgetIcon icon = (WidgetIcon) o;
                        Widget w = WidgetFactory.lookup(icon.getWidgetFilename());
                        if (w == null) {
                            return;
                        }
                        logger.info("widget " + w);
                        // if a widget was closed remove them from the workspace.
                        w.getWidgetState().stopPropertyProperty().addListener((ChangeListener) (observableValue, aBoolean, aBoolean2) -> {

                            if ((Boolean) aBoolean2) {
                                workspaceWidgetMap.values().stream().forEach((workspace) -> {
                                    workspace.remove(w);
                                });
                            }

                        });

                        icon.addEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler) (mouseEvent) -> {
                            logger.info("setting workspace " + icon.getWidgetFilename());
                            // this check if a widget is already in a workspace.
                            boolean found = false;
                            for (Set<Widget> workspace : workspaceWidgetMap.values()) {
                                if (workspace.contains(w)) {
                                    found = true;
                                    break;
                                }
                            }
                            // add the launched widget onto the current workspace
                            if (!found) {
                                workspaceWidgetMap.get(tileSelector.getCellNumber()).add(WidgetFactory.lookup(icon.getWidgetFilename()));
                            }
                        });
                    });
                }
            }
        });
        vBox.getChildren().addAll(nodes);
        IconSwipeListPane iconListPane = new IconSwipeListPane(info, vBox, IconSwipeListPane.SwipeAxis.VERTICAL);

        DragStagePane dragStagePane = new DragStagePane(primaryStage);
        dragStagePane.setLayoutX(60);
        dragStagePane.setLayoutY(10);

        Rectangle whiteRectangle = new Rectangle(200 - 50 - 20,300 - 20,Color.rgb(255, 255, 255, .30));

        dragStagePane.getChildren().addAll(whiteRectangle, appNameText);

        VerticalTab tab1 = new VerticalTab("Apps", 0, tabPane);
        Pane tab1Content = new Pane();
        tab1Content.getChildren().addAll(dragStagePane, iconListPane);
        tab1.setContent(tab1Content);
        return tab1;
    }

    private VerticalTab createTab2(VerticalTabPane tabPane) {
        VerticalTab tab = new VerticalTab("Spaces", 1, tabPane);
        Pane tabContent = new Pane();

        tileSelector.layoutXProperty().bind(tabPane.widthProperty().subtract(VerticalTab.TAB_WIDTH)
                .subtract(tileSelector.widthProperty())
                .divide(2));
        tileSelector.layoutYProperty().bind(tabPane.heightProperty()
                .subtract(tileSelector.heightProperty())
                .divide(2));
        tabContent.getChildren().add(tileSelector);

        tileSelector.cellNumberProperty().set(1);

        tileSelector.cellNumberProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) -> {
            List<KeyFrame> moveWindowsOut = new ArrayList<>();
            List<KeyFrame> moveWindowsIn = new ArrayList<>();

            // move widget's out of view.
            workspaceWidgetMap.get(oldNumber.intValue()).forEach((Widget widget) -> {
                logger.info(widget.getName() + " in workspace " + oldNumber);
                // save current location
                Stage widgetStage = widget.getParentStage();
                logger.info("moving out code: stage: " + widgetStage.hashCode());
                double x = widgetStage.getX();
                double y = widgetStage.getY();
                WidgetInfo widgetInfo = widgetWidgetInfoMap.get(widget);

                // TODO: Clean this up!!! needs to be more efficient.
                if (widgetInfo == null) {
                    widgetInfo = new WidgetInfo();
                }
                if (widgetInfo.getCurrentXChangeListener() != null) {
                    widgetInfo.currentXProperty().removeListener(widgetInfo.getCurrentXChangeListener());
                    widgetInfo.currentYProperty().removeListener(widgetInfo.getCurrentYChangeListener());
                }
                widgetInfo.setCurrentXChangeListener((ChangeListener<Number>) (observableValue1, oldX, newX) -> {
                    widgetStage.setX(newX.doubleValue());
                });
                widgetInfo.setCurrentYChangeListener((ChangeListener<Number>) (observableValue1, oldY, newY) -> {
                    widgetStage.setY(newY.doubleValue());
                });
                widgetInfo.currentXProperty().addListener(widgetInfo.getCurrentXChangeListener());
                widgetInfo.currentYProperty().addListener(widgetInfo.getCurrentYChangeListener());

                widgetInfo.setSavedX(x);
                widgetInfo.setSavedY(y);
                widgetInfo.setCurrentX(x);
                widgetInfo.setCurrentY(y);
                // move from current to new y
                // determine go top or bottom
                double h = widgetStage.getHeight();
                double midPoint = Screen.getPrimary().getBounds().getHeight() / 2;
                double direction = ((y + h) - midPoint) - (midPoint - y);
                boolean top = direction <= 0;
                double moveYTo = (top) ? (-h) : Screen.getPrimary().getBounds().getHeight();
                KeyValue keyValue = new KeyValue(widgetInfo.currentYProperty(), moveYTo);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(200), keyValue);
                moveWindowsOut.add(keyFrame);
                widgetWidgetInfoMap.put(widget, widgetInfo);
            });
            // create animation to move widget's out of view.
            Timeline moveOutTimeline = new Timeline();
            moveOutTimeline.setOnFinished((ActionEvent actionEvent) -> {
                workspaceWidgetMap.get(oldNumber.intValue()).forEach((Widget widget) -> {
                    widget.pause();
                });
            });
            moveOutTimeline.getKeyFrames().addAll(moveWindowsOut);

            logger.info("workspace selected: " + newNumber);
            workspaceWidgetMap.get(newNumber.intValue()).forEach((widget) -> {
                logger.info(widget.getName() + " in workspace " + newNumber);

                WidgetInfo widgetInfo = widgetWidgetInfoMap.get(widget);
                KeyValue keyValue = new KeyValue(widgetInfo.currentYProperty(), widgetInfo.getSavedY());
                KeyFrame keyFrame = new KeyFrame(Duration.millis(200), keyValue);
                moveWindowsIn.add(keyFrame);
                widget.getParentStage().show();
                widget.resume();
                widget.getParentStage().toFront();
            });
            // create animation to move widget's out of view.
            Timeline moveInTimeline = new Timeline();
            moveInTimeline.getKeyFrames().addAll(moveWindowsIn);

            moveOutTimeline.playFromStart();
            moveInTimeline.playFromStart();
        });

        setStyle("-fx-background-color: transparent;");
        tab.setContent(tabContent);

        return tab;

    }

    private VerticalTab createTab3(VerticalTabPane tabPane) {
        VerticalTab tab = new VerticalTab("Settings", 2, tabPane);
        Pane tabContent = new Pane();

        //tabContent.getChildren().add();
        tab.setContent(tabContent);
        return tab;
    }

    public ObservableList<Node> getWidgetIconList() {
        return vBox.getChildren();
    }
}
