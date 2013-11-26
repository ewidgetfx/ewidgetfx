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

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

 /**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetInfo {
    private SimpleDoubleProperty savedX = new SimpleDoubleProperty();
    private SimpleDoubleProperty savedY = new SimpleDoubleProperty();
    private SimpleDoubleProperty currentX = new SimpleDoubleProperty();
    private SimpleDoubleProperty currentY = new SimpleDoubleProperty();
    private ChangeListener<Number> currentXChangeListener;
    private ChangeListener<Number> currentYChangeListener;

    public double getSavedX() {
        return savedX.get();
    }

    public SimpleDoubleProperty savedXProperty() {
        return savedX;
    }

    public void setSavedX(double savedX) {
        this.savedX.set(savedX);
    }

    public double getSavedY() {
        return savedY.get();
    }

    public SimpleDoubleProperty savedYProperty() {
        return savedY;
    }

    public void setSavedY(double savedY) {
        this.savedY.set(savedY);
    }

    public double getCurrentX() {
        return currentX.get();
    }

    public SimpleDoubleProperty currentXProperty() {
        return currentX;
    }

    public void setCurrentX(double currentX) {
        this.currentX.set(currentX);
    }

    public double getCurrentY() {
        return currentY.get();
    }

    public SimpleDoubleProperty currentYProperty() {
        return currentY;
    }

    public void setCurrentY(double currentY) {
        this.currentY.set(currentY);
    }

    public ChangeListener<Number> getCurrentXChangeListener() {
        return currentXChangeListener;
    }

    public void setCurrentXChangeListener(ChangeListener<Number> currentXChangeListener) {
        this.currentXChangeListener = currentXChangeListener;
    }

    public ChangeListener<Number> getCurrentYChangeListener() {
        return currentYChangeListener;
    }

    public void setCurrentYChangeListener(ChangeListener<Number> currentYChangeListener) {
        this.currentYChangeListener = currentYChangeListener;
    }
}
