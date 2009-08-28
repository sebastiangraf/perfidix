package org.perfidix.perclipse.model;

import java.util.Map;

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
     * @param benchElements
     *            This param is an HashMap which consists of each java element
     *            name with its total bench runs value.
     */
    void initTotalBenchProgress(Map<String, Integer> benchElements);

    /**
     * The updateCurrentRun method notifies the view which element is currently
     * running.
     * 
     * @param currentElement
     *            This {@link String} param represents the current running java
     *            element.
     */
    void updateCurrentRun(String currentElement);

    /**
     * The updateError method updates the view that an error occurred while
     * benching the given java element.
     * 
     * @param element
     *            This {@link String} param represents the element name of the
     *            benched object where the error occurred.
     * @param exception The exception occurred in the element.
     */
    void updateError(String element, final String exception);

    /**
     * This method notifies the view that all bench runs completed.
     */
    void finishedBenchRuns();

}
