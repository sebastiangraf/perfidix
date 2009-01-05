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
package org.perfidix.ouput;

import java.io.PrintStream;

import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.NiceTable.Alignment;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Summary output using the {@link NiceTable} to format. Just giving an overview
 * of statistical analysis over the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class TabluarSummaryVisitor extends ResultVisitor {

    /** Print stream where the result should end */
    private final PrintStream out;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramOut
     *            an {@link PrintStream} to pipe to.
     */
    public TabluarSummaryVisitor(final PrintStream paramOut) {
        out = paramOut;
    }

    /**
     * Constructor, just giving out on the {@link System#out}.
     */
    public TabluarSummaryVisitor() {
        this(System.out);
    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(final BenchmarkResult benchRes) {
        NiceTable table = new NiceTable(9);
        table = generateHeader(table);
        for (final AbstractMeter meter : benchRes.getRegisteredMeters()) {
            table.addHeader(meter.getName(), '=', Alignment.Center);
            for (final ClassResult classRes : benchRes.getIncludedResults()) {
                table.addHeader(classRes.getElementName(), '.', Alignment.Left);
                for (final MethodResult methRes : classRes.getIncludedResults()) {
                    table =
                            generateMeterResult(
                                    methRes.getElementName(), meter, methRes,
                                    table);
                }
                table.addHeader(
                        new StringBuilder("Summary for ").append(
                                classRes.getElementName()).toString(), '_',
                        Alignment.Left);
                table = generateMeterResult("", meter, classRes, table);
                table.addLine('-');
            }
        }
        table.addHeader(
                "Summary for the whole benchmark", '*', Alignment.Center);
        for (final AbstractMeter meter : benchRes.getRegisteredMeters()) {
            table = generateMeterResult("", meter, benchRes, table);
        }
        table.addLine('=');
        out.println(table.toString());
    }

    private final NiceTable generateMeterResult(
            final String descColumn, final AbstractMeter meter,
            final AbstractResult result, final NiceTable input) {
        input.addRow(new String[] {
                descColumn,
                meter.getUnit(),
                ResultVisitor.format(result.sum(meter)),
                ResultVisitor.format(result.min(meter)),
                ResultVisitor.format(result.max(meter)),
                ResultVisitor.format(result.mean(meter)),
                ResultVisitor.format(result.getStandardDeviation(meter)),
                new StringBuilder("[").append(
                        ResultVisitor.format(result.getConf05(meter))).append(
                        "-").append(
                        ResultVisitor.format(result.getConf95(meter))).append(
                        "]").toString(),
                ResultVisitor.format(result.getResultSet(meter).size()) });
        return input;
    }

    private final NiceTable generateHeader(final NiceTable table) {
        table.addHeader("Benchmark");
        table.addLine('*');
        table.addRow(new String[] {
                "-", "unit", "sum", "min", "max", "avg", "stddev", "conf95",
                "runs" });
        table.addLine('*');
        return table;
    }
}
