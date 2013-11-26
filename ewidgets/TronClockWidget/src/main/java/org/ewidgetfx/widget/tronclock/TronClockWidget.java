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
package org.ewidgetfx.widget.tronclock;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.ewidgetfx.widget.tronclock.ArcClock.BLUE5;
import static org.ewidgetfx.widget.tronclock.ArcClock.RED2;

/**
 * The demo will make use of JavaFX's Canvas API. This class creates three clocks which are animated using an
 * AnimationTimer class.
 *
 * This clock is using Widget API for app containers to manage widget launching life cycle.
 *
 * Inspired by http://burnwell88.deviantart.com/art/Clock-136761577 and http://rainmeter.net/cms/
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class TronClockWidget extends DefaultWidget {

    private AnimationTimer timer;

    TextIconOverlay tio = new TextIconOverlay();
    Thread backgroundThread;
    Runnable worker;
    Task task;

    public TronClockWidget() {
    }

    @Override
    public WidgetIcon buildWidgetIcon() {
        // create an icon
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M15.5,2.374C8.251,2.375,2.376,8.251,2.374,15.5C2.376,22.748,8.251,28.623,15.5,28.627c7.249-0.004,13.124-5.879,13.125-13.127C28.624,8.251,22.749,2.375,15.5,2.374zM15.5,25.623C9.909,25.615,5.385,21.09,5.375,15.5C5.385,9.909,9.909,5.384,15.5,5.374c5.59,0.01,10.115,4.535,10.124,10.125C25.615,21.09,21.091,25.615,15.5,25.623zM8.625,15.5c-0.001-0.552-0.448-0.999-1.001-1c-0.553,0-1,0.448-1,1c0,0.553,0.449,1,1,1C8.176,16.5,8.624,16.053,8.625,15.5zM8.179,18.572c-0.478,0.277-0.642,0.889-0.365,1.367c0.275,0.479,0.889,0.641,1.365,0.365c0.479-0.275,0.643-0.887,0.367-1.367C9.27,18.461,8.658,18.297,8.179,18.572zM9.18,10.696c-0.479-0.276-1.09-0.112-1.366,0.366s-0.111,1.09,0.365,1.366c0.479,0.276,1.09,0.113,1.367-0.366C9.821,11.584,9.657,10.973,9.18,10.696zM22.822,12.428c0.478-0.275,0.643-0.888,0.366-1.366c-0.275-0.478-0.89-0.642-1.366-0.366c-0.479,0.278-0.642,0.89-0.366,1.367C21.732,12.54,22.344,12.705,22.822,12.428zM12.062,21.455c-0.478-0.275-1.089-0.111-1.366,0.367c-0.275,0.479-0.111,1.09,0.366,1.365c0.478,0.277,1.091,0.111,1.365-0.365C12.704,22.344,12.54,21.732,12.062,21.455zM12.062,9.545c0.479-0.276,0.642-0.888,0.366-1.366c-0.276-0.478-0.888-0.642-1.366-0.366s-0.642,0.888-0.366,1.366C10.973,9.658,11.584,9.822,12.062,9.545zM22.823,18.572c-0.48-0.275-1.092-0.111-1.367,0.365c-0.275,0.479-0.112,1.092,0.367,1.367c0.477,0.275,1.089,0.113,1.365-0.365C23.464,19.461,23.3,18.848,22.823,18.572zM19.938,7.813c-0.477-0.276-1.091-0.111-1.365,0.366c-0.275,0.48-0.111,1.091,0.366,1.367s1.089,0.112,1.366-0.366C20.581,8.702,20.418,8.089,19.938,7.813zM23.378,14.5c-0.554,0.002-1.001,0.45-1.001,1c0.001,0.552,0.448,1,1.001,1c0.551,0,1-0.447,1-1C24.378,14.949,23.929,14.5,23.378,14.5zM15.501,6.624c-0.552,0-1,0.448-1,1l-0.466,7.343l-3.004,1.96c-0.478,0.277-0.642,0.889-0.365,1.365c0.275,0.479,0.889,0.643,1.365,0.367l3.305-1.676C15.39,16.99,15.444,17,15.501,17c0.828,0,1.5-0.671,1.5-1.5l-0.5-7.876C16.501,7.072,16.053,6.624,15.501,6.624zM15.501,22.377c-0.552,0-1,0.447-1,1s0.448,1,1,1s1-0.447,1-1S16.053,22.377,15.501,22.377zM18.939,21.455c-0.479,0.277-0.643,0.889-0.366,1.367c0.275,0.477,0.888,0.643,1.366,0.365c0.478-0.275,0.642-0.889,0.366-1.365C20.028,21.344,19.417,21.18,18.939,21.455z");
        svgPath.autosize();
        svgPath.setFill(Color.WHITE);

        // create a widget icon with text icon overlay
        tio.setFillColor(Color.rgb(0, 0, 0, .50));
        WidgetIcon widgetIcon = new WidgetIcon(svgPath, tio);
        widgetIcon.setBadgePosition(WidgetIcon.BADGE_POS.S);
        widgetIcon.showBadgeIndicator();
        return widgetIcon;
    }

    @Override
    public void startBackground() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

        task = new Task() {

            @Override
            protected Object call() throws Exception {
                while (true) {
                    // Note: This is not on the GUI thread.
                    // simulate hard work
                    final Date data = new Date();
                    // also update message for progress.
                    updateMessage(dateFormat.format(new Date()));

                    Thread.sleep(5000);
                }
            }
        };

        //listen for the updateMessage above (inside while loop)
        task.messageProperty().addListener((ChangeListener<String>) (observableValue, oldValue, newValue) -> {
            getWidgetIcon().showBadgeIndicator();
            tio.setText(newValue);
            getWidgetIcon().refresh();
        });

//tio.setText(dateFormat.format(new Date()));
//        tio.setText(dateFormat.format(data));
//        getWidgetIcon().refresh();
        worker = new Runnable() {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

            @Override
            public void run() {
                tio.setText(dateFormat.format(new Date()));
                while (true) {
                    try {
                        // Note: This is not on the GUI thread.
                        // simulate hard work
                        Thread.sleep(5000);
                        final Date data = new Date();

                        // Below is run on the GUI thread.
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tio.setText(dateFormat.format(data));
                                getWidgetIcon().refresh();
                            }
                        });
                    } catch (InterruptedException e) {
                        // called by the app container to stop worker threads and close jvm.
                        System.out.println("Get me out of here. Indicator will stop.");
                        break;
                    }
                }
            }
        };
//

        // this will run even if the widget window is closed.
        backgroundThread = new Thread(worker);
        backgroundThread.start();
    }

    @Override
    public void init() {

        setPrefWidth(210);
        setPrefHeight(210);
        System.out.println("decoration: " + getDecoration());

        // black rectangular translucent background
        Rectangle clockBackground = new Rectangle();
        clockBackground.setArcWidth(20);
        clockBackground.setArcHeight(20);
        clockBackground.setHeight(210);
        clockBackground.setWidth(210);
        clockBackground.setFill(Color.rgb(0, 0, 0, .70));
        getChildren().add(clockBackground);

        // create a canvas node
        Canvas canvas = new Canvas();
        canvas.setWidth(getPrefWidth());
        canvas.setHeight(getPrefHeight());

        final GraphicsContext gc = canvas.getGraphicsContext2D();

        // create blue clock
        final ArcClock clock = new ArcClock(20, RED2, BLUE5, 200);
        // create an animation (update & render loop)
        long startTime = System.nanoTime();
        timer = new AnimationTimer() {

            long elapsedTime = 0;

            @Override
            public void handle(long now) {
                // update clocks
                clock.update(now);

                // clear screen
                gc.clearRect(0, 0, getWidth(), getHeight());

                // draw blue clock
                clock.draw(gc);
            }
        };
        // add the single node onto the scene graph
        getChildren().add(canvas);

    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void pause() {
        timer.stop();
    }

    @Override
    public void resume() {
        timer.start();
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void stopBackground() {
        task.cancel();
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }
    }

    public static void main(String[] args) {
        TronClockWidget widget = new TronClockWidget();
        widget.setDecoration(DECORATION.STAGED_UNDECORATED);

        LaunchInfo info = new LaunchInfo();
        info.setLaunchType(LaunchInfo.LaunchType.WIDGET);
        info.setExecutionLine(widget.getClass().getName());
        info.setWidget(widget);
        widget.setLaunchInfo(info);
        Demo.newInstance()
                .setWidget(widget)
                .setSceneColor(new Color(0, 0, 0, .70))
                .run(args);

    }

}
