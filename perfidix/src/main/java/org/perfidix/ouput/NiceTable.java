/*
 * Copyright 2007 University of Konstanz
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
 * $Id: NiceTable.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.ouput;

import java.util.ArrayList;

/**
 * This class represents a table which allows formatting of data as an ascii
 * table. it takes care of automatically adjusting widths of the tables. A word
 * on the orientations: at the current point of development, the orientation
 * works on COLUMNS and NOT on ROWs. so you can set the orientation of a full
 * ROW, but you cannot set the orientation for each field[x][y] in the table.
 * 
 * @author Alexander Onea, neue Couch
 * @author Sebastian Graf, University of Konstanz
 */
public final class NiceTable {

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
    public static final String NEWLINE = "\n";

    /**
     * Constant for the space between data and border.
     */
    public static final String SPACE = " ";

    /**
     * Container for all rows.
     */
    private final ArrayList<TabluarComponent> rows;

    /**
     * Storing the length of chars in the different columns.
     */
    private final int[] columnLengths;

    /**
     * An array holding the orientations of columns.
     */
    private final Alignment[] orientations;

    /**
     * Border symbol, can be changed in the runtime.
     */
    private char border = '|';

    /**
     * Constructor. needs the number of columns to show.
     * 
     * @param numberOfColumns
     *            the number of columns to display.
     */
    public NiceTable(final int numberOfColumns) {

        columnLengths = new int[numberOfColumns];
        orientations = new Alignment[numberOfColumns];
        rows = new ArrayList<TabluarComponent>();
    }

    /**
     * Adds a header.
     * 
     * @param title
     *            the text to display within the header
     */
    public final void addHeader(final String title) {
        addHeader(title, '=', Alignment.Left);
    }

    /**
     * Adds a header row to the table. can be used to give a table some title.
     * 
     * <pre>
     *  addHeader(&quot;hello&quot;,'.',NiceTable.LEFT)
     *  would produce a row like
     *  ... hello ..........................
     *   {rows in here.}
     * </pre>
     * 
     * @param title
     *            the string to display as a header
     * @param mark
     *            the mark to use for the rest of the column
     * @param orientation
     *            the orientation of the header column.
     */
    public final void addHeader(
            final String title, final char mark,
            final Alignment orientation) {
        final Header h = new Header(title, mark, orientation, this);
        rows.add(h);
    }

    /**
     * Adds a string row. checks that the strings added do not contain newlines,
     * because if so, it has to split them in order to make them fit the row.
     * 
     * @param data
     *            the array of data.
     */
    public final void addRow(final String[] data) {
        if (anyStringContainsNewLine(data)) {
            final String[][] theMatrix = NiceTable.Util.createMatrix(data);
            for (int i = 0; i < theMatrix.length; i++) {
                addRow(theMatrix[i]);
            }
        } else {
            final Row myRow =
                    new Row(this.columnLengths.length, this, data);
            rows.add(myRow);
        }

    }

    /**
     * allows the addition of lines. think of it as a horizontal rule in a
     * table.
     * 
     * @param fill
     *            any character with which to draw the line.
     */
    public final void addLine(final char fill) {
        rows.add(new DynamicLine(fill, this));
    }

    /**
     * the main method doing the work. draws everyting.
     * 
     * @return the formatted table.
     */
    @Override
    public final String toString() {

        final StringBuilder s = new StringBuilder();

        for (int i = 0; i < rows.size(); i++) {
            final TabluarComponent myObj = rows.get(i);
            s.append(myObj.draw());
        }
        return s.toString();
    }

    /**
     * This abstract class represents all drawable items in this
     * {@link NiceTable}.
     * 
     * @author Alexander Onea, neue Couch
     */
    private abstract class TabluarComponent {

        /** Instance to draw to. */
        private final NiceTable table;

        /**
         * Constructor.
         * 
         * @param paramTable
         *            to be drawn
         */
        TabluarComponent(final NiceTable paramTable) {
            table = paramTable;
        }

        /**
         * Drawing this item.
         * 
         * @return a string representation to draw this line.
         */
        abstract String draw();

        /**
         * Getter for member table.
         * 
         * @return the table
         */
        public final NiceTable getTable() {
            return table;
        }

    }

    /**
     * A header row is like a <th>element in HTML with colspan =
     * totalNumberOfColumns.
     * 
     * @author Alexander Onea, neue Couch
     */
    private final class Header extends TabluarComponent {

        /**
         * Title of the header.
         */
        private final String title;

        /**
         * Enclosed char of the header.
         */
        private final char enclosing;

        /**
         * Orientation of the title.
         */
        private final Alignment orientation;

