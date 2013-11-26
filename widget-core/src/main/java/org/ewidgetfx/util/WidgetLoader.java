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

import javafx.collections.ObservableMap;
import javafx.stage.Stage;
import org.ewidgetfx.core.Widget;

import java.io.File;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public interface WidgetLoader {

    File getDefaultWidgetDirectory();

    Widget loadFile(File jarFile, Stage containerStage, ClassLoader parentClassLoader);

    ObservableMap<String, Widget> loadWidgets(File widgetDir, String filePattern, Stage containerStage, ClassLoader parentClassLoader);
}
