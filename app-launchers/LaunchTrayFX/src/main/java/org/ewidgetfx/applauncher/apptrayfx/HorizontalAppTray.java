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

import javafx.geometry.Dimension2D;
import javafx.scene.layout.HBox;
import org.ewidgetfx.applauncher.apptrayfx.iconswipelist.IconSwipeListInfo;
import org.ewidgetfx.applauncher.apptrayfx.iconswipelist.IconSwipeListPane;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.util.DragStagePane;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class HorizontalAppTray extends AppTray {

    public HorizontalAppTray(Stage primaryStage, String[][] icons) {
        super(primaryStage, icons);
        init();
    }

    public void init() {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Text appNameText = TextBuilder.create()
                .x(100)
                .y(150)
                .fill(Color.YELLOW)
                .effect(DropShadowBuilder.create()
                        .offsetX(2.0)
                        .offsetY(2.0)
                        .color(Color.rgb(50, 50, 50, .588))
                        .build()
                )
                .font(Font.font("Serif", 20))
                .build();

        List<WidgetIcon> nodes = buildIcons2(primaryStage, new Dimension2D(50, 50), appNameText);
        // Horizontal icon list
        HBox hBox = new HBox();
        hBox.getChildren().addAll(nodes);
        hBox.setLayoutX(10);
        hBox.setLayoutY(5);

        IconSwipeListInfo info = new IconSwipeListInfo();
        // size of the surface of the icon swipe list.
        info.widthProperty().set(300);
        info.heightProperty().set(50);

        // create a view port for the swipe area.
        info.getScrollViewPort().xProperty().set(10);
        info.getScrollViewPort().yProperty().set(10);
        info.getScrollViewPort()
                .widthProperty().bind(info.widthProperty()
                        .subtract(info.getScrollViewPort()
                                .xProperty()
                                .multiply(2)));

        info.getScrollViewPort().heightProperty().bind(info.heightProperty());

        IconSwipeListPane appTray = new IconSwipeListPane(info, hBox, IconSwipeListPane.SwipeAxis.HORIZONTAL);

        appTray.translateYProperty().bind(primaryStage.heightProperty().subtract(info.getHeight()));

        DragStagePane dragStagePane = new DragStagePane(primaryStage);
        dragStagePane.setLayoutX(10);
        dragStagePane.setLayoutY(10);

        Rectangle whiteRectangle = new Rectangle();
        whiteRectangle.setFill(Color.rgb(255, 255, 255, .30));
        whiteRectangle.widthProperty().bind(info.widthProperty().subtract(2 * info.getScrollViewPort().getX()));
        whiteRectangle.heightProperty().bind(primaryStage.heightProperty().subtract(info.getHeight()));
        dragStagePane.getChildren()
                .addAll(whiteRectangle, appNameText);

        getChildren().addAll(dragStagePane, appTray);
    }

}
