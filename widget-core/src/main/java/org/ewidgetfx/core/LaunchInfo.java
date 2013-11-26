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

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class LaunchInfo {

    public static enum LaunchType {

        NATIVE_APP, WIDGET, SHELL_SCRIPT
    };

    private String executionLine;
    private LaunchType launchType;
    private Widget widget;

    public LaunchInfo() {
    }

    ;

    public LaunchInfo(String executionLine, LaunchType launchType, Widget widget) {
        this.executionLine = executionLine;
        this.launchType = launchType;
        this.widget = widget;
    }

    public String getExecutionLine() {
        return executionLine;
    }

    public void setExecutionLine(String executionLine) {
        this.executionLine = executionLine;
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    public void setLaunchType(LaunchType launchType) {
        this.launchType = launchType;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }
}
