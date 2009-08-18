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
    private HashMap<String, Integer> mapElements;
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
        mapElements = benchElementsWithTotalBench;
        List<JavaElementsWithTotalRuns> list =
                new ArrayList<JavaElementsWithTotalRuns>();
        if (mapElements != null && mapElements.size() > 0) {
            Set<String> theSet = mapElements.keySet();
            for (String elementName : theSet) {
                list.add(new JavaElementsWithTotalRuns(elementName, mapElements
                        .get(elementName)));
                totalRun = totalRun + mapElements.get(elementName);
            }
        }
        runSession.setBenchRunSession(totalRun, list);
        viewUpdater.updateView(runSession);
    }

    /** {@inheritDoc} */
    public void updateCurrentRun(String currentElement) {
        if (mapElements != null && mapElements.containsKey(currentElement)) {
            runSession.setCurrentRun(currentElement);
            viewUpdater.updateView(runSession);
        }
    }

    /** {@inheritDoc} */
    public void updateError(String element) {
        if (mapElements != null && mapElements.containsKey(element)) {
            runSession.updateError(element);
            viewUpdater.updateView(runSession);
        }
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
