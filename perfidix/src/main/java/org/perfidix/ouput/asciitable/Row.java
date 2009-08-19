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
 * A table's row contains the data, keeps a reference to its parent and computes
 * its own width.
 * 
 * @author Alexander Onea, neue Couch
 */
public final class Row extends TabularComponent {

    /**
     * The data, converted to string.
     */
    private final String[] data;

    /**
     * Constructor.
     * 
     * @param numberOfColumns
     *            of the table
     * @param myParent
     *            table to be printed to
     * @param paramData
     *            data to be plotted
     */
    public Row(
            final int numberOfColumns, final NiceTable myParent,
            final String[] paramData) {
        super(myParent);
        data = paramData;
        for (int i = 0; i < data.length; i++) {
            getTable().updateColumnWidth(i, data[i].toString().length());
            data[i] = data[i].toString().trim();
        }
    }

    /**
     * Returns the row's total width.
     * 
     * @return the width of the row.
     */
    public final int getRowWidth() {

        int k = 0;
        for (int i = 0; i < data.length; i++) {
            k += getTable().getColumnWidth(i);
        }
        return k;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String draw() {

        final String[] row = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            row[i] =
                    Util.pad(
                            data[i], ' ', getTable().getColumnWidth(i),
                            getTable().getOrientation(i));

        }
        return Util
                .combine(
                        new String(new char[] { TabularComponent.BORDER }),
                        SPACE,
                        Util
                                .implode(
                                        Util
                                                .combine(
                                                        SPACE,
                                                        new String(
                                                                new char[] { TabularComponent.BORDER }),
                                                        SPACE), row),
                        SPACE, new String(
                                new char[] { TabularComponent.BORDER }),
                        NEWLINE);

    }

}
