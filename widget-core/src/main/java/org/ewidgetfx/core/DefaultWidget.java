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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public abstract class DefaultWidget extends Pane implements Widget {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty version = new SimpleStringProperty();
    private String description;
    private String vendor;
    private String vendorUrl;
    private String vendorEmail;
    private DECORATION decoration;

    private final WidgetState widgetState = new WidgetState();
    private LaunchInfo launchInfo;
    private final ObjectProperty<WidgetIcon> widgetIcon = new SimpleObjectProperty<>();
    private Stage parentStage;

    public DefaultWidget() {
    }

    public DefaultWidget(LaunchInfo launchInfo, String name, String version, WidgetIcon widgetIcon) {
        this.name.setValue(name);
        this.version.setValue(version);
        this.launchInfo = launchInfo;
        this.widgetIcon.setValue(widgetIcon);
    }

    @Override
    public String getName() {
        if (name.get() == null) {
            name.setValue(getClass().getName());
        }
        return name.get();
    }

    @Override
    public void setName(String name) {
        this.name.setValue(name);
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String getVersion() {
        return version.get();
    }

    @Override
    public void setVersion(String version) {
        this.version.setValue(version);
    }

    @Override
    public StringProperty versionProperty() {
        return version;
    }

    @Override
    public DECORATION getDecoration() {
        return decoration;
    }

    @Override
    public void setDecoration(DECORATION decoration) {
        this.decoration = decoration;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String descr) {
        this.description = descr;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public String getVendorUrl() {
        return vendorUrl;
    }

    @Override
    public void setVendorUrl(String vendorUrl) {
        this.vendorUrl = vendorUrl;
    }

    @Override
    public String getVendorEmail() {
        return vendorEmail;
    }

    @Override
    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    @Override
    public LaunchInfo getLaunchInfo() {
        return launchInfo;
    }

    @Override
    public void setLaunchInfo(LaunchInfo launchInfo) {
        this.launchInfo = launchInfo;
    }

    @Override
    public WidgetIcon getWidgetIcon() {
        return widgetIconProperty().get();
    }

    @Override
    public void setWidgetIcon(WidgetIcon widgetIcon) {
        this.widgetIcon.setValue(widgetIcon);
    }

    @Override
    public ObjectProperty<WidgetIcon> widgetIconProperty() {
        return widgetIcon;
    }

    @Override
    public Pane getAsNode() {
        return this;
    }

    @Override
    public Stage getParentStage() {
        return parentStage;
    }

    @Override
    public void setParentStage(Stage stage) {
        parentStage = stage;
    }

    @Override
    public WidgetState getWidgetState() {
        return widgetState;
    }

//    @Override
//    public WidgetIcon buildWidgetIcon() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
    @Override
    public void startBackground() {

    }

//    @Override
//    public void init() {
//        System.out.println("initialize widget");
//    }
//        @Override
//    public void start() {
//        System.out.println("starting widget");
//    }
    @Override
    public void pause() {
        System.out.println("pausing widget");
    }

    @Override
    public void resume() {
        System.out.println("resuming widget");
    }

//    @Override
//    public void stop() {
//        System.out.println("stopping widget");
//    }
    @Override
    public void stopBackground() {

    }
}
