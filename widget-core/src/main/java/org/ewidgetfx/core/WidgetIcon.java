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
package org.ewidgetfx.core;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Widget or application containers can have icons to assist in the launching of widgets. WidgetIcon objects can also
 * have 'badge indicators' or icon overlays for icons.
 *
 * @param <B>
 * @see <code>TextIconOverlay</code>.
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetIcon<B extends Node> extends HBox {

    Group iconSurface = new Group();
    private Node appIconNode;
    private B badgeIndicator;

    public static enum BADGE_POS {

        NW, N, NE, E, SE, S, SW, W, CENTER, ABSOLUTE
    };
    private BADGE_POS badgePos = BADGE_POS.NE;

    protected EventHandler<MouseEvent> mouseEntered = e -> ((WidgetIcon) e.getSource()).setOpacity(1);
    protected EventHandler<MouseEvent> mouseExit = e -> ((WidgetIcon) e.getSource()).setOpacity(.80);
    protected EventHandler<MouseEvent> mousePressed = e -> System.out.println(e);
    protected EventHandler<MouseEvent> mouseClicked = e -> System.out.println(e);
    private boolean layoutChildren = false;
    private String widgetFilename;

    public WidgetIcon(Node node) {
        super();
        appIconNode = node;
        // TODO: build a default icon if there isn't a node
        init();
    }

    public WidgetIcon(Node node, B badgeIndicator) {
        super();
        appIconNode = node;
        this.badgeIndicator = badgeIndicator;
        // TODO: build a default icon if there isn't a node

        init();
    }

    protected void init() {

        StackPane iconLayers = new StackPane();
        iconLayers.setOpacity(.80);

        // This rectangle makes the icon background dimensions for StackPane width and height.
        Rectangle rect = new Rectangle();
        // When live the rect will respond to the later value of the pref width & height
        rect.widthProperty().bind(prefWidthProperty());
        rect.heightProperty().bind(prefHeightProperty());
        // hide rect will provide an invisible sizer.
        rect.setVisible(false);

        // center both the rect (invisible sizer) and app icon.
        StackPane.setAlignment(rect, Pos.CENTER);
        iconLayers.getChildren().add(rect);
        StackPane.setAlignment(appIconNode, Pos.CENTER);
        iconLayers.getChildren().add(appIconNode);

        //    build filters to intercept mouse events.
        // Scene graph Runtime RT-30659 (Not a bug..)
        // use the setPickOnBounds(true)
        setPickOnBounds(true);
        addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEntered);
        addEventFilter(MouseEvent.MOUSE_EXITED, mouseExit);
        addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
        addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClicked);
        iconLayers.setCacheHint(CacheHint.SPEED);
        appIconNode.setCacheHint(CacheHint.SPEED);

        iconSurface.getChildren().add(iconLayers);
        if (badgeIndicator != null) {
            hideBadgeIndicator();

            iconSurface.getChildren().add(badgeIndicator);
        }
        setAlignment(Pos.CENTER);
        getChildren().add(iconSurface);
    }

    public Node getAppIconNode() {
        return appIconNode;
    }

    public void setAppIconNode(Node appIconNode) {
        iconSurface.getChildren().remove(this.appIconNode);
        this.appIconNode = appIconNode;
        iconSurface.getChildren().add(0, appIconNode);
    }

    public B getBadgeIndicator() {
        return (B) badgeIndicator;
    }

    public void setBadgePosition(BADGE_POS position) {
        badgePos = position;
        layoutChildren = false;
        requestLayout();
    }

    public void refresh() {
        layoutChildren = false;
        requestLayout();
    }

    public BADGE_POS getBadgePosition() {
        return badgePos;
    }

    public void setBadgeIndicator(B badgeIndicator) {
        if (this.badgeIndicator != null) {
            iconSurface.getChildren().remove(this.badgeIndicator);
        }
        this.badgeIndicator = badgeIndicator;
        iconSurface.getChildren().add(badgeIndicator);

    }

    public void showBadgeIndicator() {
        this.badgeIndicator.setVisible(true);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (!layoutChildren) {
            updateBadgePosition();
        }
        layoutChildren = true;
    }

    private void updateBadgePosition() {
        if (badgeIndicator == null) {
            return;
        }
        double w = badgeIndicator.getBoundsInLocal().getWidth();
        double h = badgeIndicator.getBoundsInLocal().getHeight();
        double surfaceWidth = getPrefWidth();
        double surfaceHeight = getPrefHeight();
        BADGE_POS badge_pos = getBadgePosition();

        switch (badge_pos) {
            case N:
                badgeIndicator.setLayoutX((surfaceWidth - w) / 2);
                badgeIndicator.setLayoutY(0);
                break;
            case NE:
                badgeIndicator.setLayoutX(surfaceWidth - w);
                badgeIndicator.setLayoutY(0);
                break;
            case E:
                badgeIndicator.setLayoutX((surfaceWidth - w));
                badgeIndicator.setLayoutY((surfaceHeight - h) / 2);
                break;
            case SE:
                badgeIndicator.setLayoutX((surfaceWidth - w));
                badgeIndicator.setLayoutY((surfaceHeight - h));
                break;
            case S:
                badgeIndicator.setLayoutX((surfaceWidth - w) / 2);
                badgeIndicator.setLayoutY((surfaceHeight - h));
                break;
            case SW:
                badgeIndicator.setLayoutX(0);
                badgeIndicator.setLayoutY((surfaceHeight - h));
                break;
            case W:
                badgeIndicator.setLayoutX(0);
                badgeIndicator.setLayoutY((surfaceHeight - h) / 2);
                break;
            case CENTER:
                badgeIndicator.setLayoutX((surfaceWidth - w) / 2);
                badgeIndicator.setLayoutY((surfaceHeight - h) / 2);
                break;
            case NW:
                badgeIndicator.setLayoutX(0);
                badgeIndicator.setLayoutY(0);
                break;
            case ABSOLUTE:
            // upper left (0,0)
            default:
        }
    }

    public void hideBadgeIndicator() {
        this.badgeIndicator.setVisible(false);
    }

    public void removeBadgeIndicator() {
        iconSurface.getChildren().remove(badgeIndicator);
    }

    public String getWidgetFilename() {
        return widgetFilename;
    }

    public void setWidgetFilename(String widgetFilename) {
        this.widgetFilename = widgetFilename;
    }
}
