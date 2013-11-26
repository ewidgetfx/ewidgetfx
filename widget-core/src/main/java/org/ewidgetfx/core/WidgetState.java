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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetState {

    private final BooleanProperty buildIconProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty startBackgroundProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty initializedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty startedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty pausedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty resumedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty stopProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty stopBackgroundProperty = new SimpleBooleanProperty(false);

    public boolean getInitializedProperty() {
        return initializedProperty.get();
    }

    public BooleanProperty initializedPropertyProperty() {
        return initializedProperty;
    }

    public void setInitializedProperty(boolean initializedProperty) {
        this.initializedProperty.set(initializedProperty);
    }

    public boolean getStartedProperty() {
        return startedProperty.get();
    }

    public BooleanProperty startedPropertyProperty() {
        return startedProperty;
    }

    public void setStartedProperty(boolean startedProperty) {
        this.startedProperty.set(startedProperty);
    }

    public boolean getPausedProperty() {
        return pausedProperty.get();
    }

    public BooleanProperty pausedPropertyProperty() {
        return pausedProperty;
    }

    public void setPausedProperty(boolean pausedProperty) {
        this.pausedProperty.set(pausedProperty);
    }

    public boolean getResumedProperty() {
        return resumedProperty.get();
    }

    public BooleanProperty resumedPropertyProperty() {
        return resumedProperty;
    }

    public void setResumedProperty(boolean resumedProperty) {
        this.resumedProperty.set(resumedProperty);
    }

    public boolean getStopProperty() {
        return stopProperty.get();
    }

    public BooleanProperty stopPropertyProperty() {
        return stopProperty;
    }

    public void setStopProperty(boolean stopProperty) {
        this.stopProperty.set(stopProperty);
    }

    public boolean getBuildIconProperty() {
        return buildIconProperty.get();
    }

    public BooleanProperty buildIconPropertyProperty() {
        return buildIconProperty;
    }

    public void setBuildIconProperty(boolean buildIconProperty) {
        this.buildIconProperty.set(buildIconProperty);
    }

    public boolean getStartBackgroundProperty() {
        return startBackgroundProperty.get();
    }

    public BooleanProperty startBackgroundPropertyProperty() {
        return startBackgroundProperty;
    }

    public void setStartBackgroundProperty(boolean startBackgroundProperty) {
        this.startBackgroundProperty.set(startBackgroundProperty);
    }

    public boolean getStopBackgroundProperty() {
        return stopBackgroundProperty.get();
    }

    public BooleanProperty stopBackgroundPropertyProperty() {
        return stopBackgroundProperty;
    }

    public void setStopBackgroundProperty(boolean stopBackgroundProperty) {
        this.stopBackgroundProperty.set(stopBackgroundProperty);
    }
}
