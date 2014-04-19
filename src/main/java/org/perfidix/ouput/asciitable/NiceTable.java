/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.ouput.asciitable;


import org.perfidix.ouput.asciitable.AbstractTabularComponent.Alignment;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a table which allows formatting of data as an ascii table. it takes care of automatically
 * adjusting widths of the tables. A word on the orientations: at the current point of development, the orientation
 * works on COLUMNS and NOT on ROWs. so you can set the orientation of a full ROW, but you cannot set the orientation
 * for each field[x][y] in the table.
 *
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public final class NiceTable {

    /**
     * Container for all rows.
     */
    private transient final List<AbstractTabularComponent> rows;

    /**
     * Storing the length of chars in the different columns.
     */
    private transient final int[] columnLengths;

    /**
     * An array holding the orientations of columns.
     */
    private transient final Alignment[] orientations;

    /**
     * Constructor. needs the number of columns to show.
     *
     * @param numberOfColumns the number of columns to display.
     */
    public NiceTable(final int numberOfColumns) {

        columnLengths = new int[numberOfColumns];
        orientations = new Alignment[numberOfColumns];
        rows = new ArrayList<AbstractTabularComponent>();
    }

    /**
     * Adds a header.
     *
     * @param title the text to display within the header
     */
    public void addHeader(final String title) {
        addHeader(title, '=', Alignment.Left);
    }

    /**
     * Adds a header row to the table. can be used to give a table some title.
     * <p/>
     * <pre>
     *  addHeader(&quot;hello&quot;,'.',NiceTable.LEFT)
     *  would produce a row like
     *  ... hello ..........................
     *   {rows in here.}
     * </pre>
     *
     * @param title       the string to display as a header
     * @param mark        the mark to use for the rest of the column
     * @param orientation the orientation of the header column.
     */
    public void addHeader(final String title, final char mark, final Alignment orientation) {
        final Header header = new Header(title, mark, orientation, this);
        rows.add(header);
    }

    /**
     * Adds a string row. checks that the strings added do not contain newlines, because if so, it has to split them in
     * order to make them fit the row.
     *
     * @param data the array of data.
     */
    public void addRow(final String[] data) {
        if (anyStringContainsNewLine(data)) {
            final String[][] theMatrix = Util.createMatrix(data);
            for (int i = 0; i < theMatrix.length; i++) {
                addRow(theMatrix[i]);
            }
        } else {
            final Row myRow = new Row(this, data);
            rows.add(myRow);
        }

    }

    /**
     * allows the addition of lines. think of it as a horizontal rule in a table.
     *
     * @param fill any character with which to draw the line.
     */
    public void addLine(final char fill) {
        rows.add(new DynamicLine(fill, this));
    }

    /**
     * the main method doing the work. draws everyting.
     *
     * @return the formatted table.
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < rows.size(); i++) {
            final AbstractTabularComponent myObj = rows.get(i);
            builder.append(myObj.draw());
        }
        return builder.toString();
    }

    /**
     * Returns the global column width at index columnIndex.
     *
     * @param columnIndex the index of the column for which to fetch the width.
     * @return the width (in number of chars) for the column index.
     */
    protected int getColumnWidth(final int columnIndex) {

        return columnLengths[columnIndex];
    }

    /**
     * Performs an update on the column lengths.
     *
     * @param index   the index of the column
     * @param newSize the new size of the column
     */
    protected void updateColumnWidth(final int index, final int newSize) {
        columnLengths[index] = Math.max(columnLengths[index], newSize);
    }

    /**
     * Returns the actual number of columns this table has got.
     *
     * @return the number of columns the table may contain.
     */
    private int numColumns() {
        return this.columnLengths.length;
    }

    /**
     * Returns the orientation of a column.
     *
     * @param columnIndex integer
     * @return Alignment for the column
     */
    protected Alignment getOrientation(final int columnIndex) {

        return orientations[columnIndex];
    }

    /**
     * Returns the row width.
     *
     * @return int the width of the row
     */
    private int getRowWidth() {
        int returnVal = 1;
        if (rows.size() < 1) {
            returnVal = 0;
        }
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i) instanceof Row) {
                returnVal = ((Row) rows.get(i)).getRowWidth();
            }
        }
        return returnVal;
    }

    /**
     * Returns the total width of the table.
     *
     * @return int the width of the table
     */
    protected int getTotalWidth() {
        return this.getRowWidth() + 3 * numColumns() + 1;
    }

    /**
     * Tests whether any string contains a newline symbol.
     *
     * @param data the array to check.
     * @return whether any of the strings contains a newline symbol.
     */
    private boolean anyStringContainsNewLine(final String[] data) {
        boolean returnVal = false;
        for (int i = 0; i < data.length; i++) {
            if (Util.containsNewlines(data[i])) {
                returnVal = true;
            }
        }
        return returnVal;
    }

}
