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
public final class Row extends AbstractTabularComponent {

    /**
     * The data, converted to string.
     */
    private transient final String[] data;

    /**
     * Constructor.
     * 
     * @param myParent
     *            table to be printed to
     * @param paramData
     *            data to be plotted
     */
    public Row(final NiceTable myParent, final String[] paramData) {
        super(myParent);
        data = new String[paramData.length];
        System.arraycopy(paramData, 0, data, 0, paramData.length);
        for (int i = 0; i < data.length; i++) {
            getTable().updateColumnWidth(i, data[i].length());
            data[i] = data[i].trim();
        }
    }

    /**
     * Returns the row's total width.
     * 
     * @return the width of the row.
     */
    public int getRowWidth() {

        int overallWidth = 0;
        for (int i = 0; i < data.length; i++) {
            overallWidth += getTable().getColumnWidth(i);
        }
        return overallWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String draw() {

        final String[] row = new String[data.length]; // NOPMD by sebi on 26.08.09 21:22
        for (int i = 0; i < data.length; i++) {
            row[i] = // NOPMD by sebi on 26.08.09 21:22
                    Util.pad(
                            data[i], ' ', getTable().getColumnWidth(i),
                            getTable().getOrientation(i));

        }
        return Util
                .combine(
                        new String(
                                new char[] { AbstractTabularComponent.BORDER }),
                        SPACE,
                        Util
                                .implode(
                                        Util
                                                .combine(
                                                        SPACE,
                                                        new String(
                                                                new char[] { AbstractTabularComponent.BORDER }),
                                                        SPACE), row),
                        SPACE,
                        new String(
                                new char[] { AbstractTabularComponent.BORDER }),
                        NEWLINE);

    }

}
