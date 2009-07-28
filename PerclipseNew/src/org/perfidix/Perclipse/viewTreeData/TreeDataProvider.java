package org.perfidix.Perclipse.viewTreeData;

/**
 * The TreeDataProvider class is responsible for the data that will be displayed
 * in the progress view of the eclipse plugin
 * 
 * @author Lukas Lewandowski, DiSy, University of Konstanz
 */
public class TreeDataProvider {
 
    private String parentElementName;
    private int numberOfBenchsForElement;
    private int currentBench;
    private TreeDataProvider parent = null;

    /**
     * childElement within the tree view a child. But its not really a child of
     * parentElement. The child element depicts the result of each bench run of
     * the parent element.
     */
    private TreeDataProvider childElements[] = new TreeDataProvider[0];

    /**
     * @param elementName
     */
    public TreeDataProvider(String elementName) {
        this.parentElementName = elementName;
    }

    /**
     * @param parentElement
     * @param numberOfBenchs
     * @param currentBench
     */
    public TreeDataProvider(
            String parentElement, int numberOfBenchs, int currentBench) {
        this.parentElementName = parentElement;
        this.numberOfBenchsForElement = numberOfBenchs;
        this.currentBench = currentBench;
    }

    /**
     * Override toString()
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return parentElementName;
    }

    /**
     * @return
     */
    public TreeDataProvider[] getChildElements() {
        return childElements;
    }

    /**
     * @return
     */
    public String getParentElementName() {
        return parentElementName;
    }

    /**
     * @return
     */
    public int getNumberOfBenchsForElement() {
        return numberOfBenchsForElement;
    }

    /**
     * @return
     */
    public int getCurrentBench() {
        return currentBench;
    }

    /**
     * @return
     */
    public TreeDataProvider getParent() {
        return parent;
    }

}
