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
 * $Id: AsciiTableTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.junit.Test;

public class AsciiTableTest extends PerfidixTest {

    /**
     * tests that the rows are aligned properly.
     */
    @Test
    public void testRowAlignment() {
        NiceTable a = new NiceTable(2);
        a.addRow(new String[] { "a\nb\nc", "a\nb" });
        a.addRow(new String[] { "d", "d" });

        getLog().info("testRowAlignment start ...");
        getLog().info(a.toString());
        getLog().info("testRowAlignment finished. ...");

        String expected =
                "| a | a |\n" + "| b | b |\n" + "| c |   |\n" + "| d | d |\n";
        assertEquals(expected, a.toString());
    }

    @Test
    public void testCreate() {

        try {
            NiceTable a = new NiceTable(2);
            getLog().info(a.toString());
            assertEquals("", a.toString());
        } catch (Exception e) {
            fail("should not reach here.");
        }

    }

    /**
     * allows the insertion of more than one table.
     */
    @Test
    public void testNestedTable() {

        NiceTable t = new NiceTable(3);
        NiceTable one = new NiceTable(2);
        NiceTable two = new NiceTable(2);
        NiceTable three = new NiceTable(2);
        one.addRow(new String[] { "a", "b" });
        two.addRow(new String[] { "c", "d" });
        three.addRow(new String[] { "e", "f" });
        t.setColumnDelimiter("-");
        t.addRow(new Object[] { one, two, three });

        String result = t.toString().trim();
        String expected = "- | a | b |  - | c | d |  - | e | f |  -";
        assertEquals(expected, result);
    }

    @Test
    public void testAddHeaderCenter() {

        NiceTable ding = new NiceTable(3);
        long[] data = { 1, 2, 3 };
        ding.addHeader("h", '=', NiceTable.CENTER);
        ding.addRow(data);
        String result = ding.toString();
        getLog().info(result);
        String expected = "";
        expected += "|==== h ====|\n";
        expected += "| 1 | 2 | 3 |\n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddHeaderRight() {

        NiceTable ding = new NiceTable(3);
        long[] data = { 1, 2, 3 };
        ding.addHeader("h", '=', NiceTable.RIGHT);
        ding.addRow(data);
        String result = ding.toString();
        getLog().info(result);
        String expected = "";
        expected += "|======= h =|\n";
        expected += "| 1 | 2 | 3 |\n";
        assertEquals(expected, result);
    }

    @Test
    public void testAddHeaderLeft() {

        NiceTable ding = new NiceTable(3);
        long[] data = { 1, 2, 3 };
        ding.addHeader("h", '=', NiceTable.LEFT);
        ding.addRow(data);
        String result = ding.toString();
        getLog().info(result);
        String expected = "";
        expected += "|= h =======|\n";
        expected += "| 1 | 2 | 3 |\n";
        assertEquals(expected, result);
    }

    @Test
    public void testImplode() {

        String[] s = { "a", "b" };
        assertEquals("a-b", NiceTable.Util.implode("-", s));
    }

    @Test
    public void testRepeat() {

        assertEquals("aaa", NiceTable.Util.repeat("a", 3));
        assertEquals("a", NiceTable.Util.repeat("a", 1));
        assertEquals("", NiceTable.Util.repeat("a", 0));
        assertEquals("", NiceTable.Util.repeat("a", -1));

    }

    /**
     * tests whether the orientation parameters for the asciiTable work how they
     * should.
     */
    @Test
    public void testOrientation() {

        String[] q = { "aa", "a" };
        String[] r = { "a", "aa" };
        String[] s = { "a", "a" };
        NiceTable t = new NiceTable(2);
        t.addRow(q);
        t.addRow(r);
        t.addRow(s);
        t.setOrientation(0, NiceTable.LEFT);
        t.setOrientation(1, NiceTable.RIGHT);
        String expected = "";
        expected += "| aa |  a |\n";
        expected += "| a  | aa |\n";
        expected += "| a  |  a |\n";
        getLog().info(t.toString());
        assertEquals(expected, t.toString());
    }

    @Test
    public void testOrientationCenter() {

        String[] a = { "a" };
        String[] b = { "aaa" };
        String[] c = { "aaaaa" };

        NiceTable t = new NiceTable(1);
        t.addRow(a);
        t.addRow(b);
        t.addRow(c);
        t.setOrientation(0, NiceTable.CENTER);
        String expected = "";
        expected += "|   a   |\n";
        expected += "|  aaa  |\n";
        expected += "| aaaaa |\n";

        getLog().info(t.toString());

        assertEquals(expected, t.toString());

    }

    @Test
    public void testDynamics() {

        String[] s = { "a", "b", "c", "d", "e" };
        Number[] d = { 1.222222222, 3.000, 4, 5.0, 4444 };

        NiceTable b = new NiceTable(s.length);
        b.addLine('-');
        b.addRow(s);
        b.addLine('=');
        b.addRow(d);
        b.addLine('-');
        String result = b.toString();
        getLog().info(result);
        String expected = "";
        expected += "--------------------------------------\n";
        expected += "| a           | b   | c | d   | e    |\n";
        expected += "======================================\n";
        expected += "| 1.222222222 | 3.0 | 4 | 5.0 | 4444 |\n";
        expected += "--------------------------------------\n";
        assertEquals(expected, result);

    }

