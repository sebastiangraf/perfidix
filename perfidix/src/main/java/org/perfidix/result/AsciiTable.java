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

package org.perfidix.result;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Hashtable;

import org.perfidix.IMeter;

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
 * @see Result
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
    private void addHeader(final ResultContainer res) {
        if (res instanceof BenchmarkResult) {
            table.addHeader(res.getName().toUpperCase());
            table.addLine('*');
            table.addRow(header);
            table.addLine('=');
            return;
        }
        if (res instanceof ClassResult) {
            table.addHeader(" ", ' ', NiceTable.LEFT);
            table.addHeader(res.getName(), '.', NiceTable.LEFT);
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
    private void createSummary(final IMeter m, final ResultContainer res) {
        Formatter j = new Formatter();
        Object[] data =
                {
                        " ",
                        m.getUnit(),
                        res.sum(m),
                        res.min(m),
                        res.max(m),
                        format(res.avg(m)),
                        format(res.getStandardDeviation(m)),
                        j.format(
                                "[" + floatFormat + "," + floatFormat + "]",
                                Math.max(0, res.avg(m) - res.getConf95(m)), res
                                        .avg(m)
                                        + res.getConf95(m)),
                        res.getNumberOfRuns(),

                };
        table.addRow(data);
    }

    private void addFooter(final ResultContainer res) {
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
                indent + "summary for " + res.getName() + indent, whichChar,
                NiceTable.LEFT);

        for (Object meter : res.getRegisteredMeters()) {
            createSummary((IMeter) meter, res);
        }

        if (res instanceof BenchmarkResult) {
            table.addLine('=');
        } else if (res instanceof ClassResult) {
            table.addLine('_');
        } else {
            // do nothing.
        }

    }

    private void visitSubcontainers(final ResultContainer res) {
        addHeader(res);
        for (Object res2 : res.getChildren()) {
            visit((Result) res2);
        }
        addFooter(res);
    }

    /**
     * visits the method results. the this is a
     * 
     * @param res
     */
    private void visitMethodResults(final MethodResult res) {

        final Hashtable<IMeter, ArrayList<SingleResult>> customChild =
                res.getCustomChildren();

        String theName;
        if (customChild.size() > 1) {
            table.addHeader("" + res.getName(), ' ', NiceTable.LEFT);
            theName = "`";
        } else {
            theName = res.getName();
        }

        for (final IMeter meter : customChild.keySet()) {

            for (final SingleResult singRes : customChild.get(meter)) {
                visitSingleResult(singRes, theName);
            }
        }
    }

    private void visitSingleResult(
            final SingleResult res, final String nameToDisplay) {

        if (res.getResultSet().length < 1) {
            return;
        }

        Object[] data =
                {
                        nameToDisplay,
                        res.getUnit(),
                        res.sum(),
                        res.min(),
                        res.max(),
                        format(res.avg()),
                        format(res.getStandardDeviation()),
                        "["
                                + format(getConf95Min(res))
                                + ","
                                + format(getConf95Max(res))
                                + "]", res.getNumberOfRuns() };
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
    public void visit(final IResult res) {

        if (res instanceof SingleResult) {
            visitSingleResult((SingleResult) res, res.getName());
        } else if (res instanceof MethodResult) {
            visitMethodResults((MethodResult) res);
        } else {
            visitSubcontainers((ResultContainer) res);
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
