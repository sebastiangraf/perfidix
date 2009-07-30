package org.perfidix.Perclipse.views;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.perfidix.Perclipse.model.BenchRunSession;
import org.perfidix.Perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.Perclipse.util.ShowJavaElementInJavaEditor;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * The BenchViewer class is responsible for displaying the java elements which
 * will be benched during the perfidix process. It depicts the elements in the
 * eclipse view BenchView.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewer {

    private BenchRunSession benchRunSession;
    private Image hierarchyIcon;
    private PageBook viewerBook;
    private TreeViewer treeViewer;

    /**
     * The constructor gets the parents composite and creates the BenchViewer.
     * The BenchViewer is part of the parents composite. It is also responsible
     * for adding an dispose listener.
     * 
     * @param parent
     *            This param represents the composite of the parent, which is
     *            declared in the BenchView.
     */
    public BenchViewer(Composite parent) {
        hierarchyIcon = BenchView.createImage("icons/time.png"); //$NON-NLS-1$
        parent.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                disposeIcons();
            }

        });
        createBenchViewers(parent);
    }

    /**
     * This method is responsible for creation of a viewer. The viewer has to
     * display the java elements which will be benched by the perfidix project.
     * 
     * @param parent
     *            The parent param represents the composite created in
     *            BenchView.
     */
    private void createBenchViewers(Composite parent) {
        viewerBook = new PageBook(parent, SWT.NONE | SWT.BORDER);
        viewerBook.setLayoutData(new GridData(GridData.FILL_BOTH));
        treeViewer = new TreeViewer(viewerBook, SWT.NONE);
        treeViewer.setContentProvider(new BenchTreeContentProvider());
        treeViewer.setLabelProvider(new BenchTreeLabelProvider());
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {

                TreeSelection treeSelection =
                        (TreeSelection) event.getSelection();

                // It just for testing purposes
                ShowJavaElementInJavaEditor elementInJavaEditor =
                        new ShowJavaElementInJavaEditor(treeSelection
                                .getFirstElement());
            }
        });
        treeViewer.setInput(null);
        viewerBook.showPage(treeViewer.getTree());
    }

    /**
     * This method is responsible for updating the BenchView. When a bench run
     * finished the viewer has to be updated and the corresponding java element
     * has to be updated.
     * 
     * @param benchRunSessionParam
     *            This param represents the running bench session and contains
     *            every information about the running bench process (data).
     */
    public void processChangesInUI(BenchRunSession benchRunSessionParam) {

        benchRunSession = benchRunSessionParam;

        if (benchRunSession.getBenchElements() == null) {
            treeViewer.setInput(null);

            return;
        }

        List<?> classList = benchRunSession.getBenchElements();
        TreeDataProvider dataProvider[] =
                new TreeDataProvider[classList.size()];
        for (Object treeDataProvider : classList) {
            dataProvider[classList.indexOf(treeDataProvider)] =
                    new TreeDataProvider(
                            ((JavaElementsWithTotalRuns) treeDataProvider)
                                    .getJavaElement(),
                            ((JavaElementsWithTotalRuns) treeDataProvider)
                                    .getTotalRuns(),
                            ((JavaElementsWithTotalRuns) treeDataProvider)
                                    .getCurrentRun());

        }
        treeViewer.setInput(dataProvider);

    }

    /**
     * This method is responsible for disposing the icon.
     */
    private void disposeIcons() {
        hierarchyIcon.dispose();
    }

}
