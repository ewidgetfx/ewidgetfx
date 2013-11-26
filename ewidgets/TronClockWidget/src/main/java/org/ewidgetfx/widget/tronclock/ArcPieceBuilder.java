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

import javafx.scene.paint.Color;

/**
 * ArcPieceBuilder is conveniecy class for the user of the API to build ArcPiece instances using the builder pattern.
 *
 * Disclaimer: to make things simple I don't use JavaFX Properties and do not use getters/setters on the domain model.
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class ArcPieceBuilder {

    ArcPiece arcPiece = new ArcPiece();

    public static ArcPieceBuilder create() {
        return new ArcPieceBuilder();
    }

    public ArcPieceBuilder x(double x) {
        arcPiece.x = x;
        return this;
    }

    public ArcPieceBuilder y(double y) {
        arcPiece.y = y;
        return this;
    }

    public ArcPieceBuilder w(double w) {
        arcPiece.w = w;
        return this;
    }

    public ArcPieceBuilder h(double h) {
        arcPiece.h = h;
        return this;
    }

    public ArcPieceBuilder startAngle(double startAngle) {
        arcPiece.startAngle = startAngle;
        return this;
    }

    public ArcPieceBuilder arcExtent(double arcExtent) {
        arcPiece.arcExtent = arcExtent;
        return this;
    }

    public ArcPieceBuilder strokeWidth(double width) {
        arcPiece.strokeWidth = width;
        return this;
    }

    public ArcPieceBuilder strokeColor(Color c) {
        arcPiece.strokeColor = c;
        return this;
    }

    public ArcPieceBuilder clockwise() {
        arcPiece.clockwise = true;
        return this;
    }

    public ArcPieceBuilder counterClockwise() {
        arcPiece.clockwise = false;
        return this;
    }

    public ArcPieceBuilder displayTimePerFrameMillis(long millis) {
        arcPiece.displayTimePerFrameMillis = millis;
        return this;
    }

    public ArcPieceBuilder pixelsToMove(double numPixels) {
        arcPiece.pixelsToMove = numPixels;
        return this;
    }

    public ArcPiece build() {
        return arcPiece;
    }
}
