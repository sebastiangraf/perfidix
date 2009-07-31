package org.perfidix.Perclipse.viewTreeData;

/**
 * The TreeDataProvider class is responsible for the data that will be displayed
 * in the progress view of the eclipse plug-in
 * 
 * @author Lukas Lewandowski, DiSy, University of Konstanz
 */
public class TreeDataProvider {

    private String parentElementName;
    private int numberOfBenchsForElement;
    private int currentBench;
    private TreeDataProvider parent = null;

    private TreeDataProvider childElements[] = new TreeDataProvider[0];

    /**
     * The constructor initializes the data which will be displayed in the view.
     * 
     * @param parentElement
     *            This param represents the java element that will be benched by
     *            perfidix.
     * @param numberOfBenchs
     *            This param represents the amount of runs for the responsible
     *            java element.
     * @param currentBench
     *            This param represents the current count of the bench run. Here
     *            it is initialized.
     */
    public TreeDataProvider(
            String parentElement, int numberOfBenchs, int currentBench) {
        this.parentElementName = parentElement;
        this.numberOfBenchsForElement = numberOfBenchs;
        this.currentBench = currentBench;
        parent = this;
    }

    /**
     * @return This returns the child elements of the java elements that will be
     *         benched. Currently it is not used. But it could.
     */
    public TreeDataProvider[] getChildElements() {
        return childElements;
    }

    /**
     * @return This method returns the java element that will be benched.
     */
    public String getParentElementName() {
        return parentElementName;
    }

    /**
     * @return This method returns the total runs for an appropriate java
     *         element.
     */
    public int getNumberOfBenchsForElement() {
        return numberOfBenchsForElement;
    }

    /**
     * Counts the currentBench;
     */
    public void updateCurrentBench(){
        currentBench=currentBench+1;
    }
    
    /**
     * @return This method returns the current bench run of the java element.
     */
    public int getCurrentBench() {
        return currentBench;
    }

    /**
     * @return This method returns this object.
     */
    public TreeDataProvider getParent() {
        return parent;
    }

    /**
     * Override toString()
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return parentElementName;
    }

}
