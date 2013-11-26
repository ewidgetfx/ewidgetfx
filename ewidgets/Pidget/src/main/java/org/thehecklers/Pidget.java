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
package org.thehecklers;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class Pidget extends DefaultWidget {

    private long curTime;
    private long startTime;
    private int curPic;
    // The icon overlay or badge indicator
    TextIconOverlay tio = new TextIconOverlay();
    Thread backgroundThread;
    Task task;
    private AnimationTimer timer;
    Dimension widgetBounds;
    ArrayList<String> pictureFileSpecs;

    @Override
    public WidgetIcon buildWidgetIcon() {
        SVGPath svgPath = new SVGPath();
        // Picture icon
        svgPath.setContent("M2.5,4.833v22.334h27V4.833H2.5zM25.25,25.25H6.75V6.75h18.5V25.25zM11.25,14c1.426,0,2.583-1.157,2.583-2.583c0-1.427-1.157-2.583-2.583-2.583c-1.427,0-2.583,1.157-2.583,2.583C8.667,12.843,9.823,14,11.25,14zM24.251,16.25l-4.917-4.917l-6.917,6.917L10.5,16.333l-2.752,2.752v5.165h16.503V16.25z");
        svgPath.autosize();
        svgPath.setFill(Color.WHITE);

        WidgetIcon widgetIcon = new WidgetIcon(svgPath, tio);
        widgetIcon.setBadgePosition(WidgetIcon.BADGE_POS.SE);

        return widgetIcon;
    }

    @Override
    public void startBackground() {
        // Nothing to see here, move along...  :-)
    }

    @Override
    public void init() {
        widgetBounds = new Dimension(640, 480);

        setPrefSize(widgetBounds.getWidth(), widgetBounds.getHeight());
        setMaxSize(widgetBounds.getWidth(), widgetBounds.getHeight());

        // Visible background for widget
        Rectangle bg = new Rectangle();
        bg.setHeight(widgetBounds.getHeight());
        bg.setWidth(widgetBounds.getWidth());
        bg.setStroke(Color.BLACK);
        bg.setFill(Color.rgb(255, 255, 255, .70));  // black, 70% opacity
        getChildren().add(bg);
    }

    @Override
    public void start() {
        ImageView imgView = new ImageView();
        getChildren().add(imgView);

        System.out.println("Opening directory...");
        String picDir = loadDirectory();
        File dir = new File(picDir);
        File[] files = dir.listFiles((File dir1, String name) -> name.endsWith(".jpg"));

        if (files.length == 0) {
            // Notify user of extreme lack of photographic material. :-)
            Label noFilesLabel = new Label("No files in directory " + picDir
                    + ", please add 'picdir=<path>' entry to Pidget.properties file.");
            noFilesLabel.setPrefSize(widgetBounds.getWidth(), widgetBounds.getHeight());
            noFilesLabel.setMaxSize(widgetBounds.getWidth(), widgetBounds.getHeight());
            noFilesLabel.setAlignment(Pos.CENTER);
            getChildren().add(noFilesLabel);
        } else {
            ArrayList<Image> images = new ArrayList<>();

            for (File picFile : files) {
                System.out.println(picFile);
                try {
                    images.add(new Image(new FileInputStream(picFile)));
                    System.out.println("Adding picture " + picFile.toString() + " to the picture carousel...");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // Start the clock & counter for picture rotation
            startTime = System.currentTimeMillis() - 10000;
            curPic = 0;

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if ((curTime = System.currentTimeMillis()) - startTime > 9999) {
                        System.out.println("Now displaying " + images.get(curPic).toString() + " in Pidget...");
                        imgView.setImage(images.get(curPic));
                        imgView.setFitHeight(widgetBounds.getHeight());
                        imgView.setPreserveRatio(true);
                        curPic = curPic == images.size() - 1 ? 0 : curPic + 1;
                        startTime = curTime;
                    }
                }
            };
            timer.start();
        }
    }

    @Override
    public void pause() {
        // Nothing to see here, move along...  :-)
    }

    @Override
    public void resume() {
        // Nothing to see here, move along...  :-)
    }

    @Override
    public void stop() {
        // Nothing to see here, move along...  :-)
    }

    @Override
    public void stopBackground() {
        task.cancel();
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
        }
    }

    public static void main(String[] args) {

        Pidget widget = new Pidget();
        widget.setDecoration(DECORATION.STAGED_UNDECORATED);

        LaunchInfo info = new LaunchInfo();
        info.setLaunchType(LaunchInfo.LaunchType.WIDGET);
        info.setExecutionLine(widget.getClass().getName());
        info.setWidget(widget);

        Demo.newInstance()
                .setWidget(widget)
                .setSceneColor(new Color(0, 0, 0, .70))
                .run(args);

    }

    private String loadDirectory() {
        Properties applicationProps = new Properties();
        FileInputStream in = null;
        File propFile = new File("Pidget.properties");

        if (!propFile.exists()) {
            // If it doesn't exist, create it.
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in = new FileInputStream(propFile);
            applicationProps.load(in);
            in.close();

            if (applicationProps.containsKey("picdir")) {
                return applicationProps.getProperty("picdir", ".");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ".";
    }
}
