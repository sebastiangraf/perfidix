package org.perfidix.Perclipse.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

public class BenchRunSessionListener implements IBenchRunSessionListener {

    private BenchRunSession runSession;
    private BenchRunViewUpdater viewUpdater;
    private boolean finished = false;

    public BenchRunSessionListener() {
        runSession = BenchRunSession.getInstance();
        viewUpdater = new BenchRunViewUpdater();
    }

    public void initTotalBenchProgress(
            int totalRun, Object[] benchElementsWithTotalBench) {
        List<JavaElementsWithTotalRuns> list =
                new ArrayList<JavaElementsWithTotalRuns>();
        if (benchElementsWithTotalBench != null) {
            for (int i = 0; i < benchElementsWithTotalBench.length; i = i + 2) {
                list.add(new JavaElementsWithTotalRuns(
                        (String) benchElementsWithTotalBench[i],
                        (Integer) benchElementsWithTotalBench[i + 1]));

            }
        }
        runSession.setBenchRunSession(totalRun, list);
        viewUpdater.updateView(runSession);
    }

    public void updateCurrentRun(String currentElement) {
        runSession.setCurrentRun(currentElement);
        viewUpdater.updateView(runSession);
    }

    public void updateError(String element) {
        runSession.updateError(element);
        viewUpdater.updateView(runSession);
    }

    public void finishedBenchRuns() {
        finished = true;

    }

}
