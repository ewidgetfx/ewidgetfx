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
package org.thehecklers.ticker;

import javafx.animation.AnimationTimer;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;
import org.thehecklers.feed.Feed;
import org.thehecklers.feed.TwitterFeed;

/**
 *
 * @author Mark Heckler <mark.heckler@gmail.com>
 * Twitter: @MkHeck
 *
 *
 */
public class Ticker extends DefaultWidget {

    // The icon overlay or badge indicator
    TextIconOverlay tio = new TextIconOverlay();
    Thread backgroundThread;
//    Runnable worker;

    private boolean border = true;
    private double xCoord;
    private double yCoord;
    private String message;
    private Feed msgFeed;
    private Color color = Color.YELLOW;
    private Dimension messageBounds;
    private AnimationTimer timer;

    public Ticker() {
    }

    @Override
    public WidgetIcon buildWidgetIcon() {
        // create an icon
        SVGPath svgPath = new SVGPath();
        // Twitter icon
        svgPath.setContent("M26.492,9.493c-0.771,0.343-1.602,0.574-2.473,0.678c0.89-0.533,1.562-1.376,1.893-2.382c-0.832,0.493-1.753,0.852-2.734,1.044c-0.785-0.837-1.902-1.359-3.142-1.359c-2.377,0-4.306,1.928-4.306,4.306c0,0.337,0.039,0.666,0.112,0.979c-3.578-0.18-6.75-1.894-8.874-4.499c-0.371,0.636-0.583,1.375-0.583,2.165c0,1.494,0.76,2.812,1.915,3.583c-0.706-0.022-1.37-0.216-1.95-0.538c0,0.018,0,0.036,0,0.053c0,2.086,1.484,3.829,3.454,4.222c-0.361,0.099-0.741,0.147-1.134,0.147c-0.278,0-0.547-0.023-0.81-0.076c0.548,1.711,2.138,2.955,4.022,2.99c-1.474,1.146-3.33,1.842-5.347,1.842c-0.348,0-0.69-0.021-1.027-0.062c1.905,1.225,4.168,1.938,6.6,1.938c7.919,0,12.248-6.562,12.248-12.25c0-0.187-0.002-0.372-0.01-0.557C25.186,11.115,25.915,10.356,26.492,9.493");
        // Facebook icon
        //svgPath.setContent("M25.566,2.433H6.433c-2.2,0-4,1.8-4,4v19.135c0,2.199,1.8,4,4,4h19.135c2.199,0,4-1.801,4-4V6.433C29.566,4.232,27.768,2.433,25.566,2.433zM25.309,16.916h-3.218v11.65h-4.819v-11.65h-2.409V12.9h2.409v-2.411c0-3.275,1.359-5.224,5.229-5.224h3.218v4.016h-2.011c-1.504,0-1.604,0.562-1.604,1.608L22.091,12.9h3.644L25.309,16.916z");
        svgPath.autosize();
        svgPath.setFill(Color.web("#00acee"));

        WidgetIcon widgetIcon = new WidgetIcon(svgPath, tio);
        widgetIcon.setBadgePosition(WidgetIcon.BADGE_POS.SE);
        return widgetIcon;
    }

    @Override
    public void startBackground() {
//        worker = new Runnable() {
//            @Override
//            public void run() {
//            }
//        };
    }

    @Override
    public void init() {
        System.out.println("decoration: " + getDecoration());
        // Determine and configure widget screen real estate
        final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Dimension widgetBounds = new Dimension(screenBounds.getWidth(), 25);

        setPrefSize(widgetBounds.getWidth(), widgetBounds.getHeight());
        setMaxSize(widgetBounds.getWidth(), widgetBounds.getHeight());

        Rectangle backRect = createBackground(widgetBounds.getWidth(), widgetBounds.getHeight());
        getChildren().add(backRect);

        Canvas canvas = new Canvas(widgetBounds.getWidth(), widgetBounds.getHeight());

        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.getDefault());
        gc.setFill(color);
        gc.setStroke(color);

        getChildren().add(canvas);

        // commented out please see start() method.
        getParentStage().setX(screenBounds.getMinX());
        getParentStage().setWidth(screenBounds.getWidth());
        getParentStage().setY(screenBounds.getHeight() - widgetBounds.getHeight());

        msgFeed = new TwitterFeed();
        //msgFeed = new FacebookFeed();
        formatNextMessage();

        final Bounds canvasBounds = canvas.getBoundsInLocal();
        // The x coordinate for ticker content begins at right edge of screen, minus border
        xCoord = canvasBounds.getMaxX() - 1;
        // Center the y coordinate on the canvas for the text
        yCoord = canvasBounds.getMaxY() - (canvasBounds.getMaxY() - canvasBounds.getMinY() - messageBounds.getHeight());

        // above is not known until nodes and text has been rendered.
        // since the ticker's height is already defined take the center (baseline) add two pixels to avoid the yellow border
        //yCoord = (widgetBounds.getHeight() / 2) + 2;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // update
                gc.clearRect(xCoord, canvasBounds.getMinY() + 2,
                        messageBounds.getWidth(), canvasBounds.getHeight() - 4);

                if (border) {
                    gc.strokeRect(canvasBounds.getMinX(), canvasBounds.getMinY(),
                            canvasBounds.getMaxX(), canvasBounds.getMaxY());
                }

                gc.fillText(message, xCoord -= 2, yCoord);
                if (xCoord < -messageBounds.getWidth()) {
                    xCoord = canvasBounds.getMaxX() - 1;
                    formatNextMessage();
                }
            }
        };

        timer.start();
    }

    @Override
    public void start() {
        //timer.start();
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
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts, e.g., in IDEs with limited FX support. NetBeans
     * ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Ticker widget = new Ticker();
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

    private void formatNextMessage() {
        // Retrieve next message from the live feed
        message = msgFeed.getMessage();

        // Format the message for scrolling movement. Inelegant, but functional.
        Text text = new Text(message);
        text.snapshot(null, null);
        messageBounds = new Dimension(text.getLayoutBounds().getWidth(), text.getLayoutBounds().getHeight());
    }

    private Rectangle createBackground(double w, double h) {
        Rectangle bg = new Rectangle();
        //bg.setArcWidth(20);
        //bg.setArcHeight(20);
        bg.setHeight(h);
        bg.setWidth(w);
        //bg.setFill(Color.rgb(0, 0, 0, .70));
        bg.setFill(Color.BLACK);
        return bg;
    }
}
