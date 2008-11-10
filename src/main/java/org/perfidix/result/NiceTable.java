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

package org.perfidix.result;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * ok, it needs a new name, but it's a table which allows formatting of data as
 * an ascii table. it takes care of automatically adjusting widths of the
 * tables. TODO a table could recursively contain other tables, this would
 * affect also the height of table rows. make this one truly 2-dimensional. a
 * word on the orientations: at the current point of development, the
 * orientation works on COLUMNS and NOT on ROWs. so you can set the orientation
 * of a full ROW, but you cannot set the orientation for each field[x][y] in the
 * table. CHANGELOG:
 * --------------------------------------------------------------------
 * 02-02-2006: tested the newline problems and made them work. it is now
 * possible to create tables like | a | b | | c | | with the definition of
 * String s = {"a\nc","b"} ; 08-02-2006: refactored the whole class in to get
 * rid of the formatter warnings. removed the numColumns variable.
 * 
 * @author axo
 * @since 2005-10-21
 */
public class NiceTable {

    /** an index for setting orientation of the column's values to align=left. */
    public static final int LEFT = 999;

    /** css-style: text-align: right. */
    public static final int RIGHT = 998;

    /** css-style: text-align: center. */
    public static final int CENTER = 997;

    /**
     * the border symbol.
     */
    private String theBORDER = "|";

    private static final int MAGIC_THREE = 3;

    /**
     * the newline symbol to use.
     */
    public static final String NEWLINE = "\n";

    /**
     * the space to use between borders and data.
     */
    public static final String SPACE = " ";

    /**
     * the container of the rows.
     */
    private ArrayList<Object> rows = new ArrayList<Object>();

    /**
     * just now, the number of columns is final. that is not really necessary,
     * since we could dynamically add more columns to it, but there needs to be
     * implemented some more things about it, and since i'm not thinking of
     * that, i make this one final.
     */
    private final int numColumns;

    /**
     * data container for the column lengths.
     * 
     * @example: column a contains the string "a" in the first row. so the
     *           column length for the first row is '1'. this gets updated
     *           whenever there's a new row to insert.
     */
    private int[] columnLengths;

    /**
     * an array holding the orientations of columns.
     */
    private int[] orientations;

    /**
     * constructor. needs the number of columns to show.
     * 
     * @param numberOfColumns
     *            the number of columns to display.
     */
    public NiceTable(final int numberOfColumns) {

        this.numColumns = numberOfColumns;
        columnLengths = new int[numColumns];
        orientations = new int[numColumns];
    }

    /**
     * returns the orientation of a column.
     * 
     * @param columnIndex
     *            integer
     */
    private int getOrientation(final int columnIndex) {

        return orientations[columnIndex];
    }

    /**
     * sets the orientation of the column with index i. available orientations
     * are NiceTable.LEFT, NiceTable.RIGHT, NiceTable.CENTER take care to use
     * the correct column index.
     * 
     * @param columnIndex
     *            integer
     * @param orientation
     *            the orientation of the row content.
     */
    public void setOrientation(final int columnIndex, final int orientation) {

        assert (columnIndex >= 0 && columnIndex <= numColumns);
        switch (orientation) {
        case RIGHT:
            orientations[columnIndex] = RIGHT;
            break;
        case CENTER:
            orientations[columnIndex] = CENTER;
            break;
        case LEFT:
        default:
            orientations[columnIndex] = LEFT;
        }
    }

    /**
     * returns the global column width at index columnIndex.
     * 
     * @param columnIndex
     *            the index of the column for which to fetch the width.
     * @return the width (in number of chars) for the column index.
     */
    private int getColumnWidth(final int columnIndex) {

        assert (columnIndex < numColumns && columnIndex >= 0);
        return columnLengths[columnIndex];
    }

    /**
     * performs an update on the column lengths.
     */
    private void updateColumnWidth(final int index, final int newSize) {

        columnLengths[index] = Math.max(columnLengths[index], newSize);
    }

