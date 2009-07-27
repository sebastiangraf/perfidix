package org.perfidix.Perclipse.viewTreeTestdaten;

public class TreeDataProvider {
    /**
     * parentElement is the Element which has been annotated to be benched.
     */
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

    public TreeDataProvider(String elementName) {
        this.parentElementName = elementName;
    }

    public TreeDataProvider(
            String parentElement, int numberOfBenchs, int currentBench) {
        this.parentElementName = parentElement;
        this.numberOfBenchsForElement=numberOfBenchs;
        this.currentBench=currentBench;
    }


    public String toString() {
        return parentElementName;
    }

    public TreeDataProvider getParent() {
        return parent;
    }

    public TreeDataProvider[] getChildElements() {
        return childElements;
    }

    public String getParentElementName() {
        return parentElementName;
    }


    public int getNumberOfBenchsForElement() {
        return numberOfBenchsForElement;
    }
    
    public int getCurrentBench() {
        return currentBench;
    }

}
