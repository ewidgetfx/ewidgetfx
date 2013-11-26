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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * ArcPiece is an object representing the state (model) of an arc shape that will be drawn on the Graphics Context.
 * During an animation loop values in the model will often be updated. The update() method has the ability to calculate
 * elapsed time to allow the arc to later be animated based on frames per second. The draw() method simply renders the
 * arc shape onto the Graphics Context (surface).
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class ArcPiece {

    public double x;
    public double y;
    public double w;
    public double h;
    public double startAngle;
    public double arcExtent;
    public double strokeWidth = 2;
    public double pixelsToMove = 2;
    public Color strokeColor;
    public boolean clockwise = false;

    long startTime = 0;
    public long displayTimePerFrameMillis = 60;
    private long displayTimePerFrameNano = 60 * 1000000;

    public void update(long now) {
        if (startTime == 0) {
            startTime = now;
            displayTimePerFrameNano = displayTimePerFrameMillis * 1000000;
        }

        long elapsed = now - startTime;
        if (elapsed > displayTimePerFrameNano) {
            if (!clockwise) {
                startAngle = startAngle + pixelsToMove;
                if (startAngle > 360) {
                    startAngle = 0;
                }
            } else {
                startAngle = startAngle - pixelsToMove;
                if (startAngle < -360) {
                    startAngle = 0;
                }
            }
            startTime = 0;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(strokeColor);
        gc.setLineWidth(strokeWidth);
        gc.strokeArc(x,
                y,
                w,
                h,
                startAngle,
                arcExtent,
                ArcType.OPEN);
    }
}
