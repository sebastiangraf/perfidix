package org.perfidix.Perclipse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BenchRunSessionListener implements IBenchRunSessionListener {

    private BenchRunSession runSession;
    private BenchRunViewUpdater viewUpdater;
    private boolean finished = false;

    public BenchRunSessionListener() {
        runSession = new BenchRunSession();
        viewUpdater = new BenchRunViewUpdater();
    }

    public void initTotalBenchProgress(
            HashMap<String, Integer> benchElementsWithTotalBench) {
        int totalRun = 0;
        List<JavaElementsWithTotalRuns> list =
                new ArrayList<JavaElementsWithTotalRuns>();
        if (benchElementsWithTotalBench != null) {
            Set<String> theSet = benchElementsWithTotalBench.keySet();
            for (String elementName : theSet) {
                list.add(new JavaElementsWithTotalRuns(
                        elementName, benchElementsWithTotalBench
                                .get(elementName)));
                totalRun =
                        totalRun + benchElementsWithTotalBench.get(elementName);
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
