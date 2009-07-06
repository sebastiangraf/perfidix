package org.perfidix.Perclipse.views;

import org.eclipse.jdt.internal.ui.viewsupport.ColoredViewersManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.PageBook;
import org.perfidix.Perclipse.viewTreeTestdaten.Person;
import org.perfidix.Perclipse.viewTreeTestdaten.TreeDataProvider;

public class BenchViewer {

	private BenchView benchView;

	private Clipboard clipboard;
	private Image hierarchyIcon;
	private PageBook viewerBook;
	private TreeViewer treeViewer;

	private int layoutMode;

	public BenchViewer(Composite parent, Clipboard clipboard, BenchView runner) {
		benchView = runner;
		this.clipboard = clipboard;

		hierarchyIcon = BenchView.createImage("icons/time.png"); //$NON-NLS-1$
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}

		});

		layoutMode = BenchView.LAYOUT_HIERARCHICAL;

		createBenchViewers(parent);

		// registerViewersRefresh();
		//         
		// initContextMenu();
	}

	private void createBenchViewers(Composite parent) {
		 viewerBook= new PageBook(parent, SWT.NONE | SWT.BORDER);
		 viewerBook.setLayoutData(new GridData(GridData.FILL_BOTH));

		 
		 
		treeViewer = new TreeViewer(viewerBook, SWT.NONE) ;
		treeViewer.setContentProvider(new BenchTreeContentProvider());
		treeViewer.setLabelProvider(new BenchTreeLabelProvider());
		treeViewer.setInput(TreeDataProvider.exampleData());
		//treeViewer.setInput(new TreeDataProvider[] {new TreeDataProvider("MyMan", 999, 15.898755)});
		


		viewerBook.showPage(treeViewer.getTree());
	}

	private void disposeIcons() {
		hierarchyIcon.dispose();
	}

}
