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
package org.ewidgetfx.applauncher.apptrayfx.tileselector;

import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Carl Dea <carl.dea@gmail.com>
 * @since 1.0
 */
public class TileSelector extends Control {

    protected static final String DEFAULT_STYLE_CLASS = "tile-selector";
    protected List<KeyBinding> keyBindingList = FXCollections.observableList(new ArrayList<KeyBinding>());
    private SimpleIntegerProperty numRows = new SimpleIntegerProperty(2);
    private SimpleIntegerProperty numCols = new SimpleIntegerProperty(2);
    private SimpleIntegerProperty cellNumber = new SimpleIntegerProperty(1);

    public TileSelector() {
        init();
    }

    public TileSelector(int rows, int cols) {
        numRows.set(rows);
        numCols.set(cols);
        init();
    }

    protected void init() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    @Override
    protected String getUserAgentStylesheet() {
        return getClass().getResource("/style/tile-selector.css").toExternalForm();
    }

    public List<KeyBinding> getKeyBindingList() {
        return keyBindingList;
    }

    public int getNumRows() {
        return numRows.get();
    }

    public SimpleIntegerProperty numRowsProperty() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows.set(numRows);
    }

    public int getNumCols() {
        return numCols.get();
    }

    public SimpleIntegerProperty numColsProperty() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols.set(numCols);
    }

    public int getCellNumber() {
        return cellNumber.get();
    }

    public SimpleIntegerProperty cellNumberProperty() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber.set(cellNumber);
    }
}
