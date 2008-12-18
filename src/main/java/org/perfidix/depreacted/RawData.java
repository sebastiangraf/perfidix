package org.perfidix.depreacted;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.result.AbstractResult;
import org.perfidix.result.BenchmarkResult;
import org.perfidix.result.ClassResult;
import org.perfidix.result.MethodResult;

/**
 * Storing the raw data without any computation in single files. Per method, one
 * single file is opened with the data. Only usable with Benchmarkresult
 * 
 * @author sebi
 */
public class RawData extends ResultVisitor {

    private final File folder;

    private static final Log LOGGER = LogFactory.getLog(RawData.class);

    private final AbstractMeter meter;

    public RawData(final String pathToFolder, final AbstractMeter meter) {

        folder = new File(pathToFolder);
        this.meter = meter;

    }

    @Override
    public void visit(AbstractResult r) {
        try {
            if (!(r instanceof BenchmarkResult)) {
                throw new RuntimeException(
                        "only benchmark results are supported!");
            }
            BenchmarkResult benchRes = (BenchmarkResult) r;
            for (ClassResult classRes : benchRes.getChildren()) {
                for (MethodResult methodRes : classRes.getChildren()) {
                    File timeFile =
                            new File(this.folder.getAbsolutePath()
                                    + File.separatorChar
                                    + classRes
                                    + "$"
                                    + methodRes
                                    + ".csv");
                    getMethodResult(timeFile, methodRes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMethodResult(
            final File outputFile, final MethodResult methodRes) {

        try {
            final File currentFile =
                    new File(outputFile.getAbsoluteFile()
                            + "$"
                            + meter.getName());
            // if (currentFile.exists()) {
            // currentFile.delete();
            // }
            FileWriter timeOut;
            if (currentFile.exists()) {
                timeOut = new FileWriter(currentFile, true);
                timeOut.write(",");
            } else {
                timeOut = new FileWriter(currentFile, false);
            }
            final Collection<Double> data = methodRes.getResultSet(meter);
            int i = 0;
            for (Double d : data) {
                if (i == data.size() - 1) {
                    timeOut.write(d + " ");
                } else {
                    timeOut.write(d + ",");
                }
            }
            timeOut.flush();
            timeOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
