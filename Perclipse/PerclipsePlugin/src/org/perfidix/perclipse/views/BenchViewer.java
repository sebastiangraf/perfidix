/**
 * Copyright (c) 2011, University of Konstanz, Distributed Systems Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of Konstanz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.perclipse.views;

import java.util.List;

import org.eclipse.jdt.internal.ui.viewsupport.SelectionProviderMediator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.model.BenchRunSession;
import org.perfidix.perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.perclipse.viewtreedata.TreeDataProvider;

/**
 * The BenchViewer class is responsible for displaying the java elements which
 * will be benched during the perfidix process. It depicts the elements in the
 * eclipse view BenchView.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
@SuppressWarnings("restriction")
public class BenchViewer {

    private final class BenchSelectionListener
            implements ISelectionChangedListener {
        public void selectionChanged(final SelectionChangedEvent event) {
            handleSelected();
        }
    }

    private final class BenchOpenListener extends SelectionAdapter {
        @Override
        public void widgetDefaultSelected(final SelectionEvent event) {
            handleDefaultSelected();
        }
    }

    private transient BenchRunSession benchRunSession;
    private transient TreeViewer treeViewer;
    private transient SelectionProviderMediator selectionProvider;
    final private transient BenchView view;
    private transient TreeDataProvider dataProvider[];
    private transient boolean dataFilled = false;
    private transient int sameLaunch = 0;

    /**
     * The constructor gets the parents composite and creates the BenchViewer.
     * The BenchViewer is part of the parents composite. It is also responsible
     * for adding an dispose listener.
     * 
     * @param parent
     *            This param represents the composite of the parent, which is
     *            declared in the BenchView.
     * @param view
     *            The bench view.
     */
    public BenchViewer(final Composite parent, final BenchView view) {
        parent.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(final DisposeEvent event) {
                disposeIcons();
            }

        });
        this.view = view;
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
    private void createBenchViewers(final Composite parent) {
        final PageBook viewerBook = new PageBook(parent, SWT.NONE | SWT.BORDER);
        viewerBook.setLayoutData(new GridData(GridData.FILL_BOTH));
        treeViewer = new TreeViewer(viewerBook, SWT.NONE);
        treeViewer.setContentProvider(new BenchTreeContentProvider());
        treeViewer.setLabelProvider(new BenchTreeLabelProvider());
        selectionProvider =
                new SelectionProviderMediator(
                        new StructuredViewer[] { treeViewer }, treeViewer);
        selectionProvider
                .addSelectionChangedListener(new BenchSelectionListener());
        final BenchOpenListener openListener = new BenchOpenListener();
        treeViewer.getTree().addSelectionListener(openListener);

        viewerBook.showPage(treeViewer.getTree());
    }

    /**
     * This method is responsible for updating the BenchView. When a bench run
     * finished the viewer has to be updated and the corresponding java element
     * has to be updated.
     * 
     * @param bRSession
     *            This param represents the running bench session and contains
     *            every information about the running bench process (data).
     */
    public void processChangesInUI(final BenchRunSession bRSession) {

        benchRunSession = bRSession;
        if (bRSession != null) {
            final List<JavaElementsWithTotalRuns> treeDataRunnin =
                    benchRunSession.getBenchElements();
            if (treeDataRunnin == null || treeDataRunnin.isEmpty()) {

                treeViewer.setInput(null);

            } else {
                final int launchHash = treeDataRunnin.hashCode();

                if (dataFilled
                        && sameLaunch == launchHash
                        && benchRunSession.getCurrentRunElement() != null) {
                    updateCountersInTree(benchRunSession.getCurrentRunElement());
                } else {
                    initData(launchHash);
                }

            }

        }

    }

    /**
     * This method updates each counter in the tree view of our benched java
     * elements.
     * 
     * @param jElements
     *            The data of the running session.
     */
    private void updateCountersInTree(final JavaElementsWithTotalRuns jElements) {
        TreeDataProvider searchedItem = null;
        for (Object item : dataProvider) {
            if (item instanceof TreeDataProvider
                    && ((TreeDataProvider) item).getParentElementName().equals(
                            jElements.getJavaElement())) {

                searchedItem = (TreeDataProvider) item;
                searchedItem.updateCurrentBench(jElements.getCurrentRun());
                if (jElements.getErrorCount() > 0) {
                    searchedItem.updateCurrentBenchError(jElements
                            .getErrorCount());
                }
                break;

            }
        }
        treeViewer.update(searchedItem, null);

    }

    /**
     * This method initializes the tree data.
     * 
     * @param launchHash
     *            The hash value of our current launch.
     */
    private void initData(final int launchHash) {
        if (treeViewer.getTree() != null) {
            treeViewer.getTree().removeAll();
        }
        sameLaunch = launchHash;
        final List<?> classList = benchRunSession.getBenchElements();

        dataProvider = new TreeDataProvider[classList.size()];
        for (final Object treeDataProvider : classList) {
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
        dataFilled = true;

    }

    /**
     * This method handles the selection within the tree viewer.
     */
    public void handleSelected() {
        final IStructuredSelection selection =
                (IStructuredSelection) selectionProvider.getSelection();
        TreeDataProvider element = null;
        if (selection.size() == 1) {
            element = (TreeDataProvider) selection.getFirstElement();
        }
        view.handleBenchSelection(element);

    }

    /**
     * This method is responsible for disposing the icon.
     */
    private void disposeIcons() {
        // if(hierarchyIcon!=null) hierarchyIcon.dispose();
    }

    /**
     * This method handle the event for the open action of the editor.
     */
    public void handleDefaultSelected() {
        final IStructuredSelection strSelec =
                (IStructuredSelection) selectionProvider.getSelection();
        if (strSelec.size() == 1) {
            TreeDataProvider element = null;
            element = (TreeDataProvider) strSelec.getFirstElement();
            PerclipseActivator.logInfo(element.getParentElementName()
                    + " has been selected");
        }

    }

}
