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
package org.ewidgetfx.widget.helloworld;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;

import java.util.Random;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class HelloWorldWidget extends DefaultWidget {

    // The icon overlay or badge indicator
    TextIconOverlay tio = new TextIconOverlay();
    Thread backgroundThread;

    Task task;

    public HelloWorldWidget() {
    }

    @Override
    public WidgetIcon buildWidgetIcon() {
        // create an icon
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M16,1.466C7.973,1.466,1.466,7.973,1.466,16c0,8.027,6.507,14.534,14.534,14.534c8.027,0,14.534-6.507,14.534-14.534C30.534,7.973,24.027,1.466,16,1.466zM16,29.534C8.539,29.534,2.466,23.462,2.466,16C2.466,8.539,8.539,2.466,16,2.466c7.462,0,13.535,6.072,13.535,13.533C29.534,23.462,23.462,29.534,16,29.534zM11.104,14c0.932,0,1.688-1.483,1.688-3.312s-0.755-3.312-1.688-3.312s-1.688,1.483-1.688,3.312S10.172,14,11.104,14zM20.729,14c0.934,0,1.688-1.483,1.688-3.312s-0.756-3.312-1.688-3.312c-0.932,0-1.688,1.483-1.688,3.312S19.798,14,20.729,14zM8.143,21.189C10.458,24.243,13.148,26,16.021,26c2.969,0,5.745-1.868,8.11-5.109c-2.515,1.754-5.292,2.734-8.215,2.734C13.164,23.625,10.54,22.756,8.143,21.189z");
        svgPath.autosize();
        svgPath.setFill(Color.WHITE);
        WidgetIcon widgetIcon = new WidgetIcon(svgPath, tio);
        widgetIcon.setBadgePosition(WidgetIcon.BADGE_POS.SE);

        return widgetIcon;
    }

    @Override
    public void startBackground() {

        // A Task to be used in JFX UIs.
        // Hypothetical background worker task.
        task = new Task() {

            @Override
            protected Object call() throws Exception {
                while (true) {
                    // do lot's of work
                    Thread.sleep(1000);
                    Random rnd = new Random();
                    int n = rnd.nextInt(99);
                    // also update message for progress.
                    updateMessage(n + "");
                }
            }
        };

        task.messageProperty().addListener((ChangeListener<String>) (observableValue, oldValue, newValue) -> {
            int num = Integer.parseInt(newValue);
            if (num == 0) {
                getWidgetIcon().hideBadgeIndicator();
            } else {
                getWidgetIcon().showBadgeIndicator();
            }
            tio.setText(newValue);
            getWidgetIcon().refresh();
        });
        backgroundThread = new Thread(task);
        backgroundThread.start();
    }

    @Override
    public void init() {
        setPrefWidth(210);
        setPrefHeight(210);

        // visible background for widget
        Rectangle bg = new Rectangle();
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setHeight(210);
        bg.setWidth(210);
        bg.setStroke(Color.BLACK);
        bg.setFill(Color.rgb(255, 255, 255, .70));
        getChildren().add(bg);

        // hello world
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(4.0f);
        innerShadow.setOffsetY(4.0f);

        Text t = new Text();
        t.setEffect(innerShadow);
        t.setX(25);
        t.setY(110);
        t.setText("Hello World");
        t.setFill(Color.GREEN);
        t.setFont(Font.font(null, FontWeight.BOLD, 30));
        getChildren().add(t);

        Button button = new Button();
        button.setText("Close widget");
        button.setLayoutX(50);
        button.setLayoutY(50);
        button.setOnAction((ActionEvent actionEvent) -> {
            System.out.println("close window ");
            getParentStage().close();
        });
        getChildren().add(button);

    }

    @Override
    public void start() {
        // nothing to do
    }

    @Override
    public void stop() {
        // nothing to do
    }

    @Override
    public void stopBackground() {
        task.cancel();
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }
    }

    public static void main(String[] args) {

        HelloWorldWidget widget = new HelloWorldWidget();
        widget.setDecoration(DECORATION.STAGED_CONFIG_CLOSE);

        LaunchInfo info = new LaunchInfo();
        info.setLaunchType(LaunchInfo.LaunchType.WIDGET);
        info.setExecutionLine(widget.getClass().getName());
        info.setWidget(widget);

        Demo.newInstance()
                .setWidget(widget)
                .setSceneColor(new Color(0, 0, 0, .70))
                .run(args);

    }

}
