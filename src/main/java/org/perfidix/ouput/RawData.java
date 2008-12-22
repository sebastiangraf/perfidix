package org.perfidix.ouput;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Storing the raw data without any computation comma-separated to multiple
 * {@link java.io.OutputStream} instances.
 * 
 * @author Sebastian Graf, University of Konstanz
 */
public abstract class RawData extends ResultVisitor {

    private final static String STANDARDSEPARATOR = ",";
    private final static String UNITSEPARATOR = ":";

    /**
     * Constructor.
     */
    protected RawData() {
    }

    /** {@inheritDoc} */
    @Override
    protected void handleBenchmarkResult(final BenchmarkResult benchRes) {

        if (getCurrentBenchmarkOutputStream() != null) {
            final OutputStreamWriter writer =
                    new OutputStreamWriter(getCurrentBenchmarkOutputStream());
            try {
                for (final AbstractMeter meter : benchRes.getRegisteredMeters()) {

                    writer.write(new StringBuilder(meter.getUnit()).append(
                            UNITSEPARATOR).toString());
                    final StringBuilder results = new StringBuilder();
                    final Collection<Double> data =
                            benchRes.getResultSet(meter);
                    for (final double d : data) {
                        results.append(d).append(STANDARDSEPARATOR);
                    }
                    writer.write(results.substring(0, results.length() - 1));
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void handleClassResult(final ClassResult classRes) {

        if (getCurrentClassOutputStream() != null) {
            final OutputStreamWriter writer =
                    new OutputStreamWriter(getCurrentClassOutputStream());
            try {
                for (final AbstractMeter meter : classRes.getRegisteredMeters()) {

                    writer.write(new StringBuilder(meter.getUnit()).append(
                            UNITSEPARATOR).toString());
                    final StringBuilder results = new StringBuilder();
                    final Collection<Double> data =
                            classRes.getResultSet(meter);
                    for (final double d : data) {
                        results.append(d).append(STANDARDSEPARATOR);
                    }
                    writer.write(results.substring(0, results.length() - 1));
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

    }

    /** {@inheritDoc} */
    @Override
    protected void handleMethodResult(final MethodResult methRes) {

        if (getCurrentMethodOutputStream() != null) {
            final OutputStreamWriter writer =
                    new OutputStreamWriter(getCurrentMethodOutputStream());
            try {
                for (final AbstractMeter meter : methRes.getRegisteredMeters()) {

                    writer.write(new StringBuilder(meter.getUnit()).append(
                            UNITSEPARATOR).toString());
                    final StringBuilder results = new StringBuilder();
                    final Collection<Double> data = methRes.getResultSet(meter);
                    for (final double d : data) {
                        results.append(d).append(STANDARDSEPARATOR);
                    }
                    writer.write(results.substring(0, results.length() - 1));
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

    }

}
