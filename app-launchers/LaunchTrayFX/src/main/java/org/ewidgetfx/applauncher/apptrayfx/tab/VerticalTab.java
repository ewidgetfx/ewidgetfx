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
package org.ewidgetfx.applauncher.apptrayfx.tab;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class VerticalTab extends Pane {

    private static final Logger logger = Logger.getLogger(VerticalTab.class);

    private String name;
    private final VerticalTabPane parent;
    private Pane content;
    private int selectIndex;
    public static int TAB_HEIGHT = 80;
    public static int TAB_WIDTH = 20;
    private final Shape tabRectangle;
    private final Shape tabContentShape;
    private final List<Node> contentChildren = new ArrayList<>();
    protected Group group = new Group();

    public VerticalTab(String name, int selectIndex, VerticalTabPane parent) {
        this.name = name;
        this.parent = parent;
        this.selectIndex = selectIndex;

        setId(name);

        // build tab
        Shape tabRect = RectangleBuilder.create()
                .x(0)
                .y(selectIndex * TAB_HEIGHT + (3 * (selectIndex + 1)))
                .arcWidth(6)
                .arcHeight(6)
                .width(TAB_WIDTH + 3) // allow overlap for union of tab and content rectangle
                .height(TAB_HEIGHT)
                //.fill(Color.rgb(0, 0, 0, .50))
                //.stroke(Color.WHITE)
                .build();

        Rectangle contentRect = RectangleBuilder.create()
                .x(TAB_WIDTH)
                .y(0)
                .arcWidth(10)
                .arcHeight(10)
                .width(parent.getPrefWidth() - TAB_WIDTH)
                .height(parent.getPrefHeight())
                .build();
        Shape tabShape = Shape.union(tabRect, contentRect);
        tabContentShape = tabShape;
        tabShape.setStroke(Color.WHITE);
        tabShape.setFill(Color.rgb(0, 0, 0, .70));

        group.getChildren().add(tabShape);

        // IMPORTANT now you add the tab region with white stroke after union operation
        // when using shape operations don't mess with colors until after.
        tabRect = Shape.subtract(tabRect, contentRect);

        tabRect.setFill(Color.rgb(0, 0, 0, .70));
        tabRect.setStroke(Color.WHITE);
        tabRectangle = tabRect;
        group.getChildren().add(tabRectangle);

        // build content region
        getChildren().add(group);
        group.setOnMousePressed((me) -> {
            logger.info("Tab pressed " + selectIndex);
        });

        // add the text of the tab last
        double topTabY = selectIndex * TAB_HEIGHT + (3 * (selectIndex + 1));
        double bottomTextY = topTabY + TAB_HEIGHT - (3 * (selectIndex + 1));
        Text text = new Text(TAB_WIDTH - 6,bottomTextY,  name);
        text.setFont(Font.font("SanSerif", 13));
        text.setStroke(Color.WHITE);
        text.setFill(Color.WHITE);
        
        text.getTransforms().add(Transform.rotate(-90, TAB_WIDTH - 6, bottomTextY));
        getChildren().add(text);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pane getContent() {
        return content;
    }

    public void setContent(Pane content) {
        if (this.content != null) {
            group.getChildren().remove(this.content);
            contentChildren.clear();
        }
        this.content = content;
        content.setLayoutX(TAB_WIDTH + 1);
        group.getChildren().add(content);
        contentChildren.addAll(content.getChildren());
        content.toFront();
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public Shape getTabRectangle() {
        return tabRectangle;
    }

    public Shape getTabContentShape() {
        return tabContentShape;
    }

    public void unselect() {
        content.getChildren().removeAll(contentChildren);
        // display tab rectangle
        getTabRectangle().setVisible(true);
        // set tab shape visible false
        tabContentShape.setVisible(false);
        content.setVisible(false);
        group.getChildren().removeAll(tabContentShape, content);
    }

    public void select() {
        if (!content.getChildren().containsAll(contentChildren)) {
            content.getChildren().addAll(contentChildren);
        }
        if (!group.getChildren().contains(tabContentShape) && !group.getChildren().contains(content)) {
            group.getChildren().addAll(tabContentShape, content);
        }

        // display tab rectangle
        getTabRectangle().setVisible(false);
        // set tab shape visible false
        tabContentShape.setVisible(true);
        content.setVisible(true);
        requestLayout();
    }
}
