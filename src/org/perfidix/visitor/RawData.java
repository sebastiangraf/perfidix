package org.perfidix.visitor;

import java.io.File;
import java.io.FileWriter;

import org.perfidix.IResult;
import org.perfidix.Result;

/**
 * Storing the raw data without any computation in single files. Per method, one
 * single file is opened with the data. Only usable with Benchmarkresult
 * 
 * @author sebi
 * 
 */
public class RawData extends ResultVisitor {

    private final File folder;

    public RawData(final String pathToFolder) {
        folder = new File(pathToFolder);
        if (folder.exists()) {
            if (!folder.isDirectory()) {
                folder.delete();
            } else {
                deleteRecursive(folder);
            }
        }
        folder.mkdir();
    }

    @Override
    public void visit(Result r) {
        if (!(r instanceof IResult.BenchmarkResult)) {
            throw new RuntimeException("only benchmark results are supported!");
        }
        IResult.BenchmarkResult benchRes = (IResult.BenchmarkResult) r;
        for (IResult.ClassResult classRes : benchRes.getChildren()) {
            for (IResult.MethodResult methodRes : classRes.getChildren()) {
                getMethodResult(methodRes);
            }
        }
    }

    private void getMethodResult(final IResult.MethodResult methodRes) {
        try {
            int j = 1;
            File currentFile = new File(this.folder.getAbsolutePath() + "//"
                    + methodRes.getName() + j);
            while (currentFile.exists()) {
                System.out.println(currentFile.getAbsolutePath()
                        + " is already existing!");
                j++;
                currentFile = new File(this.folder.getAbsolutePath() + "//"
                        + methodRes.getName() + j);

            }
            System.out.println("Using " + currentFile.getAbsolutePath()
                    + " for output!");
            currentFile.createNewFile();
            final FileWriter out = new FileWriter(currentFile);
            IResult.SingleResult single = methodRes.getChildren().get(0);
            final long data[] = single.getResultSet();
            for (int i = 0; i < data.length; i++) {
                if (i == data.length - 1) {
                    out.write(data[i] + " ");
                } else {
                    out.write(data[i] + ",");
                }
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteRecursive(final File file) {
        if (file.isDirectory()) {
            final File[] childs = file.listFiles();
            for (int i = 0; i < childs.length; i++) {
                deleteRecursive(childs[i]);
            }
        } else {
            file.delete();
        }
    }

}
