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
import javafx.collections.ObservableMap;
import javafx.stage.Stage;
import org.ewidgetfx.core.LaunchInfo;
import org.ewidgetfx.core.Widget;
import org.ewidgetfx.core.WidgetIcon;
import org.ewidgetfx.core.WidgetProxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class WidgetLoaderImpl implements WidgetLoader {

    private static final Logger logger = Logger.getLogger(WidgetLoaderImpl.class);

    File widgetDir = null;
    public static final String JABS_DIRECTORY = "jabs";
    public static final String LIBS_DIRECTORY = "libs";

    public WidgetLoaderImpl(File widgetDir) {
        this.widgetDir = widgetDir;
    }

    @Override
    public File getDefaultWidgetDirectory() {
        return widgetDir;
    }

    @Override
    public Widget loadFile(File widgetJarFile, Stage containerStage, ClassLoader parentClassLoader) {
        Widget widgetProxy = null;
        if (getDefaultWidgetDirectory() != null && !getDefaultWidgetDirectory().exists()) {
            getDefaultWidgetDirectory().mkdirs();
        }

        int numJars = 0;
        try {
            // get manifest entries
            JarFile jabFile = null;

            jabFile = new JarFile(widgetJarFile);

            String widgetTypeStr = jabFile.getManifest().getMainAttributes().getValue("widget-type");

            if (widgetTypeStr == null) {
                System.out.println("Skipping: " + widgetJarFile.getName());
                jabFile.close();
                return null;
            }
            // load jar dependencies
            String classpathStr = jabFile.getManifest().getMainAttributes().getValue("Class-Path");

            System.out.println("classpath = " + classpathStr);
            String[] jarsDeps = null;
            if (classpathStr != null) {
                jarsDeps = classpathStr.split("\\s+");
            }
            // count the widget jar
            numJars++;
            numJars += jarsDeps.length;
            // put jars there first.
            installDependencies(jabFile, JABS_DIRECTORY, LIBS_DIRECTORY);

            URL[] urls = new URL[numJars];
            for (int i = 0; i < jarsDeps.length; i++) {
                System.out.println("jar deps: " + jarsDeps[i]);

                File jarFileDep = new File(new File(".").getAbsoluteFile().getParentFile() + File.separator + jarsDeps[i]);

                urls[i] = jarFileDep.toURI().toURL();
            }
            urls[urls.length - 1] = widgetJarFile.toURI().toURL();

            if (LaunchInfo.LaunchType.WIDGET.toString().equalsIgnoreCase(widgetTypeStr)) {

                String clazzName = jabFile.getManifest().getMainAttributes().getValue("widget-execution-line");
                WidgetUrlClassLoader child = new WidgetUrlClassLoader(urls, parentClassLoader);
                Class classToLoad = Class.forName(clazzName, true, child);

                widgetProxy = (Widget) classToLoad.newInstance();
                widgetProxy = (Widget) Proxy.newProxyInstance(Widget.class.getClassLoader(),
                        new Class<?>[]{Widget.class},
                        new WidgetProxy(widgetProxy));

                String widgetName = jabFile.getManifest().getMainAttributes().getValue("widget-name");
                String widgetVersion = jabFile.getManifest().getMainAttributes().getValue("widget-version");
                String widgetDescr = jabFile.getManifest().getMainAttributes().getValue("widget-description");
                String widgetVendor = jabFile.getManifest().getMainAttributes().getValue("widget-vendor");
                String widgetVendorUrl = jabFile.getManifest().getMainAttributes().getValue("widget-vendor-url");
                String widgetVendorEmail = jabFile.getManifest().getMainAttributes().getValue("widget-vendor-email");
                String widgetDecoration = jabFile.getManifest().getMainAttributes().getValue("widget-decoration");
                widgetProxy.setName(widgetName);
                widgetProxy.setVersion(widgetVersion);
                widgetProxy.setDescription(widgetDescr);
                widgetProxy.setVendor(widgetVendor);
                widgetProxy.setVendorUrl(widgetVendorUrl);
                widgetProxy.setVendorEmail(widgetVendorEmail);
                Widget.DECORATION decoration = null;
                try {
                    widgetProxy.setDecoration(Widget.DECORATION.valueOf(widgetDecoration));
                } catch (Exception e) {
                    widgetProxy.setDecoration(Widget.DECORATION.NON_STAGED_UNDECORATED);
                }

                // allow widget to be removed.
                jabFile.close();

                LaunchInfo info = new LaunchInfo();
                info.setLaunchType(LaunchInfo.LaunchType.WIDGET);
                info.setExecutionLine(clazzName);
                info.setWidget(widgetProxy);

                // BEGINNING Widget lifecycle
                // 1. call buildWidgetIcon()
                WidgetIcon widgetIcon = widgetProxy.buildWidgetIcon();
                // a reference to the widget
                widgetIcon.setWidgetFilename(widgetJarFile.getName());
                final Widget w = widgetProxy;
                w.setWidgetIcon(widgetIcon);

                // 2. call startBackground()
                widgetProxy.startBackground();

                widgetIcon.setOnMouseClicked(WidgetFactory.createLaunchWidgetEventHandler(containerStage, w));

            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException e1) {
            logger.error("Exceptionw when loading Widgets ", e1);
        }
        return widgetProxy;
    }

    private void installDependencies(final JarFile jabFile, String widgetPath, String libsPath) throws IOException {
        // load jar dependencies
        String classpathStr = jabFile.getManifest().getMainAttributes().getValue("Class-Path");

        String[] jarsDeps = null;
        if (classpathStr != null) {
            jarsDeps = classpathStr.split("\\s+");
        }
        if (jarsDeps == null || jarsDeps.length == 0) {
            return;
        }
        final File widgetDir = new File(new File(".").getAbsoluteFile().getParentFile() + File.separator + widgetPath);
        final File libsDir = new File(new File(".").getAbsoluteFile().getParentFile() + File.separator + widgetPath + File.separator + libsPath);

        if (!widgetDir.exists()) {
            boolean created = widgetDir.mkdirs();
        }
        if (!libsDir.exists()) {
            boolean created = libsDir.mkdirs();
        }

        // extract jars from classpath.
        final List<String> deps = new ArrayList<>();

        for (String fileName : jarsDeps) {
            String simpleName = null;
            int slashIndx = fileName.lastIndexOf("/");
            if (slashIndx > -1) {
                simpleName = fileName.substring(slashIndx + 1);
            }
            // extract jar
            deps.add(simpleName);
        }

        Stream<JarEntry> files = jabFile.stream().filter((Predicate) (jarEntry) -> {
            JarEntry je = (JarEntry) jarEntry;
            return (!je.isDirectory() && deps.contains(je.getName()));
        });

        files.forEach((JarEntry jarEntry) -> {
            File tmpJar = new File(libsDir.getAbsolutePath() + File.separator + jarEntry.getName());
            if (tmpJar.exists()) {
                return;
            }
            System.out.println("Loading... " + tmpJar);
            java.io.InputStream is = null; // get the input stream
            java.io.FileOutputStream fos = null;
            try {
                is = jabFile.getInputStream(jarEntry);

                try {
                    fos = new java.io.FileOutputStream(tmpJar);

                } catch (FileNotFoundException e) {
                    logger.error("File not found ", e);
                }
                while (is.available() > 0) {  // write contents of 'is' to 'fos'
                    fos.write(is.read());
                }
                // flush bytes to disk
                fos.flush();

                fos.close();
                is.close();
            } catch (IOException e) {
                logger.error("IO exception ", e);
            } finally {
                try {

                    fos.close();
                    is.close();
                } catch (IOException e) {
                    logger.error("Exception when finally ", e);
                    fos = null;
                    is = null;
                }
            }
        });

    }

    @Override
    public ObservableMap<String, Widget> loadWidgets(File widgetDir, String filePattern, Stage containerStage, ClassLoader parentClassLoader) {
        ObservableMap<String, Widget> widgetMap = FXCollections.observableMap(new HashMap<String, Widget>());

        File[] widgets = null;
        if (widgetDir.exists()) {
            widgets = widgetDir.listFiles((FilenameFilter) (dir, name) -> {
                Pattern p = Pattern.compile(filePattern);
                Matcher m = p.matcher(name);
                return m.matches();
            });

            if (widgets != null && widgets.length > 0) {
                for (File widget1 : widgets) {
                    System.out.println(widget1.getName());
                    Widget widget = loadFile(widget1, containerStage, this.getClass().getClassLoader());
                    if (widget == null) {
                        continue;
                    }
                    widgetMap.put(widget1.getName(), widget);
                    System.out.println("loaded: " + widget1.getName());
                }
            }
        } else {
            widgetDir.mkdirs();
        }

        return widgetMap;
    }
}

class WidgetUrlClassLoader extends URLClassLoader {

    WidgetUrlClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    WidgetUrlClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
