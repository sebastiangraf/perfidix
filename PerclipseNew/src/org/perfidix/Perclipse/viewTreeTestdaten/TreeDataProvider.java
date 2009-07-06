package org.perfidix.Perclipse.viewTreeTestdaten;

public class TreeDataProvider {
	/**
	 * parentElement is the Element which has been annotated to be benched.
	 * 
	 */
	private String parentElementName;
	private int numberOfBenchsForParent;
	private double durationForParent;
	private TreeDataProvider parent = null;

	/**
	 * childElement within the tree view a child. But its not really a child of
	 * parentElement. The child element depicts the result of each bench run of
	 * the parent element.
	 * 
	 */
	private TreeDataProvider childElements[]= new TreeDataProvider[0];

	public TreeDataProvider(String parentElement, int numberOfBenchs,
			double durationForElement) {
		this.parentElementName = parentElement;
		this.numberOfBenchsForParent = numberOfBenchs;
		this.durationForParent = durationForElement;
	}

	public TreeDataProvider(String parentElement, int numberOfBenchs,
			double durationForElement, TreeDataProvider childElements[]) {
		this(parentElement, numberOfBenchs, durationForElement);
		this.childElements = childElements;
		for (int i = 0; i < childElements.length; i++) {
			childElements[i].parent = this;
		}
	}

	public static TreeDataProvider[] exampleData() {
		return new TreeDataProvider[] {
				new TreeDataProvider("Element1", 2, 10.555,
						new TreeDataProvider[] {
								new TreeDataProvider("Element1", 0, 1.078),
								new TreeDataProvider("Element4", 0, 1.043),
								new TreeDataProvider("Element1", 0, 1.099) }),
				new TreeDataProvider("Element2", 1, 10.232,
						new TreeDataProvider[] { new TreeDataProvider(
								"Element2", 0, 1.088) }) };
	}

	public String toString() {
		return this.parentElementName +" Benchs: "+ numberOfBenchsForParent+ "   (" + durationForParent + ")";
	}

	public TreeDataProvider getParent() {
		return parent;
	}

	public TreeDataProvider[] getChildElements() {
		return childElements;
	}

}
