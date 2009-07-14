package org.perfidix.Perclipse.views;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.perfidix.Perclipse.model.BenchRunSession;
import org.perfidix.Perclipse.util.ShowJavaElementInJavaEditor;
import org.perfidix.Perclipse.viewTreeTestdaten.TreeDataProvider;

public class BenchViewer {

	private BenchView benchView;
	private BenchRunSession benchRunSession;

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

		// initContextMenu();
	}

	private void createBenchViewers(Composite parent) {
		viewerBook = new PageBook(parent, SWT.NONE | SWT.BORDER);
		viewerBook.setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer = new TreeViewer(viewerBook, SWT.NONE);
		treeViewer.setContentProvider(new BenchTreeContentProvider());
		treeViewer.setLabelProvider(new BenchTreeLabelProvider());
		treeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event){
				
				TreeSelection treeSelection =(TreeSelection) event.getSelection();
				
				ShowJavaElementInJavaEditor elementInJavaEditor = new ShowJavaElementInJavaEditor(treeSelection.getFirstElement());
			}
		});
		treeViewer.setInput(null);
		viewerBook.showPage(treeViewer.getTree());
	}

	public void processChangesInUI(BenchRunSession benchRunSession) {

		//benchRunSession = BenchRunSession.getInstance();
		this.benchRunSession=benchRunSession;

		if (benchRunSession.getBenchedClasses() == null) {
			treeViewer.setInput(null);

			return;
		}

		List classList = benchRunSession.getBenchedClasses();
		TreeDataProvider dataProvider[] = new TreeDataProvider[classList.size()];
		for (Object treeDataProvider : classList) {
			dataProvider[classList.indexOf(treeDataProvider)] = new TreeDataProvider(
					treeDataProvider.toString());

		}
		treeViewer.setInput(dataProvider);

	}

	public synchronized void registerActiveSession(BenchRunSession runSession) {
		benchRunSession = runSession;

	}

	private void disposeIcons() {
		hierarchyIcon.dispose();
	}

}
