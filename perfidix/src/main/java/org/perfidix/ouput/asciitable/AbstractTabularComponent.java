/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.ouput.asciitable;

/**
 * This abstract class represents all drawable items in this {@link NiceTable}.
 * 
 * @author Alexander Onea, neue Couch
 */
public abstract class AbstractTabularComponent {

    /** Alignment in the cells. */
    public enum Alignment {
        /** Left alignement. */
        Left,
        /** Right alignement. */
        Right,
        /** Center alignement. */
        Center
    };

    /**
     * Constant for the newline-symbol.
     */
    protected static final String NEWLINE = "\n";

    /**
     * Border symbol, can be changed in the runtime.
     */
    protected static final char BORDER = '|';

    /**
     * Constant for the space between data and border.
     */
    protected static final String SPACE = " ";

    /** Instance to draw to. */
    private transient final NiceTable table;

    /**
     * Constructor.
     * 
     * @param paramTable
     *            to be drawn
     */
    protected AbstractTabularComponent(final NiceTable paramTable) {
        table = paramTable;
    }

    /**
     * Drawing this item.
     * 
     * @return a string representation to draw this line.
     */
    protected abstract String draw();

    /**
     * Getter for member table.
     * 
     * @return the table
     */
    protected final NiceTable getTable() {
        return table;
    }

}
