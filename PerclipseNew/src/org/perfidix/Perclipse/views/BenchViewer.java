package org.perfidix.Perclipse.views;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.perfidix.Perclipse.util.BenchRunSession;
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
		treeViewer.setInput(null);
		viewerBook.showPage(treeViewer.getTree());
	}

	public void processChangesInUI() {

		benchRunSession = BenchRunSession.getInstance();

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

		// testRoot= fTestRunSession.getTestRoot();
		//
		// StructuredViewer viewer= getActiveViewer();
		// if (getActiveViewerNeedsRefresh()) {
		// clearUpdateAndExpansion();
		// setActiveViewerNeedsRefresh(false);
		// viewer.setInput(testRoot);
		//
		// } else {
		// Object[] toUpdate;
		// synchronized (this) {
		// toUpdate= fNeedUpdate.toArray();
		// fNeedUpdate.clear();
		// }
		// if (! fTreeNeedsRefresh && toUpdate.length > 0) {
		// if (fTreeHasFilter)
		// for (int i= 0; i < toUpdate.length; i++)
		// updateElementInTree((TestElement) toUpdate[i]);
		// else {
		// HashSet toUpdateWithParents= new HashSet();
		// toUpdateWithParents.addAll(Arrays.asList(toUpdate));
		// for (int i= 0; i < toUpdate.length; i++) {
		// TestElement parent= ((TestElement) toUpdate[i]).getParent();
		// while (parent != null) {
		// toUpdateWithParents.add(parent);
		// parent= parent.getParent();
		// }
		// }
		// fTreeViewer.update(toUpdateWithParents.toArray(), null);
		// }
		// }
		// if (! fTableNeedsRefresh && toUpdate.length > 0) {
		// if (fTableHasFilter)
		// for (int i= 0; i < toUpdate.length; i++)
		// updateElementInTable((TestElement) toUpdate[i]);
		// else
		// fTableViewer.update(toUpdate, null);
		// }
		// }
		// autoScrollInUI();

	}

	public synchronized void registerActiveSession(BenchRunSession runSession) {
		benchRunSession = runSession;
		// registerAutoScrollTarget(null);
		// registerViewersRefresh();
	}

	private void disposeIcons() {
		hierarchyIcon.dispose();
	}

}
