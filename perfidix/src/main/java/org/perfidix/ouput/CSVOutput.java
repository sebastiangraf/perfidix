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
import java.util.HashSet;
import java.util.Set;

import org.perfidix.exceptions.AbstractPerfidixMethodException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Getting out the raw-data as csv.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class CSVOutput extends AbstractOutput {

    /**
     * Separator to distinguish between class, meter and method.
     */
    private static final String SEPARATOR = "$";

    /** Print stream where the result should end. */
    private transient final File folder;

    /**
     * Flag for correct commata for results.
     */
    private transient boolean firstResult;

    /**
     * Flag for correct commata for exceptions.
     */
    private transient boolean firstException;

    /**
     * Set for deleting old files in the beginning.
     */
    private transient final Set<File> usedFiles;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramFolder
     *            an {@link File} object which has to be a folder to write to
     */
    public CSVOutput(final File paramFolder) {
        super();
        folder = paramFolder;
        if (folder != null && !folder.isDirectory()) {
            throw new IllegalStateException(new StringBuilder(paramFolder
                    .toString()).append(" has to be a folder!").toString());
        }
        firstResult = true;
        firstException = true;
        usedFiles = new HashSet<File>();
    }

    /**
     * Constructor for output to {@link System#out}.
     */
    public CSVOutput() {
        this(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void listenToResultSet(
            final Method meth, final AbstractMeter meter, final double data) {
        final PrintStream stream =
                setUpNewPrintStream(
                        false, meth.getDeclaringClass().getSimpleName(), meth
                                .getName(), meter.getName());
        if (!firstResult) {
            stream.append(",");
        }
        stream.append(Double.toString(data));
        stream.flush();
        firstResult = false;

    }

    /** {@inheritDoc} */
    @Override
    public void listenToException(final AbstractPerfidixMethodException exec) {
        final PrintStream currentWriter =
                setUpNewPrintStream(false, "Exceptions");
        if (!firstException) {
            currentWriter.append("\n");
        }
        currentWriter.append(exec.getRelatedAnno().getSimpleName());
        currentWriter.append(",");
        if (exec.getMethod() != null) {
            currentWriter.append(exec
                    .getMethod().getDeclaringClass().getSimpleName());
            currentWriter.append("#");
            currentWriter.append(exec.getMethod().getName());
            currentWriter.append(",");
        }
        exec.getExec().printStackTrace(currentWriter);
        currentWriter.flush();
        firstException = false;

    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(final BenchmarkResult res) {
        // Printing the data
        for (final ClassResult classRes : res.getIncludedResults()) {
            for (final MethodResult methRes : classRes.getIncludedResults()) {
                for (final AbstractMeter meter : methRes.getRegisteredMeters()) {
                    final PrintStream currentWriter =
                            setUpNewPrintStream(
                                    true, classRes.getElementName(), methRes
                                            .getElementName(), meter.getName());
                    boolean first = true;
                    for (final Double d : methRes.getResultSet(meter)) {
                        if (first) {
                            currentWriter.append(d.toString());
                            first = false;
                        } else {
                            currentWriter.append(new StringBuilder(",").append(
                                    d.toString()).toString());
                        }
                    }

                    currentWriter.flush();

                }
            }
        }

        // Printing the exceptions
        final PrintStream currentWriter =
                setUpNewPrintStream(true, "Exceptions");

        for (final AbstractPerfidixMethodException exec : res.getExceptions()) {
            currentWriter.append(exec.getRelatedAnno().getSimpleName());
            if (exec.getMethod() != null) {
                currentWriter.append(":");
                currentWriter.append(exec
                        .getMethod().getDeclaringClass().getSimpleName());
                currentWriter.append("#");
                currentWriter.append(exec.getMethod().getName());
            }
            currentWriter.append("\n");
            exec.getExec().printStackTrace(currentWriter);
        }

        currentWriter.flush();

    }

    /**
     * Setting up a new {@link PrintStream}.
     * 
     * @param visitorStream
     *            is the stream for the visitor? Because of line breaks after
     *            the results.
     * @param names
     *            the elements of the filename
     * @return a {@link PrintStream} instance
     * @throws FileNotFoundException
     *             if something goes wrong with the file
     */
    private PrintStream setUpNewPrintStream(
            final boolean visitorStream, final String... names) {

        PrintStream out = System.out;

        if (folder == null) {
            if (visitorStream) {
                out.println();
            }
        } else {
            final File toWriteTo = new File(folder, buildFileName(names));
            if (!usedFiles.contains(toWriteTo)) {
                toWriteTo.delete();
                usedFiles.add(toWriteTo);
                firstResult = true;
            }
            try {
                out =
                        new PrintStream(new FileOutputStream(
                                toWriteTo, !visitorStream));
            } catch (final FileNotFoundException e) {
                throw new IllegalStateException(e);
            }

        }
        return out;

    }

    /**
     * Helper method to build suitable fileNames.
     * 
     * @param names
     *            different names to be combined
     * @return a String for a suitable file name
     */
    private String buildFileName(final String... names) {
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