        /**
         * Constructor.
         * 
         * @param contentToDisplay
         *            the string to be present in the header row.
         * @param theEnclosing
         *            a char filling up the rest of the row.
         * @param theOrientation
         *            the orientation of the text
         * @param theTable
         *            the parent table.
         */
        private Header(
                final String contentToDisplay, final char theEnclosing,
                final Alignment theOrientation, final NiceTable theTable) {
            super(theTable);
            this.orientation = theOrientation;
            this.enclosing = theEnclosing;

            this.title =
                    Util.combine(
                            new String(new char[] { enclosing })
                                    .toString(), SPACE, contentToDisplay,
                            SPACE, new String(new char[] { enclosing })
                                    .toString());

        }

        /**
         * {@inheritDoc}
         */
        @Override
        final String draw() {
            return Util.combine(
                    new String(new char[] { border }), Util.pad(
                            title, enclosing,
                            getTable().getTotalWidth() - 2, orientation),
                    new String(new char[] { border }), NEWLINE);

        }

    }

    /**
     * A dynamic, extensible line. Good as a row delemiter.
     * 
     * @author Alexander Onea, neue Couch
     */
    private final class DynamicLine extends TabluarComponent {

        /** Content for this dynamic line. */
        private final char content;

        /**
         * Constructor.
         * 
         * @param theContent
         *            where the row should be filled with.
         * @param theTable
         *            the table to be drawn to.
         */
        private DynamicLine(final char theContent, final NiceTable theTable) {
            super(theTable);
            this.content = theContent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        final String draw() {
            return Util.combine(new String(new char[] { border }), Util
                    .repeat(new String(new char[] { content }), getTable()
                            .getTotalWidth() - 2), new String(
                    new char[] { border }), NEWLINE);

        }

    }

    /**
     * A table's row contains the data, keeps a reference to its parent and
     * computes its own width.
     * 
     * @author Alexander Onea, neue Couch
     */
    private final class Row extends TabluarComponent {

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
        private Row(
                final int numberOfColumns, final NiceTable myParent,
                final String[] paramData) {
            super(myParent);
            data = paramData;
            for (int i = 0; i < data.length; i++) {
                getTable().updateColumnWidth(
                        i, data[i].toString().length());
                data[i] = data[i].toString().trim();
            }
        }

        /**
         * Returns the row's total width.
         * 
         * @return the width of the row.
         */
        private final int getRowWidth() {

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
        final String draw() {

            final String[] row = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                row[i] =
                        Util.pad(data[i], ' ', getTable()
                                .getColumnWidth(i), getTable()
                                .getOrientation(i));

            }
            return Util.combine(
                    new String(new char[] { border }), SPACE, Util
                            .implode(Util.combine(SPACE, new String(
                                    new char[] { border }), SPACE), row),
                    SPACE, new String(new char[] { border }), NEWLINE);

        }

    }

    /**
     * Utilities for the ascii table.
     */
    public static final class Util {

        /**
         * private constructor.
         */
        private Util() {
            // do nothing.
        }

        /**
         * Combines an unknown number of Strings to one String.
         * 
         * @param args
         *            multiple Strings
         * @return the combined string
         */
        private static final String combine(final String... args) {
            final StringBuilder builder = new StringBuilder();
            for (final String arg : args) {
                builder.append(arg);
            }
            return builder.toString();
        }

        /**
         * This method fills one char either on the right site of a given string
         * or on the left site or on both sites. Refer to the example below.
         * 
         * <pre>
         *  pad (&quot;hello&quot;,'-',11,NiceTable.LEFT);
         *  results in &quot;hello------&quot;
         *  pad(&quot;hello&quot;,'/',11,NiceTable.RIGHT);
         *  results in &quot;//////hello&quot;;
         *  pad(&quot;hello&quot;,'.',11,NiceTable.CENTER);
         *  results in &quot;...hello...&quot;
         * </pre>
         * 
         * @return padded string
         * @param doPadWithThis
         *            the string to pad the string with
         * @param data
         *            the data to pad
         * @param orientation
         *            which orientation to take
         * @param totalWidth
         *            the total width of the result string
         */
        private static final String pad(
                final String data, final char doPadWithThis,
                final int totalWidth, final Alignment orientation) {

            final String pad =
                    repeat(new String(new char[] { doPadWithThis }), Math
                            .max(0, totalWidth - data.length()));
            if (orientation == Alignment.Center) {
                return pad.substring(0, pad.length() / 2)
                        + data
                        + pad.substring(pad.length() / 2, pad.length());
            }

            if (orientation == Alignment.Right) {
                return new StringBuilder(pad).append(data).toString();
            } else {
                return new StringBuilder(data).append(pad).toString();
            }

        }

