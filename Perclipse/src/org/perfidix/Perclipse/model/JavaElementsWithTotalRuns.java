package org.perfidix.Perclipse.model;

/**
 * This class declares an java element that will be benched. It contains of its
 * fully qualified name and bench runs.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class JavaElementsWithTotalRuns {

    private String javaElement;
    private int totalRuns;
    private int currentRun;
    private int errorCount;

    /**
     * The constructor initializes the java element name and its total runs.
     * Afterwards it resets the current and error count.
     * 
     * @param javaElement
     *            The fully qualified {@link String} name of the java element.
     * @param totalRuns
     *            The total bench runs of the java element.
     */
    public JavaElementsWithTotalRuns(String javaElement, int totalRuns) {
        this.javaElement = javaElement;
        this.totalRuns = totalRuns;
        currentRun = 0;
        errorCount = 0;
    }

    /**
     * @return Return the currentRun of the java element.
     */
    public int getCurrentRun() {
        return currentRun;
    }

    /**
     * @return Return the total runs of the java element.
     */
    public int getTotalRuns() {
        return totalRuns;
    }

    /**
     * @return Return the fully qualified name of the java element.
     */
    public String getJavaElement() {
        return javaElement;
    }

    /**
     * This method updates the current bench run of this java element.
     */
    public void updateCurrentRun() {
        currentRun = currentRun + 1;
    }

    /**
     * This method updates the current error count of this java element.
     */
    public void updateErrorCount() {
        errorCount = errorCount + 1;
    }

    /**
     * @return Returns the amount of the error count.
     */
    public int getErrorCount() {
        return errorCount;
    }

}
