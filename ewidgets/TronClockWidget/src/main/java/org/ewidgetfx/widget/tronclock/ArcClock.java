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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class is a container class that maintains all of the parts that comprised of a clock. It is also responsible for
 * updating and drawing itself at each animation frame cycle. The ArcClock contains zero to many ArcPiece objects to be
 * drawn. The clock will also display the time of day.
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class ArcClock {

    public static Color BLUE4 = Color.rgb(195, 254, 250, 0.6);
    public static Color BLUE5 = Color.rgb(28, 231, 248, 0.7);

    public static Color BLUE1 = Color.rgb(126, 166, 212, 0.6);
    public static Color BLUE2 = Color.rgb(126, 166, 222, 0.5);
    public static Color BLUE3 = Color.rgb(130, 166, 230, 0.5);
    public static Color GREEN1 = Color.rgb(130, 230, 166, 0.5);
    public static Color RED1 = Color.rgb(230, 130, 166, 0.5);
    public static Color RED2 = Color.rgb(255, 30, 30, 0.7);

    public ArcPiece longPiece;
    public ArcPiece[] arcPieces;
    public int maxDiameter;
    public double radius;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddhhmmssa");

    public ArcClock(int numArcs, Color longPieceColor, Color manyPieceColor, int maxDiameter) {
        this.maxDiameter = maxDiameter;
        radius = maxDiameter / 2;
        longPiece = ArcPieceBuilder.create()
                .strokeColor(longPieceColor)
                .strokeWidth(5)
                .x(0)
                .y(0)
                .w(maxDiameter)
                .h(maxDiameter)
                .startAngle(45)
                .arcExtent(240)
                .displayTimePerFrameMillis(1000)
                .pixelsToMove(1)
                .build();

        arcPieces = createRandomArcs(numArcs, manyPieceColor, maxDiameter / 2);
    }

    public void update(long now) {
        longPiece.update(now);
        for (ArcPiece ap : arcPieces) {
            ap.update(now);
        }
    }

    public void draw(GraphicsContext gc) {
        longPiece.draw(gc);
        for (ArcPiece ap : arcPieces) {
            ap.draw(gc);
        }
        // draw hour
        gc.setFont(Font.font("Calibri", 40));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);

        String dateTimeStr = DATE_FORMAT.format(new Date());
        //yyyyMMddhhmmssa
        gc.fillText(dateTimeStr.substring(8, 10), radius, radius + 18);
        gc.setFont(Font.font("Calibri", 20));
        gc.fillText(dateTimeStr.substring(10, 12) + " " + dateTimeStr.substring(14), maxDiameter - 40, radius - 40);
        gc.fillText(dateTimeStr.substring(12, 14), maxDiameter - 40, maxDiameter - 40);

    }

    public static int randomIntRange(int min, int max) {
        Random rand = new Random();
        int range = max - min + 1;
        return rand.nextInt(range) + min;
    }

    public static ArcPiece[] createRandomArcs(int num, Color color, double radius) {
        final ArcPiece[] manyPieces = new ArcPiece[num];
        for (int i = 0; i < num; i++) {
            manyPieces[i] = randomArcPiece(color, radius);
        }
        return manyPieces;
    }

    public static ArcPiece randomArcPiece(Color color, double radius) {

        int width = randomIntRange(60, (int) radius * 2);
        int randomStrokeWidth = randomIntRange(1, 10);
        int randomStartAngle = randomIntRange(1, 270);
        int randomExtentAngle = randomIntRange(10, 360 - randomStartAngle);
        long randomMillis = randomIntRange(0, 33);
        Color someColor = color;
        if (color == null) {
            someColor = BLUE1;
        }
        final ArcPiece arcPiece = ArcPieceBuilder.create()
                .strokeColor(someColor)
                .strokeWidth(randomStrokeWidth)
                .x(radius - (width / 2))
                .y(radius - (width / 2))
                .w(width)
                .h(width)
                .startAngle(randomStartAngle)
                .arcExtent(randomExtentAngle)
                .displayTimePerFrameMillis(randomMillis)
                .pixelsToMove(2)
                .build();
        arcPiece.clockwise = new Random().nextBoolean();

        return arcPiece;
    }
}
