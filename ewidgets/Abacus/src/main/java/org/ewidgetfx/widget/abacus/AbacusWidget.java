/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ewidgetfx.widget.abacus;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import org.ewidgetfx.core.DefaultWidget;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.TextIconOverlay;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.Demo;
import solution.Abacus_6_Styled;

/**
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 */
public class AbacusWidget extends DefaultWidget {

    Abacus_6_Styled abacus = new Abacus_6_Styled();
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
        setPrefWidth(abacus.getWidth());
        setPrefHeight(abacus.getHeight());

        setStyle("-fx-background-color: radial-gradient(center 25% 25%, radius 60%, reflect, red, black );");

        getChildren().add(abacus);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }

    public static void main(String[] args) {

        AbacusWidget widget = new AbacusWidget();
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
