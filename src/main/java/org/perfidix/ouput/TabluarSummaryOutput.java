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

import java.io.OutputStream;

import org.perfidix.result.BenchmarkResult;

/**
 * Summary output using the {@link NiceTable} to format. Just giving an overview
 * of statistical analysis over the results.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class TabluarSummaryOutput extends ResultVisitor {

    private final OutputStream out;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramOut
     *            an {@link OutputStream} to pipe to.
     */
    public TabluarSummaryOutput(final OutputStream paramOut) {
        out = paramOut;
    }

    /**
     * Constructor, just giving out on the {@link System#out}.
     */
    public TabluarSummaryOutput() {
        this(System.out);
    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(final BenchmarkResult res) {
        final NiceTable table = new NiceTable(9);
        table.addHeader("Benchmark");
        table.addLine('*');
        table.addRow(new String[] {
                "-", "unit", "sum", "min", "max", "avg", "stddev", "conf95",
                "runs" });

    }
}
