/*
 * Copyright 2008 Distributed Systems Group, University of Konstanz
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
 * $Revision$
 * $Author$
 * $Date$
 *
 */
package org.perfidix.output.asciitable;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.perfidix.ouput.asciitable.NiceTable;
import org.perfidix.ouput.asciitable.AbstractTabularComponent.Alignment;

/**
 * Test class for the NiceTable.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public class NiceTableTest {

    private NiceTable table;
    private final static int COLUMNNUMBER = 10;

    /**
     * Simple setUp.
     */
    @Before
    public void setUp() {
        table = new NiceTable(COLUMNNUMBER);
    }

    /**
     *Simple tearDown
     */
    @After
    public void tearDown() {
        table = null;
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#NiceTable(int)}.
     */
    @Test
    public void testCreate() {
        assertEquals("", table.toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#addHeader(java.lang.String)}
     * .
     */
    @Test
    public void testAddHeaderString() {
        table.addHeader("This is a test");
        assertEquals("|= This is a test =============|\n", table
                .toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#addHeader(java.lang.String, char, org.perfidix.ouput.asciitable.NiceTable.Alignment)}
     * .
     */
    @Test
    public void testAddHeaderStringCharAlignment() {
        table.addHeader("This is a test", '-', Alignment.Left);
        assertEquals("|- This is a test -------------|\n", table
                .toString());

        tearDown();
        setUp();

        table.addHeader("This is a test", '/', Alignment.Center);
        assertEquals("|/////// This is a test ///////|\n", table
                .toString());

        tearDown();
        setUp();

        table.addHeader("This is a test", '\\', Alignment.Right);
        assertEquals(
                "|\\\\\\\\\\\\\\\\\\\\\\\\\\ This is a test \\|\n", table
                        .toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#addRow(java.lang.String[])}
     * .
     */
    @Test
    public void testAddRow() {
        final String[] data = { "this", "is", "a", "test" };
        table.addRow(data);
        assertEquals("| this | is | a | test |\n", table.toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#addLine(char)}.
     */
    @Test
    public void testAddLine() {
        table.addLine('*');
        assertEquals("|******************************|\n", table
                .toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#toString()}.
     */
    @Test
    public void testToString() {
        table.addHeader("This is a header");
        table.addRow(new String[] { "This", "is", "one", "data" });
        table.addLine('-');
        table.addRow(new String[] { "This", "is", "another", "data" });
        assertEquals(
                "|= This is a header ===========================|\n| This | is | one     | data |\n|----------------------------------------------|\n| This | is | another | data |\n",
                table.toString());
    }

    /**
     * Recursive usage of multiple tables
     */
    @Test
    public void testNestedTable() {

        final NiceTable t = new NiceTable(3);
        final NiceTable one = new NiceTable(2);
        final NiceTable two = new NiceTable(2);
        final NiceTable three = new NiceTable(2);
        one.addRow(new String[] { "a", "b" });
        two.addRow(new String[] { "c", "d" });
        three.addRow(new String[] { "e", "f" });
        t.addRow(new String[] {
                one.toString(), two.toString(), three.toString() });

        final String result = t.toString().trim();
        final String expected = "| | a | b |  | | c | d |  | | e | f |  |";
        assertEquals(expected, result);
    }

    /**
     * Alignement test if some rows are incomplete
     */
    @Test
    public void testRowAlignment() {
        final NiceTable a = new NiceTable(2);
        a.addRow(new String[] { "a\nb\nc", "a\nb" });
        a.addRow(new String[] { "d", "d" });

        final String expected =
                "| a | a |\n"
                        + "| b | b |\n"
                        + "| c |   |\n"
                        + "| d | d |\n";
        assertEquals(expected, a.toString());
    }

    /**
     * Test dynamic row insertion.
     */
    @Test
    public void testDynamics() {

        final String[] s = { "a", "b", "c", "d", "e" };
        final Number[] d = { 1.222222222, 3.000, 4, 5.0, 4444 };

        final NiceTable b = new NiceTable(s.length);
        b.addLine('-');
        b.addRow(s);
        b.addLine('=');
        b.addRow(new String[] {
                d[0].toString(), d[1].toString(), d[2].toString(),
                d[3].toString(), d[4].toString() });
        b.addLine('-');
        final String result = b.toString();
        final StringBuilder expected = new StringBuilder();
        expected.append("|------------------------------------|\n");
        expected.append("| a           | b   | c | d   | e    |\n");
        expected.append("|====================================|\n");
        expected.append("| 1.222222222 | 3.0 | 4 | 5.0 | 4444 |\n");
        expected.append("|------------------------------------|\n");
        assertEquals(expected.toString(), result);

    }
}
