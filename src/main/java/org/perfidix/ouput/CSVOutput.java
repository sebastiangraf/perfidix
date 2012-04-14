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
package org.perfidix.ouput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

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
    private static final String SEPARATOR = "-";

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
    private transient final Map<File, PrintStream> usedFiles;

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
            throw new IllegalStateException(new StringBuilder(paramFolder.toString()).append(
                " has to be a folder!").toString());
        }
        firstResult = true;
        firstException = true;
        usedFiles = new Hashtable<File, PrintStream>();
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
    public boolean listenToResultSet(final Method meth, final AbstractMeter meter, final double data) {
        final PrintStream stream =
            setUpNewPrintStream(false, meth.getDeclaringClass().getSimpleName(), meth.getName(), meter
                .getName());
        if (!firstResult) {
            stream.append(",");
        }
        stream.append(Double.toString(data));
        stream.flush();
        firstResult = false;
        return true;

    }

    /** {@inheritDoc} */
    @Override
    public boolean listenToException(final AbstractPerfidixMethodException exec) {
        final PrintStream currentWriter = setUpNewPrintStream(false, "Exceptions");
        if (!firstException) {
            currentWriter.append("\n");
        }
        currentWriter.append(exec.getRelatedAnno().getSimpleName());
        currentWriter.append(",");
        if (exec.getMethod() != null) {
            currentWriter.append(exec.getMethod().getDeclaringClass().getSimpleName());
            currentWriter.append("#");
            currentWriter.append(exec.getMethod().getName());
            currentWriter.append(",");
        }
        exec.getExec().printStackTrace(currentWriter);
        currentWriter.flush();
        firstException = false;
        return true;

    }

    /** {@inheritDoc} */
    @Override
    public void visitBenchmark(final BenchmarkResult res) {
        // Printing the data
        for (final ClassResult classRes : res.getIncludedResults()) {
            for (final MethodResult methRes : classRes.getIncludedResults()) {
                for (final AbstractMeter meter : methRes.getRegisteredMeters()) {
                    final PrintStream currentWriter =
                        setUpNewPrintStream(true, classRes.getElementName(), methRes.getElementName(), meter
                            .getName());
                    boolean first = true;
                    for (final Double d : methRes.getResultSet(meter)) {
                        if (first) {
                            currentWriter.append(d.toString());
                            first = false;
                        } else {
                            currentWriter.append(new StringBuilder(",").append(d.toString()).toString());
                        }
                    }

                    currentWriter.flush();
                }
            }
        }
        // Printing the exceptions
        final PrintStream currentWriter = setUpNewPrintStream(true, "Exceptions");

        for (final AbstractPerfidixMethodException exec : res.getExceptions()) {
            currentWriter.append(exec.getRelatedAnno().getSimpleName());
            if (exec.getMethod() != null) {
                currentWriter.append(":");
                currentWriter.append(exec.getMethod().getDeclaringClass().getSimpleName());
                currentWriter.append("#");
                currentWriter.append(exec.getMethod().getName());
            }
            currentWriter.append("\n");
            exec.getExec().printStackTrace(currentWriter);
        }

        currentWriter.flush();
        tearDownAllStreams();
    }

    private void tearDownAllStreams() {
        for (final PrintStream stream : usedFiles.values()) {
            stream.close();
        }
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
    private PrintStream setUpNewPrintStream(final boolean visitorStream, final String... names) {

        PrintStream out = System.out;

        if (folder == null) {
            if (visitorStream) {
                out.println();
            }
        } else {
            final File toWriteTo = new File(folder, buildFileName(names));
            try {
                if (usedFiles.containsKey(toWriteTo)) {
                    out = usedFiles.get(toWriteTo);
                } else {
                    toWriteTo.delete();
                    out = new PrintStream(new FileOutputStream(toWriteTo, !visitorStream));
                    usedFiles.put(toWriteTo, out);
                    firstResult = true;
                }
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