    /**
     * returns the actual number of columns this table has got.
     * 
     * @return the number of columns the table may contain.
     */
    public int numColumns() {
        return numColumns;
    }

    /**
     * returns the row width. this needs some refactoring.
     */
    private int getRowWidth() {

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
     * adds a header.
     * 
     * @param title
     *            the text to display within the header
     */
    public void addHeader(final String title) {

        addHeader(title, '*', NiceTable.LEFT);
    }

    /**
     * adds a header row to the table. can be used to give a table some title.
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
    public void addHeader(
            final String title, final char mark, final int orientation) {

        Header h = new Header(title, mark, orientation, this);
        rows.add(h);
    }

    /**
     * adds the data as contents of a new row.
     * 
     * @param data
     *            the data to add.
     */
    public void addRow(final Object[] data) {
        Row myRow = new Row(numColumns, this);
        rows.add(myRow);
        myRow.setColumnData(data);
    }

    /**
     * tests whether any string contains a newline symbol.
     * 
     * @param data
     *            the array to check.
     * @return whether any of the strings contains a newline symbol.
     */
    public boolean anyStringContainsNewLine(final String[] data) {

        for (int i = 0; i < data.length; i++) {
            if (NiceTable.Util.containsNewlines(data[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * adds a string row. checks that the strings added do not contain newlines,
     * because if so, it has to split them in order to make them fit the row.
     * 
     * @param data
     *            the array of data.
     */
    public void addRow(final String[] data) {
        if (anyStringContainsNewLine(data)) {
            String[][] theMatrix = NiceTable.Util.createMatrix(data);
            for (int i = 0; i < theMatrix.length; i++) {
                addRow(theMatrix[i]);
            }
        } else {
            Row myRow = new Row(numColumns, this);
            myRow.setColumnData(data);
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
    public void addLine(final char fill) {

        rows.add(new DynamicLine(fill, this));
    }

    /**
     * allows the setting of other data than Objects.
     * 
     * @param data
     *            the data to display.
     */
    public void addRow(final long[] data) {

        Row myRow = new Row(numColumns, this);
        rows.add(myRow);
        myRow.setColumnData(data);
    }

    /**
     * adds a row with data in it.
     * 
     * @param data
     *            the data to display.
     */
    public void addRow(final double[] data) {

        Row myRow = new Row(numColumns, this);
        rows.add(myRow);
        myRow.setColumnData(data);
    }

    private int getTotalWidth() {

        return this.getRowWidth() + MAGIC_THREE * numColumns() + 1;

    }

    /**
     * the main method doing the work. draws everyting.
     * 
     * @return the formatted table.
     */
    @Override
    public String toString() {

        String s = "";

        for (int i = 0; i < rows.size(); i++) {
            Drawable myObj = (Drawable) rows.get(i);
            s += myObj.draw();
        }
        return s;
    }

    /**
     * an interface for drawable items.
     * 
     * @author axo
     * @since 2005-10-21
     */
    private interface Drawable {

        abstract String draw();

        /**
         * returns the minimum width required to display the contents. this has
         * to be fetched from all rows at display time.
         */
        abstract int minWidth();
    }

    /**
     * a header row is like a <th>element in HTML with colspan =
     * totalNumberOfColumns. since we cannot do colspan right now, we'll have to
     * make it some other way.
     * 
     * @author axo
     * @since 21.10.2005
     */
    private final class Header implements Drawable {

        private NiceTable table;

        private String title;

        private char enclosing;

        private int orientation;

        /**
         * the constructor.
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
                final int theOrientation, final NiceTable theTable) {

            this.orientation = theOrientation;
            this.enclosing = theEnclosing;
            this.table = theTable;
            this.title =
                    enclosing + SPACE + contentToDisplay + SPACE + enclosing;
        }

        /**
         * draws itself.
         */
        public String draw() {

            return theBORDER
                    + Util.pad(
                            title, enclosing, table.getTotalWidth() - 2,
                            orientation)
                    + theBORDER
                    + NEWLINE;
        }

        /**
         * returns the minimum width this one requires as a row. NiceTable needs
         * this in order to compute the row's total width.
         */
        public int minWidth() {

            return title.length();
        }
    }

    /**
     * a dynamic, extensible line.
     * 
     * @author axo
     * @since 2005-10-20
     */
    private final class DynamicLine implements Drawable {

        private char content;

        private NiceTable table;

        /**
         * a dynamic line which draws itself.
         */
        private DynamicLine(final char theContent, final NiceTable theTable) {

            this.content = theContent;
            this.table = theTable;
        }

        /**
         * draws itself.
         */
        public String draw() {

            return Util.repeat("" + content, table.getTotalWidth()) + NEWLINE;
        }

        /**
         * the minimum width of a line is either 0 or 1 i have decided that it
         * should be 0.
         */
        public int minWidth() {

            return 0;
        }
    }

    /**
     * a table's row contains the data, keeps a reference to its parent and
     * computes its own width.
     * 
     * @author axo
     * @since 2005-10-20
     */
    public final class Row implements Drawable {

        /**
         * the data, converted to string.
         */
        private String[] data;

        /**
         * a reference to the parent.
         */
        private NiceTable table;

        /**
         * the number of columns.
         */
        // private final int numColumns;
        /**
         * @param numberOfColumns
         * @param myParent
         *            the parent pointer.
         */
        private Row(final int numberOfColumns, final NiceTable myParent) {

            this.table = myParent;
            data = new String[numberOfColumns];
            // this.numColumns = numberOfColumns;
        }

        /**
         * returns the row's total width.
         */
        private final int getRowWidth() {

            int k = 0;
            for (int i = 0; i < data.length; i++) {
                k += table.getColumnWidth(i);
            }
            return k;
        }

        /**
         * sets the column data to new values. take care that the inserted array
         * has to have the same size as the available columns.
         * 
         * @param d
         *            the data
         */
        public final void setColumnData(final Object[] d) {

            assert (d.length == data.length);

            for (int i = 0; i < data.length; i++) {

                if (d[i].toString().matches("\n")) {
                    System.err.println(d[i]);
                }
                table.updateColumnWidth(i, d[i].toString().length());
                data[i] = d[i].toString().trim();
            }
        }

        /**
         * this is soooo redundant and i don't like it. but i need it. merde.
         * tell me if there's a better way to do this.
         * 
         * @param d
         *            the data
         */
        public final void setColumnData(final double[] d) {

            assert (d.length == data.length);
            String[] str = new String[d.length];
            for (int i = 0; i < data.length; i++) {
                str[i] = "" + (d[i]);
            }
            setColumnData(str);
        }

        /**
         * this also i do not like. but i don't know how to make it better right
         * now, and it works.
         * 
         * @param d
         *            the data
         */
        public final void setColumnData(final long[] d) {

            assert (d.length == data.length);
            String[] str = new String[d.length];
            for (int i = 0; i < data.length; i++) {
                str[i] = "" + (d[i]);
            }
            setColumnData(str);
        }

        /**
         * to string implementation. computes and formats everyting.
         * 
         * @return the result
         */
        public String draw() {

            String[] row = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                row[i] =
                        Util.pad(data[i], ' ', table.getColumnWidth(i), table
                                .getOrientation(i));

            }
            return theBORDER
                    + SPACE
                    + Util.implode(SPACE + theBORDER + SPACE, row)
                    + SPACE
                    + theBORDER
                    + NEWLINE;
        }

        /**
         * computes the own minimum width.
         * 
         * @return the minimum width.
         */
        public int minWidth() {
            return 0;
        }
    }

    /**
     * utilities for the ascii table.
     * 
     * @author axo
     * @since 2005-10-20
     */
    public static final class Util {

        /**
         * defines the NEWLINE character until i get it from the environment.
         */
        public static final char NEWLINE = '\n';

        /**
         * private constructor.
         */
        private Util() {
            // do nothing.
        }

        /**
         * this is a nice one. i am not sure if pad is the correct name for this
         * method, and i accept new proposals. an example will show what this
         * one does.
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
        public static String pad(
                final String data, final char doPadWithThis,
                final int totalWidth, final int orientation) {

            String pad =
                    repeat("" + doPadWithThis, Math.max(0, totalWidth
                            - data.length()));
            int padLength = pad.length();
            if (orientation == CENTER) {
                return pad.substring(0, padLength / 2)
                        + data
                        + pad.substring(padLength / 2, padLength);
            }
            return (orientation == RIGHT) ? pad + data : data + pad;
        }

        /**
         * concantenate a String array "what" with glue "glue".
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
        public static String implode(final String glue, final String[] what) {

            String s = "";
            for (int i = 0; i < what.length; i++) {
                s += what[i];
                if (i + 1 != what.length) {
                    s += glue;
                }
            }
            return s;
        }

        /**
         * @param glue
         *            glue
         * @param what
         *            the list to print.
         * @return string.
         */
        public static String implode(final String glue, final ArrayList what) {
            String[] th = new String[what.size()];
            for (int i = 0; i < what.size(); i++) {
                th[i] = what.get(i).toString();
            }
            return Util.implode(glue, th);
        }

        /**
         * @param glue
         *            the string to implode with
         * @param what
         *            the object array to display.
         * @return string.
         */
        public static String implode(final String glue, final Method[] what) {
            String[] result = new String[what.length];
            for (int i = 0; i < what.length; i++) {
                result[i] = what[i].getName();
            }
            return Util.implode(glue, result);
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
        public static String repeat(final String s, final int numTimes) {
            String str = "";

            for (int i = 0; i < numTimes; i++, str += s) {
                // yes, it's an empty implementation.
            }
            return str;
        }

        /**
         * there has to be a utility for this, but i could not find it, i am
         * very sorry to say. if anyone finds the utility, tell me and it'll be
         * factored out in no time.
         * 
         * @param glue
         *            bla
         * @param what
         *            bla
         * @return the result
         */
        public static String implode(final String glue, final double[] what) {

            String s = "";
            for (int i = 0; i < what.length; i++) {
                s += what[i];
                if (i + 1 != what.length) {
                    s += glue;
                }
            }
            return s;
        }

        /**
         * splits a string.
         * 
         * @return an array
         * @param ch
         *            bla
         * @param s
         *            bla
         */
        public static String[] explode(final char ch, final String s) {
            return s.split("\\" + ch);
        }

        /**
         * returns how many new lines are in the string.
         * 
         * @param s
         *            the string to look upon.
         * @return the number of occurences of NEWLINE in the string.
         */
        public static int numNewLines(final String s) {
            char[] arr = s.toCharArray();
            int result = 0;
            for (char ch : arr) {
                if (Util.NEWLINE == ch) {
                    result++;
                }
            }
            return result;
        }

        /**
         * tells us whether the string contains newlines. it's important to note
         * that only newlines within the string are important, not the newlines
         * at the front or the end of the string.
         */
        private static boolean containsNewlines(final String s) {
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
        public static String[][] createMatrix(final String[] data) {
            int maxNewLines = 0;
            for (String col : data) {
                maxNewLines = Math.max(maxNewLines, Util.numNewLines(col));
            }
            String[][] matrix = new String[maxNewLines + 1][data.length];
            for (int col = 0; col < data.length; col++) {
                String[] my = Util.explode('\n', data[col]);
                for (int row = 0; row < maxNewLines + 1; row++) {
                    matrix[row][col] = (my.length > row) ? my[row] : "";
                }
            }

            return matrix;

        }

    }

    /**
     * sets the column delimiter, in order to allow different delimiters for
     * different contexts.
     * 
     * @param c
     *            the border string.
     */
    public void setColumnDelimiter(final String c) {
        theBORDER = c;
    }

}
