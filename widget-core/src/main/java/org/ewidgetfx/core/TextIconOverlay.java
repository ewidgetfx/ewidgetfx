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

import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Text icon overlay is a way to allow applications to notify the user during background processes showing text on top
 * of the application's icon.
 *
 * Examples are: number of e-mails, temperature, etc.
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class TextIconOverlay extends Group {

    final Text text = new Text();
    final Rectangle badge = new Rectangle();

    public TextIconOverlay() {

        text.setFont(Font.font(12));
        text.setStrokeWidth(0);
        text.setFill(Color.WHITE);

        badge.setArcWidth(10);
        badge.setArcHeight(10);

        text.boundsInLocalProperty().addListener((ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds bounds2) -> {
            badge.setWidth(bounds2.getWidth() + 6);
            badge.setHeight(bounds2.getHeight() + 6);
        });

        badge.setFill(Color.RED);
        badge.setStroke(Color.WHITE);
        badge.setStrokeWidth(2);

        StackPane badgeSurface = new StackPane();
        badgeSurface.setAlignment(Pos.CENTER);
        badgeSurface.getChildren().addAll(badge, text);

        getChildren().add(badgeSurface);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setText(Number text) {
        this.text.setText(String.valueOf(text));
    }

    public void setTextStyle(String style) {
        text.setStyle(style);
    }

    public void setFillColor(Color fill) {
        badge.setFill(fill);
    }

    public void setStrokeWidth(double width) {
        badge.setStrokeWidth(width);
    }
}
