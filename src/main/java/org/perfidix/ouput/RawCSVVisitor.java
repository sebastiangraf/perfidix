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
import java.io.FileWriter;

import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Getting out the raw-data.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public final class RawCSVVisitor extends ResultVisitor {

    /**
     * Separator to distinguish between class, meter and method
     */
    private static final String SEPARATOR = "$";

    /** Print stream where the result should end */
    private final File folder;

    /**
     * Constructor for piping the result to elsewhere.
     * 
     * @param paramFolder
     *            an {@link File} object which has to be a folder to write to
     */
    public RawCSVVisitor(final File paramFolder) {
        folder = paramFolder;
        if (!folder.isDirectory()) {
            throw new IllegalStateException(new StringBuilder(paramFolder
                    .toString()).append(" has to be a folder!").toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void visitBenchmark(final BenchmarkResult res) {
        for (final ClassResult classRes : res.getIncludedResults()) {
            for (final MethodResult methRes : classRes.getIncludedResults()) {
                for (final AbstractMeter meter : methRes.getRegisteredMeters()) {
                    final File toWriteTo =
                            new File(folder, buildFileName(
                                    classRes.getElementName(), methRes
                                            .getElementName(), meter));
                    try {
                        final FileWriter writer =
                                new FileWriter(toWriteTo, true);
                        int i = 0;
                        for (final Double d : methRes.getResultSet(meter)) {
                            if (i == methRes.getResultSet(meter).size() - 1) {
                                writer.write(d.toString());
                            } else {
                                writer.write(new StringBuilder(d.toString())
                                        .append(",").toString());
                            }
                        }

                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    /**
     * Helper method to build suitable fileNames
     * 
     * @param clazzName
     *            the name of the {@link Class} of the {@link ClassResult}
     * @param methName
     *            the name of the {@link Method} of the {@link MethodResult}
     * @param meter
     *            the name of the meter
     * @return a String for a suitable file name
     */
    private final String buildFileName(
            final String clazzName, final String methName,
            final AbstractMeter meter) {
        final StringBuilder builder = new StringBuilder();
        builder.append(clazzName).append(SEPARATOR).append(methName).append(
                SEPARATOR).append(meter.getName()).append(".").append("csv");
        return builder.toString();
    }
}
