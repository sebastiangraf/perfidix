package org.perfidix.Perclipse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This class is the implementation of the interface
 * {@link IBenchRunSessionListener}. It receives data from the
 * PerclipseViewSkeleton and sets the BenchRunSession.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchRunSessionListener implements IBenchRunSessionListener {

    private BenchRunSession runSession;
    private BenchRunViewUpdater viewUpdater;
    private boolean finished = false;

    /**
     * The constructor creates new instances of the BenchRunSession and
     * BenchRunViewUpdater
     */
    public BenchRunSessionListener() {
        runSession = new BenchRunSession();
        viewUpdater = new BenchRunViewUpdater();
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    public void updateCurrentRun(String currentElement) {
        runSession.setCurrentRun(currentElement);
        viewUpdater.updateView(runSession);
    }

    /** {@inheritDoc} */
    public void updateError(String element) {
        runSession.updateError(element);
        viewUpdater.updateView(runSession);
    }

    /** {@inheritDoc} */
    public void finishedBenchRuns() {
        finished = true;
        runSession.setFinished(finished);

    }

    /**
     * @return Return finished.
     */
    public boolean getFinished() {
        return finished;
    }

}
