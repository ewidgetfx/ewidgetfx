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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.ewidgetfx.core.Widget;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.core.WidgetProxy;

import java.lang.reflect.Proxy;
import java.util.Collection;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class Demo extends Application {

    private static Widget widget;
    private static Color sceneColor;
    private static Demo demo;

    public Demo() {
    }

    public static Demo newInstance() {
        if (demo == null) {
            demo = new Demo();
        }
        return demo;
    }

    public static Widget getWidget() {
        return widget;
    }

    public Demo setWidget(Widget widget) {
        Widget w = (Widget) Proxy.newProxyInstance(Widget.class.getClassLoader(),
                new Class<?>[]{Widget.class},
                new WidgetProxy(widget));
        Demo.widget = w;
        return demo;
    }

    public Color getSceneColor() {
        return sceneColor;
    }

    public Demo setSceneColor(Color sceneColor) {
        Demo.sceneColor = sceneColor;
        return demo;
    }

    public void run(String[] args) {
        main(args);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent widgetNode = null;
        if (widget == null) {
            Pane grp = new Pane();
            grp.getChildren().add(new Label("No widget was created"));
            widgetNode = grp;
        }

        //. They will likely use background threads to update
        // badge indicators.
        WidgetIcon widgetIcon = widget.buildWidgetIcon();
        widgetIcon.setPrefWidth(50);
        widgetIcon.setPrefHeight(50);
        widget.setWidgetIcon(widgetIcon);

        final Widget w = widget;

        // start any worker threads.
        w.startBackground();

        widgetIcon.setOnMouseClicked(WidgetFactory.createLaunchWidgetEventHandler(stage, w));
        Group root = new Group();
        root.getChildren().add(widgetIcon);

        final ComboBox badgePosComboBox = new ComboBox();
        badgePosComboBox.getItems().addAll(
                WidgetIcon.BADGE_POS.NW,
                WidgetIcon.BADGE_POS.N,
                WidgetIcon.BADGE_POS.NE,
                WidgetIcon.BADGE_POS.E,
                WidgetIcon.BADGE_POS.SE,
                WidgetIcon.BADGE_POS.S,
                WidgetIcon.BADGE_POS.SW,
                WidgetIcon.BADGE_POS.W,
                WidgetIcon.BADGE_POS.CENTER
        );
        badgePosComboBox.setLayoutY(60);
        badgePosComboBox.setLayoutX(10);
        root.getChildren().add(badgePosComboBox);
        badgePosComboBox.valueProperty().addListener(new ChangeListener<WidgetIcon.BADGE_POS>() {
            @Override
            public void changed(ObservableValue ov, WidgetIcon.BADGE_POS oldVal, WidgetIcon.BADGE_POS newVal) {
                widgetIcon.setBadgePosition(newVal);
            }
        });

        Scene scene = new Scene(root, 100, 100, getSceneColor());
        stage.setScene(scene);
        stage.setOnCloseRequest((windowEvent) -> {
            Collection<Widget> widgets = WidgetFactory.retrieveAll();
            widgets.stream().map((widget1) -> {
                widget1.pause();
                return widget1;
            }).map((widget1) -> {
                widget1.stop();
                return widget1;
            }).forEach((widget1) -> {
                widget1.stopBackground();
            });
            Platform.exit();
            System.exit(0);

        });
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (widget != null) {
            widget.stop();
        }
    }
}
