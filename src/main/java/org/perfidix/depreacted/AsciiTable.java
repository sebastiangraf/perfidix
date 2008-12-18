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
 * $Id: AsciiTable.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix.depreacted;

import java.util.Formatter;

import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;
import org.perfidix.result.ResultContainer;

/**
 * <p>
 * This is a ResultVisitor implementation and a small example of the visitor
 * pattern, showing how it's thought to be used within perfidix results.
 * </p>
 * <p>
 * The AsciiTableVisitor parses a Result and displays it on the command line as
 * an ascii table
 * </p>
 * 
 * @see AbstractResult
 * @see ResultVisitor
 * @author axo
 */
public class AsciiTable extends ResultVisitor {

    private NiceTable table;

    private final String[] header =
            {
                    " - ", "unit", "sum", "min", "max", "avg", "stddev",
                    "conf95", "runs", };

    private final int[] alignments =
            {
                    NiceTable.LEFT, NiceTable.RIGHT, NiceTable.RIGHT,
                    NiceTable.RIGHT, NiceTable.RIGHT, NiceTable.RIGHT,
                    NiceTable.RIGHT, NiceTable.CENTER, NiceTable.RIGHT, };

    /**
     * allows formatting of the doubles.
     * 
     * @see java.util.Formatter for valid format strings.
     * @param theFloatFormat
     *            a comment.
     */
    public AsciiTable(final String theFloatFormat) {
        this();
        floatFormat = theFloatFormat;
    }

    /**
     * constructor.
     */
    public AsciiTable() {

        table = new NiceTable(header.length);
        for (int i = 0; i < alignments.length; i++) {
            table.setOrientation(i, alignments[i]);
        }
    }

    /**
     * adds the result container's header to the table.
     * 
     * @param res
     */
    private void addHeader(final ResultContainer<?> res) {
        if (res instanceof BenchmarkResult) {
            table.addHeader("");
            table.addLine('*');
            table.addRow(header);
            table.addLine('=');
            return;
        }
        if (res instanceof ClassResult) {
            table.addHeader(" ", ' ', NiceTable.LEFT);
            table.addHeader("", '.', NiceTable.LEFT);
            return;
        }
        // other result containers don't need a header.
    }

    /**
     * generates a summary.
     * 
     * @param m
     * @param res
     */
    private void createSummary(
            final AbstractMeter m, final ResultContainer<?> res) {
        Formatter j = new Formatter();
        Object[] data =
                {
                        " ",
                        m.getUnit(),
                        res.sum(m),
                        res.min(m),
                        res.max(m),
                        format(res.mean(m)),
                        format(res.getStandardDeviation(m)),
                        j.format(
                                "[" + floatFormat + "," + floatFormat + "]",
                                Math.max(0, res.mean(m) - res.getConf95(m)),
                                res.mean(m) + res.getConf95(m)),
                // res.getResultSet(m).size(),

                };
        table.addRow(data);
    }

    private void addFooter(final ResultContainer<?> res) {
        char whichChar;
        String indent;
        if (res instanceof BenchmarkResult) {
            table.addHeader(" ", ' ', NiceTable.LEFT);
            whichChar = '*';
            indent = "";
        } else if (res instanceof ClassResult) {
            whichChar = '_';
            indent = "  ";
        } else {
            whichChar = ' ';
            indent = "    ";
        }

        table.addHeader(
                indent + "summary for " + "" + indent, whichChar,
                NiceTable.LEFT);

        for (Object meter : res.getRegisteredMeters()) {
            createSummary((AbstractMeter) meter, res);
        }

        if (res instanceof BenchmarkResult) {
            table.addLine('=');
        } else if (res instanceof ClassResult) {
            table.addLine('_');
        } else {
            // do nothing.
        }

    }

    private void visitSubcontainers(final ResultContainer<?> res) {
        addHeader(res);
        // for (Object res2 : res.getChildren()) {
        // visit((AbstractResult) res2);
        // }
        addFooter(res);
    }

    /**
     * visits the method results. the this is a
     * 
     * @param res
     */
    private void visitMethodResults(
            final MethodResult res, final AbstractMeter meter) {
        Object[] data =
                {
                        "",
                        meter.getUnit(),
                        res.sum(meter),
                        res.min(meter),
                        res.max(meter),
                        format(res.mean(meter)),
                        format(res.getStandardDeviation(meter)),
                        "["
                                + format(res.mean(meter) - res.getConf95(meter))
                                + ","
                                + format(res.mean(meter) - res.getConf95(meter))
                /* + "]", res.getResultSet(meter).size() */};
        table.addRow(data);
    }

    /**
     * visits a result. the input parameter "Result" is deprecated, i don't
     * really like to do typecasting within the method, but we'll have to see.
     * 
     * @param res
     *            the result to look visit.
     */
    @Override
    public void visit(final AbstractResult res) {
        for (final AbstractMeter meter : res.getRegisteredMeters()) {
            if (res instanceof MethodResult) {
                visitMethodResults((MethodResult) res, meter);
            } else {
                visitSubcontainers((ResultContainer<?>) res);
            }
        }
    }

    /**
     * returns the output.
     * 
     * @return a string.
     */
    @Override
    public String toString() {
        return table.toString();
    }

}
