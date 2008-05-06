package org.perfidix.visitor;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perfidix.IResult;
import org.perfidix.Result;

/**
 * Storing the raw data without any computation in single files. Per method, one
 * single file is opened with the data. Only usable with Benchmarkresult
 * 
 * @author sebi
 */
public class RawData extends ResultVisitor {

    private final File folder;

    private static final Log LOGGER = LogFactory.getLog(RawData.class);

    public RawData(final String pathToFolder) {

        folder = new File(pathToFolder);

    }

    @Override
    public void visit(Result r) {
        try {
            if (!(r instanceof IResult.BenchmarkResult)) {
                throw new RuntimeException(
                        "only benchmark results are supported!");
            }
            IResult.BenchmarkResult benchRes = (IResult.BenchmarkResult) r;
            for (IResult.ClassResult classRes : benchRes.getChildren()) {
                for (IResult.MethodResult methodRes : classRes.getChildren()) {
                    File timeFile =
                            new File(this.folder.getAbsolutePath()
                                    + File.separatorChar
                                    + classRes.getName()
                                    + "$"
                                    + methodRes.getName()
                                    + ".csv");
                    getMethodResult(timeFile, methodRes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMethodResult(
            final File outputFile, final IResult.MethodResult methodRes) {

        try {
            final ArrayList<IResult.SingleResult> singleTimes =
                    new ArrayList<IResult.SingleResult>();
            final Collection<ArrayList<IResult.SingleResult>> childs =
                    methodRes.getCustomChildren().values();
            for (final ArrayList<IResult.SingleResult> currentList : childs) {
                singleTimes.addAll(currentList);
            }
            for (final IResult.SingleResult result : singleTimes) {
                final File currentFile =
                        new File(outputFile.getAbsoluteFile()
                                + "$"
                                + result.getMeter().getName());
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
                final long data[] = result.getResultSet();
                for (int i = 0; i < data.length; i++) {
                    if (i == data.length - 1) {
                        timeOut.write(data[i] + " ");
                    } else {
                        timeOut.write(data[i] + ",");
                    }
                }
                timeOut.flush();
                timeOut.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
