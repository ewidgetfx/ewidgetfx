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
package org.ewidgetfx.widget.conferencetour;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;

/**
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public class ConferenceTourWidget extends DefaultWidget {

    ConferenceTour conferenceTour = new ConferenceTour();
    TextIconOverlay tio = new TextIconOverlay();

    @Override
    public WidgetIcon buildWidgetIcon() {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M22.727,18.242L4.792,27.208l8.966-8.966l-4.483-4.484l17.933-8.966l-8.966,8.966L22.727,18.242z");
        svgPath.autosize();
        svgPath.setFill(Color.WHITE);
        WidgetIcon widgetIcon = new WidgetIcon(svgPath, tio);
        widgetIcon.setBadgePosition(WidgetIcon.BADGE_POS.SE);

        return widgetIcon;
    }

    @Override
    public void init() {
        setPrefWidth(conferenceTour.getWidth());
        setPrefHeight(conferenceTour.getHeight());
        getChildren().add(conferenceTour);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {

        ConferenceTourWidget widget = new ConferenceTourWidget();
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