        /**
         * Concantenate a String array "what" with glue "glue".
         * 
         * @param what
         *            the array of strings to concantenate
         * @param glue
         *            the glue string to use.
         * 
         *            <pre>
         * String[] what = { &quot;a&quot;, &quot;b&quot;, &quot;c&quot; };
         * 
         * String s = Util.implode(&quot;-&quot;, what);
         * //result is &quot;a-b-c&quot;
         * </pre>
         * @return String
         */
        private static final String implode(
                final String glue, final String[] what) {

            final StringBuilder s = new StringBuilder();
            for (int i = 0; i < what.length; i++) {
                s.append(what[i]);
                if (i + 1 != what.length) {
                    s.append(glue);
                }
            }
            return s.toString();
        }

        /**
         * a str_repeat function.
         * 
         * @param s
         *            the string to repeat
         * @param numTimes
         *            how many times to concantenate the string
         * @return the repeated string.
         */
        private static final String repeat(
                final String s, final int numTimes) {
            String str = "";

            for (int i = 0; i < numTimes; i++) {
                str += s;
            }
            return str;
        }

        /**
         * Splits a string with the help of a given char.
         * 
         * @return an array of elements
         * @param ch
         *            the separator char
         * @param s
         *            the string to be splitted
         */
        private static final String[] explode(final char ch, final String s) {
            return s.split("\\" + ch);
        }

        /**
         * Returns how many new lines are in the string.
         * 
         * @param s
         *            the string to look upon.
         * @return the number of occurences of {@link NiceTable#NEWLINE} in the
         *         string.
         */
        private static final int numNewLines(final String s) {
            final char[] arr = s.toCharArray();
            int result = 0;
            for (char ch : arr) {
                if (NiceTable.NEWLINE
                        .equals(new String(new char[] { ch }))) {
                    result++;
                }
            }
            return result;
        }

        /**
         * Tells us whether the string contains newlines. it's important to note
         * that only newlines within the string are important, not the newlines
         * at the front or the end of the string.
         * 
         * @param s
         *            to be checked
         * @return boolean if the string contains newlines.
         */
        private static final boolean containsNewlines(final String s) {
            return s.trim().contains(NiceTable.NEWLINE);
        }

        /**
         * Creates a matrix according to the number of new lines given into the
         * method.
         * 
         * @return the matrix.
         * @param data
         *            an array of row data
         */
        private static final String[][] createMatrix(final String[] data) {
            int maxNewLines = 0;
            for (final String col : data) {
                maxNewLines = Math.max(maxNewLines, Util.numNewLines(col));
            }
            final String[][] matrix =
                    new String[maxNewLines + 1][data.length];
            for (int col = 0; col < data.length; col++) {
                final String[] my = Util.explode('\n', data[col]);
                for (int row = 0; row < maxNewLines + 1; row++) {
                    if (my.length > row) {
                        matrix[row][col] = my[row];
                    } else {
                        matrix[row][col] = "";
                    }
                }
            }

            return matrix;

        }

    }

    /**
     * Sets the column delimiter, in order to allow different delimiters for
     * different contexts.
     * 
     * @param c
     *            the border char.
     */
    public final void setColumnDelimiter(final char c) {
        border = c;
    }

    /**
     * Returns the global column width at index columnIndex.
     * 
     * @param columnIndex
     *            the index of the column for which to fetch the width.
     * @return the width (in number of chars) for the column index.
     */
    private final int getColumnWidth(final int columnIndex) {

        return columnLengths[columnIndex];
    }

    /**
     * Performs an update on the column lengths.
     * 
     * @param index
     *            the index of the column
     * @param newSize
     *            the new size of the column
     */
    private final void updateColumnWidth(final int index, final int newSize) {

        columnLengths[index] = Math.max(columnLengths[index], newSize);
    }

    /**
     * Returns the actual number of columns this table has got.
     * 
     * @return the number of columns the table may contain.
     */
    private final int numColumns() {
        return this.columnLengths.length;
    }

    /**
     * Returns the orientation of a column.
     * 
     * @param columnIndex
     *            integer
     * @return Alignment for the column
     */
    private final Alignment getOrientation(final int columnIndex) {

        return orientations[columnIndex];
    }

    /**
     * Returns the row width.
     * 
     * @return int the width of the row
     */
    private final int getRowWidth() {

        if (rows.size() < 1) {
            return 0;
        }
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i) instanceof Row) {
                return ((Row) rows.get(i)).getRowWidth();
            }
        }
        return 1;
    }

    /**
     * Returns the total width of the table.
     * 
     * @return int the width of the table
     */
    private final int getTotalWidth() {
        final int widthFactor1 = 3;
        final int widthFactor2 = 1;
        return this.getRowWidth()
                + widthFactor1
                * numColumns()
                + widthFactor2;
    }

    /**
     * Tests whether any string contains a newline symbol.
     * 
     * @param data
     *            the array to check.
     * @return whether any of the strings contains a newline symbol.
     */
    private final boolean anyStringContainsNewLine(final String[] data) {

        for (int i = 0; i < data.length; i++) {
            if (NiceTable.Util.containsNewlines(data[i])) {
                return true;
            }
        }
        return false;
    }

}
