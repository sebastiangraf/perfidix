package org.perfidix.Perclipse.model;

import java.util.HashMap;

public interface IBenchRunSessionListener {
    public void initTotalBenchProgress(
            HashMap<String, Integer> benchElementsWithTotalBench);

    public void updateCurrentRun(String currentElement);

    public void updateError(String element);

    public void finishedBenchRuns();

}
