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


/**
 * Utilities for the ascii table.
 */
public final class Util {

    /**
     * private constructor.
     */
    private Util () {
        // do nothing.
    }

    /**
     * Combines an unknown number of Strings to one String.
     * 
     * @param args multiple Strings
     * @return the combined string
     */
    protected static String combine (final String... args) {
        final StringBuilder builder = new StringBuilder();
        for (final String arg : args) {
            builder.append(arg);
        }
        return builder.toString();
    }

    /**
     * This method fills one char either on the right site of a given string or on the left site or on both sites. Refer
     * to the example below.
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
     * @param doPadWithThis the string to pad the string with
     * @param data the data to pad
     * @param orientation which orientation to take
     * @param totalWidth the total width of the result string
     */
    protected static String pad (final String data, final char doPadWithThis, final int totalWidth, final Alignment orientation) {

        final String pad = repeat(new String(new char[] { doPadWithThis }), Math.max(0, totalWidth - data.length()));
        String returnVal = "";
        if (orientation == null) {
            returnVal = new StringBuilder(data).append(pad).toString();
        } else {
            switch (orientation) {
                case Center :
                    returnVal = pad.substring(0, pad.length() / 2) + data + pad.substring(pad.length() / 2, pad.length());
                    break;
                case Right :
                    returnVal = new StringBuilder(pad).append(data).toString();
                    break;
                default :
                    returnVal = new StringBuilder(data).append(pad).toString();

            }
        }
        return returnVal;
    }

    /**
     * Concantenate a String array "what" with glue "glue".
     * 
     * @param what the array of strings to concantenate
     * @param glue the glue string to use.
     * 
     *            <pre>
     * String[] what = { &quot;a&quot;, &quot;b&quot;, &quot;c&quot; };
     * 
     * String s = Util.implode(&quot;-&quot;, what);
     * // result is &quot;a-b-c&quot;
     * </pre>
     * @return String
     */
    protected static String implode (final String glue, final String[] what) {

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < what.length; i++) {
            builder.append(what[i]);
            if (i + 1 != what.length) {
                builder.append(glue);
            }
        }
        return builder.toString();
    }

    /**
     * a str_repeat function.
     * 
     * @param toBeRepeated the string to repeat
     * @param numTimes how many times to concantenate the string
     * @return the repeated string.
     */
    protected static String repeat (final String toBeRepeated, final int numTimes) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < numTimes; i++) {
            builder.append(toBeRepeated);
        }
        return builder.toString();
    }

    /**
     * Splits a string with the help of a given char.
     * 
     * @return an array of elements
     * @param splitter the separator char
     * @param toBeSplitted the string to be splitted
     */
    private static String[] explode (final char splitter, final String toBeSplitted) {
        return toBeSplitted.split("\\" + splitter);
    }

    /**
     * Returns how many new lines are in the string.
     * 
     * @param toExamine the string to look upon.
     * @return the number of occurences of {@link NiceTable#NEWLINE} in the string.
     */
    private static int numNewLines (final String toExamine) {
        final char[] arr = toExamine.toCharArray();
        int result = 0;
        for (char ch : arr) {
            if (AbstractTabularComponent.NEWLINE.equals(new String(new char[] { ch }))) {
                result++;
            }
        }
        return result;
    }

    /**
     * Tells us whether the string contains newlines. it's important to note that only newlines within the string are
     * important, not the newlines at the front or the end of the string.
     * 
     * @param toExamine to be checked
     * @return boolean if the string contains newlines.
     */
    public static boolean containsNewlines (final String toExamine) {
        return toExamine.trim().contains(AbstractTabularComponent.NEWLINE);
    }

    /**
     * Creates a matrix according to the number of new lines given into the method.
     * 
     * @return the matrix.
     * @param data an array of row data
     */
    public static String[][] createMatrix (final String[] data) {
        int maxNewLines = 0;
        for (final String col : data) {
            maxNewLines = Math.max(maxNewLines, Util.numNewLines(col));
        }
        final String[][] matrix = new String[maxNewLines + 1][data.length];
        for (int col = 0; col < data.length; col++) {
            final String[] exploded = Util.explode('\n', data[col]);
            for (int row = 0; row < maxNewLines + 1; row++) {
                if (exploded.length > row) {
                    matrix[row][col] = exploded[row];
                } else {
                    matrix[row][col] = "";
                }
            }
        }

        return matrix;

    }

}
