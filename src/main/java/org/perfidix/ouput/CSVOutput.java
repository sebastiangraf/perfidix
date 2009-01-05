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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.perfidix.failureHandling.PerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Getting out the raw-data as csv
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class CSVOutput extends AbstractOutput {

    /**
     * Separator to distinguish between class, meter and method
     */
    private static final String SEPARATOR = "$";

    /** Print stream where the result should end */
    private final File folder;

    /**
     * Flag for correct commata for results
     */
    private boolean firstResult;

    /**
     * Flag for correct commata for exceptions
     */
    private boolean firstException;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramFolder
     *            an {@link File} object which has to be a folder to write to
     */
    public CSVOutput(final File paramFolder) {
        folder = paramFolder;
        if (!folder.isDirectory()) {
            throw new IllegalStateException(new StringBuilder(paramFolder
                    .toString()).append(" has to be a folder!").toString());
        }
        final File[] childFiles = folder.listFiles();
        for (File file : childFiles) {
            file.delete();
        }
        firstResult = true;
        firstException = true;
    }

    /**
     * Constructor for output to {@link System#out}
     */
    public CSVOutput() {
        folder = null;
        firstResult = true;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void listenToResultSet(
            final Method meth, final AbstractMeter meter, final double data) {
        try {
            final PrintStream stream =
                    setUpNewPrintStream(
                            meth.getDeclaringClass().getName(), meth.getName(),
                            meter.getName());
            if (!firstResult) {
                stream.append(",");
            }
            stream.append(new Double(data).toString());
            stream.flush();
            firstResult = false;
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /** {@inheritDoc} */
    @Override
    public final void listenToException(PerfidixMethodException exec) {
        try {
            final PrintStream currentWriter = setUpNewPrintStream("Exceptions");
            if (!firstException) {
                currentWriter.append(",");
            }
            currentWriter.append(exec.getRelatedAnno().toString());
            currentWriter.append(":");
            exec.printStackTrace(currentWriter);
            currentWriter.flush();
            firstException = true;
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    /** {@inheritDoc} */
    @Override
    public final void visitBenchmark(final BenchmarkResult res) {
        for (final ClassResult classRes : res.getIncludedResults()) {
            for (final MethodResult methRes : classRes.getIncludedResults()) {
                for (final AbstractMeter meter : methRes.getRegisteredMeters()) {
                    try {
                        final PrintStream currentWriter =
                                setUpNewPrintStream(
                                        classRes.getElementName(), methRes
                                                .getElementName(), meter
                                                .getName());
                        int i = 0;
                        for (final Double d : methRes.getResultSet(meter)) {
                            if (i == methRes.getResultSet(meter).size() - 1) {
                                currentWriter.append(d.toString());
                            } else {
                                currentWriter.append(new StringBuilder(d
                                        .toString()).append(",").toString());
                            }
                        }

                        currentWriter.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            final PrintStream currentWriter = setUpNewPrintStream("Exceptions");

            for (final PerfidixMethodException exec : res.getExceptions()) {
                currentWriter.append(exec.getRelatedAnno().toString());
                currentWriter.append(":");
                exec.printStackTrace(currentWriter);
            }

            currentWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final PrintStream setUpNewPrintStream(final String... names)
            throws FileNotFoundException {

        if (folder != null) {
            final File toWriteTo = new File(folder, buildFileName(names));
            return new PrintStream(new FileOutputStream(toWriteTo, true));
        } else {
            System.out.println();
            return System.out;
        }
    }

    /**
     * Helper method to build suitable fileNames
     * 
     * @param names
     *            different names to be combined
     * @return a String for a suitable file name
     */
    private final String buildFileName(final String... names) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            builder.append(names[i]);
            if (i < names.length - 1) {
                builder.append(SEPARATOR);
            }
        }
        builder.append(".").append("csv");
        return builder.toString();
    }
}
