/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of Konstanz nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
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
package org.perfidix.output.asciitable;

import static org.junit.Assert.assertEquals;

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

    private transient NiceTable table;
    private final static int COLUMNNUMBER = 10;
    private final static String TESTSTRING = "This is a test";

    /**
     * Simple setUp.
     */
    @Before
    public void setUp() {
        table = new NiceTable(COLUMNNUMBER);
    }

    /**
     * Test method for {@link org.perfidix.ouput.asciitable.NiceTable#NiceTable(int)}.
     */
    @Test
    public void testCreate() {
        assertEquals("Test for create", "", table.toString());
    }

    /**
     * Test method for {@link org.perfidix.ouput.asciitable.NiceTable#addHeader(java.lang.String)} .
     */
    @Test
    public void testAddHeaderString() {
        table.addHeader(TESTSTRING);
        assertEquals("Test for normal adding", "|= This is a test =============|\n", table.toString());
    }

    /**
     * Test method for
     * {@link org.perfidix.ouput.asciitable.NiceTable#addHeader(String, char, org.perfidix.ouput.asciitable.AbstractTabularComponent.Alignment)}
     */
    @Test
    public void testAddHeaderStringCharAlignment() {
        table.addHeader(TESTSTRING, '-', Alignment.Left);
        assertEquals("Test for left alignement adding", "|- This is a test -------------|\n", table
            .toString());

        setUp();

        table.addHeader(TESTSTRING, '/', Alignment.Center);
        assertEquals("Test for center alignment", "|/////// This is a test ///////|\n", table.toString());

        setUp();

        table.addHeader(TESTSTRING, '\\', Alignment.Right);
        assertEquals("Test for right alignment", "|\\\\\\\\\\\\\\\\\\\\\\\\\\ This is a test \\|\n", table
            .toString());
    }

    /**
     * Test method for {@link org.perfidix.ouput.asciitable.NiceTable#addRow(java.lang.String[])} .
     */
    @Test
    public void testAddRow() {
        final String[] data = {
            "this", "is", "a", "test"
        };
        table.addRow(data);
        assertEquals("Test for | delim", "| this | is | a | test |\n", table.toString());
    }

    /**
     * Test method for {@link org.perfidix.ouput.asciitable.NiceTable#addLine(char)}.
     */
    @Test
    public void testAddLine() {
        table.addLine('*');
        assertEquals("Test for adding a line", "|******************************|\n", table.toString());
    }

    /**
     * Test method for {@link org.perfidix.ouput.asciitable.NiceTable#toString()}.
     */
    @Test
    public void testToString() {
        table.addHeader("This is a header");
        table.addRow(new String[] {
            "This", "is", "one", "data"
        });
        table.addLine('-');
        table.addRow(new String[] {
            "This", "is", "another", "data"
        });
        assertEquals(
            "Test for a complete table",
            "|= This is a header ===========================|\n| This | is | one     | data |\n|----------------------------------------------|\n| This | is | another | data |\n",
            table.toString());
    }

    /**
     * Recursive usage of multiple tables
     */
    @Test
    public void testNestedTable() {

        final NiceTable zero = new NiceTable(3);
        final NiceTable one = new NiceTable(2);
        final NiceTable two = new NiceTable(2);
        final NiceTable three = new NiceTable(2);
        one.addRow(new String[] {
            "a", "b"
        });
        two.addRow(new String[] {
            "c", "d"
        });
        three.addRow(new String[] {
            "e", "f"
        });
        zero.addRow(new String[] {
            one.toString(), two.toString(), three.toString()
        });

        final String result = zero.toString().trim();
        assertEquals("Test for encapsulated tables", "| | a | b |  | | c | d |  | | e | f |  |", result);
    }

    /**
     * Alignement test if some rows are incomplete
     */
    @Test
    public void testRowAlignment() {
        final NiceTable zero = new NiceTable(2);
        zero.addRow(new String[] {
            "a\nb\nc", "a\nb"
        });
        zero.addRow(new String[] {
            "d", "d"
        });

        final String expected = "| a | a |\n" + "| b | b |\n" + "| c |   |\n" + "| d | d |\n";
        assertEquals("Test for row alignment", expected, zero.toString());
    }

    /**
     * Test dynamic row insertion.
     */
    @Test
    public void testDynamics() {

        final String[] string = {
            "a", "b", "c", "d", "e"
        };
        final Number[] numbers = {
            1.222222222, 3.000, 4, 5.0, 4444
        };

        final NiceTable zero = new NiceTable(string.length);
        zero.addLine('-');
        zero.addRow(string);
        zero.addLine('=');
        zero.addRow(new String[] {
            numbers[0].toString(), numbers[1].toString(), numbers[2].toString(), numbers[3].toString(),
            numbers[4].toString()
        });
        zero.addLine('-');
        final String result = zero.toString();
        final StringBuilder expected = new StringBuilder();
        expected.append("|------------------------------------|\n");
        expected.append("| a           | b   | c | d   | e    |\n");
        expected.append("|====================================|\n");
        expected.append("| 1.222222222 | 3.0 | 4 | 5.0 | 4444 |\n");
        expected.append("|------------------------------------|\n");
        assertEquals("Another test for a complete table", expected.toString(), result);

    }
}
