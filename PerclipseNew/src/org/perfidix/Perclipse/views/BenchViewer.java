package org.perfidix.Perclipse.views;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.PageBook;
import org.perfidix.Perclipse.model.BenchRunSession;
import org.perfidix.Perclipse.model.JavaElementsWithTotalRuns;
import org.perfidix.Perclipse.viewTreeData.TreeDataProvider;

/**
 * The BenchViewer class is responsible for displaying the java elements which
 * will be benched during the perfidix process. It depicts the elements in the
 * eclipse view BenchView.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewer {

    private final class BenchSelectionListener
            implements ISelectionChangedListener {
        public void selectionChanged(SelectionChangedEvent event) {
            handleSelected();
        }
    }

    private final class BenchOpenListener extends SelectionAdapter {
        public void widgetDefaultSelected(SelectionEvent e) {
            handleDefaultSelected();
        }
    }

    private BenchRunSession benchRunSession;
    private Image hierarchyIcon;
    private PageBook viewerBook;
    private TreeViewer treeViewer;
    private SelectionProviderMediator selectionProvider;
    private BenchView view;
    private TreeDataProvider dataProvider[];
    private boolean dataFilled=false;
    private int sameLaunch=0;
    private List<?> classList;
    

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
    public BenchViewer(Composite parent, BenchView view) {
        hierarchyIcon = BenchView.createImage("icons/time.png"); //$NON-NLS-1$
        parent.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
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
    private void createBenchViewers(Composite parent) {
        viewerBook = new PageBook(parent, SWT.NONE | SWT.BORDER);
        viewerBook.setLayoutData(new GridData(GridData.FILL_BOTH));
        treeViewer = new TreeViewer(viewerBook, SWT.NONE);
        treeViewer.setContentProvider(new BenchTreeContentProvider());
        treeViewer.setLabelProvider(new BenchTreeLabelProvider());
        selectionProvider =
                new SelectionProviderMediator(
                        new StructuredViewer[] { treeViewer }, treeViewer);
        selectionProvider
                .addSelectionChangedListener(new BenchSelectionListener());
        BenchOpenListener openListener = new BenchOpenListener();
        treeViewer.getTree().addSelectionListener(openListener);

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

        List<JavaElementsWithTotalRuns> treeDataRunnin=benchRunSession.getBenchElements();
        if (treeDataRunnin == null) {

            treeViewer.setInput(null);

            return;
        }
        
        int launchHash=treeDataRunnin.hashCode();
      
        if(dataFilled==false || sameLaunch!=launchHash){
            initData(launchHash);
        }else{
            
            updateCountersInTree(benchRunSession.getCurrentRunElement());
            
        }

    }
    
    /**
     * This method updates each counter in the tree view of our benched java elements.
     * @param javaElementsWithTotalRuns The data of the running session.
     */
    private void updateCountersInTree(JavaElementsWithTotalRuns javaElementsWithTotalRuns) {
       TreeDataProvider searchedItem = null;
        for (Object item : dataProvider) {
            if(item instanceof TreeDataProvider){
                if((((TreeDataProvider) item).getParentElementName()).equals(javaElementsWithTotalRuns.getJavaElement())){
                   searchedItem=(TreeDataProvider) item;
                   break;
                }
            }
        }
        searchedItem.updateCurrentBench();
        treeViewer.update(searchedItem, null);

        
    }

    /**
     * This method initializes the tree data.
     * @param launchHash The hash value of our current launch.
     */
    private void initData(int launchHash){
        if(treeViewer.getTree()!=null){
            treeViewer.getTree().removeAll();
        }
        sameLaunch=launchHash;
        classList = benchRunSession.getBenchElements();

            dataProvider = new TreeDataProvider[classList.size()];
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
            dataFilled=true;
        
    }

    /**
     * This method handles the selection within the tree viewer.
     */
    public void handleSelected() {
        IStructuredSelection selection =
                (IStructuredSelection) selectionProvider.getSelection();
        TreeDataProvider element = null;
        if (selection.size() == 1) {
            System.out.println(selection.getFirstElement());
            element = (TreeDataProvider) selection.getFirstElement();
            System.out.println("element: " + element);
        }
        view.handleBenchSelection(element);

    }

    /**
     * This method is responsible for disposing the icon.
     */
    private void disposeIcons() {
        hierarchyIcon.dispose();
    }

    /**
     * This method handle the event for the open action of the editor.
     */
    public void handleDefaultSelected() {
        IStructuredSelection structuredSelection =
                (IStructuredSelection) selectionProvider.getSelection();
        if (structuredSelection.size() != 1) {
            return;
        }
        TreeDataProvider elementName =
                (TreeDataProvider) structuredSelection.getFirstElement();
        // I Do nothing
        System.out.println("I did it");
    }
    
    
   
}
