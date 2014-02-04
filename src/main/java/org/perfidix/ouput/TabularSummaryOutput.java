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
package org.perfidix.ouput;


import java.io.PrintStream;
import java.lang.reflect.Method;

import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.exceptions.PerfidixMethodInvocationException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.asciitable.NiceTable;
import org.perfidix.ouput.asciitable.AbstractTabularComponent.Alignment;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;


/**
 * Summary output using the {@link NiceTable} to format. Just giving an overview of statistical analysis over the
 * results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class TabularSummaryOutput extends AbstractOutput {

    /** Print stream where the result should end. */
    private transient final PrintStream out;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramOut an {@link PrintStream} to pipe to.
     */
    public TabularSummaryOutput (final PrintStream paramOut) {
        super();
        out = paramOut;
    }

    /**
     * Constructor, just giving out on the {@link System#out}.
     */
    public TabularSummaryOutput () {
        this(System.out);
    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark (final BenchmarkResult benchRes) {
        final int numberOfColumns = 9;
        NiceTable table = new NiceTable(numberOfColumns);
        table = generateHeader(table);
        for (final AbstractMeter meter : benchRes.getRegisteredMeters()) {
            table.addHeader(meter.getName(), '=', Alignment.Center);
            for (final ClassResult classRes : benchRes.getIncludedResults()) {
                table.addHeader(classRes.getElementName(), '.', Alignment.Left);
                for (final MethodResult methRes : classRes.getIncludedResults()) {
                    table = generateMeterResult(methRes.getElementName(), meter, methRes, table);
                }

                table.addHeader(new StringBuilder("Summary for ").append(classRes.getElementName()).toString(), '_', Alignment.Left);
                table = generateMeterResult("", meter, classRes, table);
                table.addLine('-');

            }
        }
        table.addHeader("Summary for the whole benchmark", '=', Alignment.Center);

        for (final AbstractMeter meter : benchRes.getRegisteredMeters()) {
            table = generateMeterResult("", meter, benchRes, table);
        }

        table.addHeader("Exceptions", '=', Alignment.Center);
        for (final AbstractPerfidixMethodException exec : benchRes.getExceptions()) {
            final StringBuilder execBuilder0 = new StringBuilder();
            execBuilder0.append("Related exception: ").append(exec.getExec().getClass().getSimpleName());
            table.addHeader(execBuilder0.toString(), ' ', Alignment.Left);

            final StringBuilder execBuilder1 = new StringBuilder();
            if (exec instanceof PerfidixMethodInvocationException) {
                execBuilder1.append("Related place: method invocation");
            } else {
                execBuilder1.append("Related place: method check");
            }
            table.addHeader(execBuilder1.toString(), ' ', Alignment.Left);
            if (exec.getMethod() != null) {
                final StringBuilder execBuilder2 = new StringBuilder();
                execBuilder2.append("Related method: ").append(exec.getMethod().getName());
                table.addHeader(execBuilder2.toString(), ' ', Alignment.Left);
            }
            final StringBuilder execBuilder3 = new StringBuilder();
            execBuilder3.append("Related annotation: ").append(exec.getRelatedAnno().getSimpleName());
            table.addHeader(execBuilder3.toString(), ' ', Alignment.Left);
            table.addLine('-');

        }
        table.addLine('=');
        out.println(table.toString());
    }

    /**
     * Generating the results for a given table.
     * 
     * @param columnDesc the description for the row
     * @param meter the corresponding {@link AbstractMeter} instance
     * @param result the corresponding {@link AbstractResult} instance
     * @param input the {@link NiceTable} to be print to
     * @return the modified {@link NiceTable} instance
     */
    private NiceTable generateMeterResult (final String columnDesc, final AbstractMeter meter, final AbstractResult result, final NiceTable input) {
        input.addRow(new String[] { columnDesc, meter.getUnit(), AbstractOutput.format(result.sum(meter)), AbstractOutput.format(result.min(meter)), AbstractOutput.format(result.max(meter)), AbstractOutput.format(result.mean(meter)), AbstractOutput.format(result.getStandardDeviation(meter)), new StringBuilder("[").append(AbstractOutput.format(result.getConf05(meter))).append("-").append(AbstractOutput.format(result.getConf95(meter))).append("]").toString(), AbstractOutput.format(result.getResultSet(meter).size()) });
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean listenToResultSet (final Method meth, final AbstractMeter meter, final double data) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Class: ").append(meth.getDeclaringClass().getSimpleName()).append("#").append(meth.getName());
        builder.append("\nMeter: ").append(meter.getName());
        builder.append("\nData: ").append(data).append("\n");
        out.println(builder.toString());
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean listenToException (final AbstractPerfidixMethodException exec) {
        final StringBuilder builder = new StringBuilder();
        if (exec.getMethod() != null) {
            builder.append("Class: ").append(exec.getMethod().getDeclaringClass().getSimpleName()).append("#").append(exec.getMethod().getName()).append("\n");
        }
        builder.append("Annotation: ").append(exec.getRelatedAnno().getSimpleName());
        builder.append("\nException: ").append(exec.getClass().getSimpleName()).append("/").append(exec.getExec().toString());
        out.println(builder.toString());
        exec.getExec().printStackTrace(out);
        return true;

    }

    /**
     * Generating header for a given table.
     * 
     * @param table the table where the header should fit to
     * @return another {@link NiceTable} instance
     */
    private NiceTable generateHeader (final NiceTable table) {
        table.addHeader("Benchmark");
        table.addRow(new String[] { "-", "unit", "sum", "min", "max", "avg", "stddev", "conf95", "runs" });
        return table;
    }

}
