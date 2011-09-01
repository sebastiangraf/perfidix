/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

        final String[] row = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            row[i] =
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
