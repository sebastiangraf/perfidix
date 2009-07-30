package org.perfidix.Perclipse.model;

import java.util.HashMap;

/**
 * This interface specifies the methods which have to update the eclipse view
 * when a given event occurs.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public interface IBenchRunSessionListener {
    /**
     * The initTotalBenchProgress initializes the progress bar in the eclipse
     * view. This param represent the sum value of all runs to be executed.
     * 
     * @param benchElementsWithTotalBench
     *            This param is an HashMap which consists of each java element
     *            name with its total bench runs value.
     */
    public void initTotalBenchProgress(
            HashMap<String, Integer> benchElementsWithTotalBench);

    /**
     * The updateCurrentRun method notifies the view which element is currently
     * running.
     * 
     * @param currentElement
     *            This {@link String} param represents the current running java
     *            element.
     */
    public void updateCurrentRun(String currentElement);

    /**
     * The updateError method updates the view that an error occurred while
     * benching the given java element.
     * 
     * @param element
     *            This {@link String} param represents the element name of the
     *            benched object where the error occurred.
     */
    public void updateError(String element);

    /**
     * This method notifies the view that all bench runs completed.
     */
    public void finishedBenchRuns();

}