    @Test
    public void testTH() {

        String[] s = { "a", "b", "c", "d" };
        NiceTable b = new NiceTable(4);

        String expected = "| a | b | c | d |\n=================\n";
        b.addRow(s);
        b.addLine('=');
        getLog().info(b.toString());
        assertEquals(expected, b.toString());

        String[] s2 = { "aa", "b", "c", "d" };
        b.addRow(s2);
        b.addLine('=');
        String expected2 = "| a  | b | c | d |\n==================\n";
        expected2 += "| aa | b | c | d |\n==================\n";
        assertEquals(expected2, b.toString());
        getLog().info(b.toString());
        Number[] doubles = { 1, 2, 3.9, 5555 };
        b.addRow(doubles);
        b.addRow(doubles);
        b.addRow(doubles);
        getLog().info(b.toString());

    }

    /**
     * assures that the AnyStringContainsNewline method works as it should.
     */
    @Test
    public void testAnyStringContainsNewline() {
        NiceTable t = new NiceTable(0);
        String[] s = { "a\n" };
        assertFalse(t.anyStringContainsNewLine(s));

        String[] s2 = { "a\nb" };
        assertTrue(t.anyStringContainsNewLine(s2));
        String[] s3 = { "a", "b" };
        assertFalse(t.anyStringContainsNewLine(s3));
        String[] s4 = { "a", "b\nc" };
        assertTrue(t.anyStringContainsNewLine(s4));
    }

    /**
     * tests the creation of a string matrix.
     */
    @Test
    public void testCreateMatrixAgain() {
        String[] s = { "a\nb", "a" };
        String[][] exp = { { "a", "a" }, { "b", "" }, };
        String[][] result = NiceTable.Util.createMatrix(s);
        try {
            for (int i = 0; i < result.length; i++) {
                for (int k = 0; k < result[i].length; k++) {
                    assertEquals(0, exp[i][k].compareTo(result[i][k]));
                }
            }
        } catch (Exception e) {
            fail("exp != result");
        }

    }

    @Test
    public void testNestedTables1() {

        getLog().info("testing nested tables start ...");
        NiceTable t = new NiceTable(2);
        t.addHeader("ab");
        t.addRow(new String[] { "a\nb", "a" });
        String exp;
        exp = "|* ab **|\n";
        exp += "| a | a |\n";
        exp += "| b |   |\n";

        String myResult = t.toString();
        getLog().info(myResult);
        assertEquals(exp, myResult);

        getLog().info("testing nested tables end ...");

    }

    @Test
    public void testRegex() {

        Pattern p = Pattern.compile("\\n", Pattern.DOTALL);
        String[] res = p.split("a\nb");
        assertEquals(2, res.length);
        assertTrue(res[0].equals("a"));
        assertTrue(res[1].equals("b"));
    }

    @Test
    public void testCreateMatrix() {

        // simplest form of the matrix.
        String[] a = { "a", "b" };
        String[][] result = NiceTable.Util.createMatrix(a);
        assertEquals(1, result.length);
        assertEquals(2, result[0].length);
        assertEquals(0, a[0].compareTo(result[0][0]));
        printMatrix(result);

    }

    /**
     * i need this only for debugging purposes.
     */
    private void printMatrix(final String[][] m) {

        getLog().info("------------------------------");
        for (String[] row : m) {
            getLog().info("");
            for (String col : row) {
                getLog().info(col + ", ");
            }
        }

        getLog().info("------------------------------");
    }

    @Test
    public void testCreateMatrix3() {
        String[] s = { "abc", "\n", "a\nb", "a\nb\nc" };
        int[] r = { 0, 1, 1, 2 };
        for (int i = 0; i < s.length; i++) {
            assertEquals(s[i], r[i], NiceTable.Util.numNewLines(s[i]));
        }

    }

    @Test
    public void testExplode() {
        String s = "hello|world";
        String[] exp = { "hello", "world" };
        String[] res = NiceTable.Util.explode('|', s);
        assertEquals(exp.length, res.length);
        assertEquals(0, exp[0].compareTo(res[0]));
        assertEquals(0, exp[1].compareTo(res[1]));

        String another = "hello\nworld";
        String[] result2 = NiceTable.Util.explode('\n', another);
        assertEquals(exp.length, result2.length);
        assertEquals(0, exp[0].compareTo(result2[0]));
        assertEquals(0, exp[1].compareTo(result2[1]));

    }

    @Test
    public void testCreateMatrix2() {

        // more complicated example.
        String[] b = { "a\nc", "b" };
        String[][] result = NiceTable.Util.createMatrix(b);
        assertEquals(2, result.length);
        assertEquals(2, result[0].length);
        String my = NiceTable.Util.implode(",", result[0]);
        getLog().info(my);
        printMatrix(result);
        assertEquals(0, "a,b".compareTo(my));
        assertEquals(0, "c".compareTo(result[1][0]));
        assertEquals(0, "".compareTo(result[1][1]));
    }

    @Test
    public void testNestedTables() {

        getLog().info("testing ... testNestedTables");
        String[] s = { "a", "b", "c" };
        NiceTable t = new NiceTable(s.length);
        t.addHeader("a");
        t.addRow(s);
        getLog().info(t.toString());
        NiceTable q = new NiceTable(2);
        q.addHeader("nested header ");
        q.addRow(new String[] { new String("rowline"), t.toString() });
        getLog().info(q.toString());
    }

}
