package org.perfidix.Perclipse.views;


import org.eclipse.jdt.ui.ITypeHierarchyViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.part.ViewPart;

public class BenchView extends ViewPart {

	public final static String MY_VIEW_ID = "org.perfidix.Perclipse.views.BenchView";
	public final static int LAYOUT_HIERARCHICAL=1;
	private static final int VIEW_ORIENTATION_VERTICAL= 0;
	private static final int VIEW_ORIENTATION_HORIZONTAL= 1;
	private static final int VIEW_ORIENTATION_AUTOMATIC= 2;
	private PerfidixProgressBar progressBar;
	private BenchViewCounterPanel benchCounterPanel;
	private BenchViewer benchViewer;
	private SashForm sashForm;
	private Label label;
	private Composite counterComposite;
	private Composite parentComosite;
	private Clipboard clipboard;
	private int viewOrientation = VIEW_ORIENTATION_AUTOMATIC;
	private int currentOrientation;
	

	

	public BenchView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		parentComosite = parent;

		clipboard= new Clipboard(parent.getDisplay());
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		counterComposite = createProgressCountPanel(parent);
		counterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_BOTH));
		
		progressBar.reset(false, false, 20, 50);
		
		benchViewer= new BenchViewer(counterComposite, clipboard, this);
		

		
		


	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		// Set focus to my widget. For a label no sense but for more
		// complextypes

	}

	public void showProgress(Composite parent) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			Thread.sleep(500);
			label = new Label(parent, SWT.WRAP);
			label.setText("still working... ");
		}
	}

	protected Composite createProgressCountPanel(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		setCounterColumns(layout);

		benchCounterPanel = new BenchViewCounterPanel(composite);
		benchCounterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		progressBar = new PerfidixProgressBar(composite);
		progressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		

		return composite;
	}

	private void setCounterColumns(GridLayout layout) {

		// here you can modify the visualization if the orientation changes
		// (horizontal vs vertical)

	}

	public static Image createImage(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private void computeOrientation(){
//		if(viewOrientation != VIEW_ORIENTATION_AUTOMATIC){
//			currentOrientation=viewOrientation;
//			setOrientation(currentOrientation);
//		}
//		else{
//			Point size=this.parentComosite.getSize();
//			if(size.x!=0 && size.y!=0){
//				if(size.x>size.y){
//					setOrientation(VIEW_ORIENTATION_HORIZONTAL);
//				}
//				else{
//					setOrientation(VIEW_ORIENTATION_VERTICAL);
//				}
//			}
//		}
//	}
	
//	private void setOrientation(int orientation) {
//		        if ((sashForm == null) || sashForm.isDisposed())
//		            return;
//		        boolean horizontal = orientation == VIEW_ORIENTATION_HORIZONTAL;
//		        sashForm.setOrientation(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
//		        for (int i = 0; i < toggleOrientationActions.length; ++i)
//		            toggleOrientationActions[i].setChecked(viewOrientation == toggleOrientationActions[i].getOrientation());
//		        currentOrientation = orientation;
//		        GridLayout layout= (GridLayout) this.counterComposite.getLayout();
//		        setCounterColumns(layout);
//		        this.parentComosite.layout();
//		    }


}
