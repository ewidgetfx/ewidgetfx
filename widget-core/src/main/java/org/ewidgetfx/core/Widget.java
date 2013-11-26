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
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public interface Widget {

    public static enum DECORATION {

        STAGED_OS_TITLE_BAR,
        STAGED_CLOSE,
        STAGED_CONFIG_CLOSE,
        STAGED_CONFIG,
        STAGED_UNDECORATED,
        NON_STAGED_CLOSE,
        NON_STAGED_CONFIG_CLOSE,
        NON_STAGED_CONFIG,
        NON_STAGED_UNDECORATED
    };

    DECORATION getDecoration();

    void setDecoration(DECORATION decoration);

    String getName();

    void setName(String name);

    StringProperty nameProperty();

    String getVersion();

    void setVersion(String version);

    StringProperty versionProperty();

    String getDescription();

    void setDescription(String descr);

    String getVendor();

    void setVendor(String vendor);

    String getVendorUrl();

    void setVendorUrl(String vendorUrl);

    String getVendorEmail();

    void setVendorEmail(String vendorEmail);

    LaunchInfo getLaunchInfo();

    void setLaunchInfo(LaunchInfo launchInfo);

    WidgetIcon getWidgetIcon();

    void setWidgetIcon(WidgetIcon widgetIcon);

    ObjectProperty<WidgetIcon> widgetIconProperty();

    Pane getAsNode();

    Stage getParentStage();

    void setParentStage(Stage stage);

    WidgetState getWidgetState();

    /**
     * Returns a created WidgetIcon for the app container to use. Called 1st.
     *
     * @return WidgetIcon containing a raw node representing the icon. App containers can resize.
     */
    WidgetIcon buildWidgetIcon();

    /**
     * Called after buildWidgetIcon() method to allow background processes to occur. Called 2nd. Typically to collect
     * data to update WidgetIcon's Icon overlay. An example would be an email widget periodically checking email to
     * update Icon overlay the number of emails received.
     */
    void startBackground();

    /**
     * Initialize the widget. If the developer calls this method the framework will not call it. Called 3rd.
     */
    void init();

    /**
     * Start is meant to be called when the user clicks to the icon to launch window. Called 4th. Typically to start
     * animations. This should not be confused with backgroundStart()
     */
    void start();

    /**
     * Pause is typically used to pause animations. Or other developer defined resources.
     */
    void pause();

    /**
     * Resume is typically used to resume a animations. Or other developer defined resources.
     */
    void resume();

    /**
     * Stop is called when the widget is closed and not visible. Typically this is to stop animations, and minor
     * cleanup. The framework will call stop and stopBackground() method when exiting the app container.
     */
    void stop();

    /**
     * Stops any background processes. Called when widget is being closed by the framework when exiting app contain.
     * Called last.
     *
     */
    void stopBackground();

}
