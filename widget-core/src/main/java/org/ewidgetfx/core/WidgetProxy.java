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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetProxy implements InvocationHandler {

    private final Widget realWidget;

    public WidgetProxy(Widget w) {
        this.realWidget = w;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object returnObj = null;
        try {
            returnObj = method.invoke(realWidget, args);
        } catch (Exception e) {
            ((InvocationTargetException) e).getTargetException().printStackTrace();
            e.printStackTrace();
            return null;
        }
        String methodName = method.getName();
        switch (methodName) {
            case "buildWidgetIcon":
                realWidget.getWidgetState().buildIconPropertyProperty().setValue(true);
                break;
            case "startBackground":
                realWidget.getWidgetState().startBackgroundPropertyProperty().setValue(true);
                realWidget.getWidgetState().stopBackgroundPropertyProperty().set(false);
                break;
            case "init":
                realWidget.getWidgetState().initializedPropertyProperty().setValue(true);
                realWidget.getWidgetState().stopPropertyProperty().setValue(false);
                break;
            case "start":
                realWidget.getWidgetState().startedPropertyProperty().setValue(true);
                realWidget.getWidgetState().stopPropertyProperty().setValue(false);
                break;
            case "pause":
                realWidget.getWidgetState().pausedPropertyProperty().setValue(true);
                realWidget.getWidgetState().resumedPropertyProperty().setValue(false);
            case "resume":
                realWidget.getWidgetState().resumedPropertyProperty().setValue(true);
                realWidget.getWidgetState().pausedPropertyProperty().setValue(false);
                break;
            case "stop":
                realWidget.getWidgetState().stopPropertyProperty().setValue(true);
                realWidget.getWidgetState().startedPropertyProperty().setValue(false);
                realWidget.getWidgetState().initializedPropertyProperty().setValue(false);
                break;
            case "stopBackground":
                realWidget.getWidgetState().stopBackgroundPropertyProperty().set(true);
                realWidget.getWidgetState().startBackgroundPropertyProperty().setValue(false);
                break;

        }
        System.out.println(new Date() + " " + realWidget.getName() + " widget's " + method.getName() + "() method was invoked ");

        return returnObj;
    }
}
