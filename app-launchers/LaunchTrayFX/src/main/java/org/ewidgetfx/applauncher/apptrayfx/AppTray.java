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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ewidgetfx.core.Widget;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.WidgetFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class AppTray extends Pane {
    
    private static final Logger logger = Logger.getLogger(AppTray.class);
    
    Stage primaryStage;
    ObservableList<WidgetIcon> widgetIcons = FXCollections.observableList(new ArrayList<WidgetIcon>());
    String[][] icons;
    
    public AppTray(Stage primaryStage, String[][] icons) {
        this.primaryStage = primaryStage;
        this.icons = icons;
        
    }
    
    public AppTray(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public ObservableList<WidgetIcon> getWidgetIcons() {
        return widgetIcons;
    }
    
    public void setWidgetIcons(ObservableList<WidgetIcon> widgetIcons) {
        this.widgetIcons = widgetIcons;
    }
    
    protected ObservableList<WidgetIcon> buildIcons2(Stage parent, Dimension2D iconSize, final Text appNameText) {

        // read in all of the jab files.
        // search directory for jab files
        File widgetsDir = new File(new File(".").getAbsoluteFile().getParentFile() + File.separator + "jabs");
        
        WidgetFactory.loadWidgets(widgetsDir, ".*\\.jar$", parent, this.getClass().getClassLoader());
        
        Collection<Widget> widgetCollection = WidgetFactory.retrieveAll();
        widgetCollection.forEach(widget -> {
            WidgetIcon widgetIcon = widget.getWidgetIcon();
            sizeWidgetIcon(widget, iconSize, appNameText);
            getWidgetIcons().add(widgetIcon);
        });
        if (icons == null) {
            return getWidgetIcons();
        }

        //*******************************
        //*
        //* Below is for demos only
        for (String[] icon : icons) {
            
            Class clazz = null;
            Widget widget = null;
            if (icon[1] != null && icon[1].trim().length() > 0) {
                try {
                    clazz = Class.forName(icon[1]);
                    widget = (Widget) clazz.newInstance();
                    
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    logger.error("Exception in buildIcons2 when initializing widget", e);
                }
            }
            Node tile = null;
            WidgetIcon widgetIcon = null;
            SVGPath svgPath = null;
            
            if (widget != null) {
                widgetIcon = widget.getWidgetIcon();
            } else {
                svgPath = new SVGPath();
                svgPath.setContent(icon[2]);
                svgPath.autosize();
                svgPath.setFill(Color.WHITE);
                
                widgetIcon = new WidgetIcon(svgPath);
            }

//            final Widget widget2 = widget;
            widgetIcon.setOnMouseEntered(me -> {
                appNameText.setText(icon[0]);
            });
            
            widgetIcon.setOnMouseExited(me -> {
                appNameText.setText("");
            });

//            if (widget != null) {
//                widgetIcon.setOnMouseClicked( me -> {
//                    Widget w = null;
//                    if (icon[1] != null && icon[1].trim().length() > 0) {
//                        try {
//                            Class clazz2 = Class.forName(icon[1]);
//                            w = (Widget) clazz2.newInstance();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    w.start();
//                    new WidgetStage(parent, w).show();
//                });
//            }
            widgetIcon.setPrefWidth(iconSize.getWidth());
            widgetIcon.setPrefHeight(iconSize.getHeight());
            widgetIcon.setAlignment(Pos.CENTER);
            getWidgetIcons().add(widgetIcon);
        }
        
        return getWidgetIcons();
    }
    
    public void sizeWidgetIcon(Widget widget, Dimension2D iconSize, Text displayText) {
        WidgetIcon widgetIcon = widget.getWidgetIcon();
        widgetIcon.setOnMouseEntered(me -> {
            displayText.setText(widget.getName());
        });
        
        widgetIcon.setOnMouseExited(me -> {
            displayText.setText("");
        });

        // IMPORTANT preferred icon dimensions allows badge icons to display.
        // when layout occurs the badge indicator will be positioned.
        widgetIcon.setMaxSize(iconSize.getWidth(), iconSize.getHeight());
        widgetIcon.setMaxHeight(iconSize.getHeight());
        widgetIcon.setMinHeight(iconSize.getHeight());
        widgetIcon.setPrefWidth(iconSize.getWidth());
        widgetIcon.setPrefHeight(iconSize.getHeight());
        widgetIcon.setAlignment(Pos.CENTER);
    }
}
