/**
 * 
 */
package org.perfidix.result;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;


/**
 * Converts a result to comma separated values including column and row header
 * allows nice output on stdout and writing to file.
 * 
 * @author Thomas Zink
 */
public class ResultToCsv extends ResultVisitor {

    /** map of filenames and filecontent */
    private HashMap<String, String> finaco;

    /** the column header */
    private final String header =
            "unit,sum,min,max,avg,var,stddev,conf95+,conf95-,conf99+,conf99-,runs\n";

    /**
     * basic constructor
     */
    public ResultToCsv() {
        finaco = new HashMap<String, String>();
    }

    /*
     * (non-Javadoc)
     * @see org.perfidix.visitor.ResultVisitor#visit(org.perfidix.Result)
     */
    @Override
    public void visit(IResult r) {
        try {
            if (!(r instanceof IResult.BenchmarkResult)) {
                throw new RuntimeException(
                        "only benchmark results are supported!");
            }
            IResult.BenchmarkResult benchRes = (IResult.BenchmarkResult) r;
            for (IResult.ClassResult classRes : benchRes.getChildren()) {
                for (IResult.MethodResult methodRes : classRes.getChildren()) {
                    String fina =
                            classRes.getName() + "$" + methodRes.getName();
                    StringBuilder fico = new StringBuilder();
                    for (IResult.SingleResult singleRes : methodRes
                            .getChildren()) {
                        // "unit,sum,min,max,avg,var,stddev,conf95+,conf95-,conf99+,conf99-,runs"
                        fico.append(singleRes.getUnit()).append(",");
                        fico.append(singleRes.sum()).append(",");
                        fico.append(singleRes.min()).append(",");
                        fico.append(singleRes.max()).append(",");
                        fico.append(singleRes.avg()).append(",");
                        fico.append(singleRes.variance()).append(",");
                        fico.append(singleRes.getStandardDeviation());
                        fico.append(",");
                        fico.append(getConf95Min(singleRes)).append(",");
                        fico.append(getConf95Max(singleRes)).append(",");
                        fico.append(getConf99Min(singleRes)).append(",");
                        fico.append(getConf99Max(singleRes)).append(",");
                        fico.append(singleRes.getNumberOfRuns()).append("\n");
                    }
                    finaco.put(fina, fico.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Set<String> keys = finaco.keySet();
        for (String key : keys) {
            ret.append(key).append("\n");
            ret.append(header);
            ret.append(finaco.get(key)).append("\n");
        }
        return ret.toString();
    }

    /**
     * flushes the csv to files in the specified output folder
     * 
     * @param pathToFolder
     *            the output path
     */
    public void toFiles(final String pathToFolder) {
        String path = new File(pathToFolder).getAbsolutePath();
        Set<String> keys = finaco.keySet();
        for (String key : keys) {
            final File curf =
                    new File(path + File.separatorChar + key + ".csv");
            if (curf.exists())
                curf.delete();
            try {
                final FileWriter curfw = new FileWriter(curf);
                curfw.write(header + finaco.get(key));
                curfw.flush();
                curfw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
