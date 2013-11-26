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
package org.ewidgetfx.util;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.ewidgetfx.core.Widget;
import org.ewidgetfx.core.WidgetStage;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetFactory {

    static WidgetLoader widgetLoader = new WidgetLoaderImpl(new File(new File(".").getAbsoluteFile().getParentFile() + File.separator + "jabs"));

    static ObservableMap<String, Widget> widgetMap = FXCollections.observableMap(new HashMap<String, Widget>());

    ;
    private WidgetFactory() {
    }

    public static Widget loadFile(File jarFile, Stage containerStage, ClassLoader parentClassLoader) {
        return widgetLoader.loadFile(jarFile, containerStage, parentClassLoader);
    }

    public static void loadWidgets(File widgetDir, String filePattern, Stage containerStage, ClassLoader parentClassLoader) {
        widgetMap.clear();
        widgetMap.putAll(widgetLoader.loadWidgets(widgetDir, filePattern, containerStage, parentClassLoader));
    }

    public static Widget lookup(String name) {
        return widgetMap.get(name);
    }

    public static void update(String name, Widget widget) {
        widgetMap.put(name, widget);
    }

    public static void remove(String name) {
        widgetMap.remove(name);
    }

    public static void addListener(MapChangeListener<String, Widget> mapChangeListener) {
        widgetMap.addListener(mapChangeListener);
    }

    public static Collection<Widget> retrieveAll() {
        return widgetMap.values();
    }

    public static EventHandler<? super MouseEvent> createLaunchWidgetEventHandler(Stage primaryStage, Widget w) {
        return me -> {
            Widget.DECORATION decor = w.getDecoration();
            if (decor == null) {
                decor = Widget.DECORATION.STAGED_UNDECORATED;
                w.setDecoration(decor);
            }

            switch (decor) {
                case STAGED_UNDECORATED:
                case STAGED_CLOSE:
                case STAGED_CONFIG:
                case STAGED_CONFIG_CLOSE:
                case STAGED_OS_TITLE_BAR:
                    WidgetStage widgetStage = null;
                    // 3. init the widget if not already
                    // widget developer's init() gets called. Called only once.
                    if (!w.getWidgetState().getInitializedProperty()) {
                        widgetStage = new WidgetStage(primaryStage, w);
                        w.init();
                    }
                    System.out.println("started prop: " + w.getWidgetState().getStartedProperty());
                    if (!w.getWidgetState().startedPropertyProperty().get()) {

                        // 4. start the widget
                        w.start();

                        widgetStage.show();
                    }
                    break;
                case NON_STAGED_UNDECORATED:
                case NON_STAGED_CLOSE:
                case NON_STAGED_CONFIG:
                case NON_STAGED_CONFIG_CLOSE:
                default:
                    w.setParentStage(primaryStage);
            }

        };
    }
}
