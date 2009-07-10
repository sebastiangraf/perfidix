package org.perfidix.Perclipse.views;

import java.text.MessageFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class BenchViewCounterPanel extends Composite {

	private Text benchRuns;
	private Text benchErrors;
	private int totalRuns;
	private int ignoredRuns;

	public BenchViewCounterPanel(Composite parent) {
		super(parent, SWT.WRAP);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 9;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		benchRuns = createLabel("Runs: ", null, " 0/0 ");
		benchErrors = createLabel("Errors: ", null, " 0 ");


		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}
		});

	}

	private void disposeIcons() {
		// here you have to dispose used images

	}

	private Text createLabel(String labelName, Image image, String initValue) {

		Label label = new Label(this, SWT.NONE);
		if (image != null) {
			image.setBackground(label.getBackground());
			label.setImage(image);
		}

		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		label = new Label(this, SWT.NONE);
		label.setText(labelName);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Text text = new Text(this, SWT.READ_ONLY);
		text.setText(initValue);
		text.setBackground(getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_BEGINNING));

		return text;
	}

	public void resetRuns() {
		setBenchRuns(0, 0);
		setBenchErrors(0);
		setTotalRuns(0);

	}

	public int getTotalRuns() {
		return totalRuns;
	}

	public void setTotalRuns(int totalRuns) {
		this.totalRuns = totalRuns;
		
	}

	public void setBenchRuns(int benchRuns, int ignored) {
		String runString;
		if (ignored == 0) {
			runString = MessageFormat.format("test", (Object[]) new String[] {
					Integer.toString(benchRuns), Integer.toString(totalRuns) });
		} else {
			runString = MessageFormat.format("ignored",
					(Object[]) new String[] { Integer.toString(benchRuns),
							Integer.toString(totalRuns) });
		}
		this.benchRuns.setText(benchRuns+"/"+totalRuns+" ("+ignored+")");
		
		if(ignoredRuns==0 && ignored> 0 || ignoredRuns != 0 && ignored==0){
			layout();
		}
		else {
		this.benchRuns.redraw();
		redraw();
		}
		ignoredRuns = ignored;

	}


	public void setBenchErrors(int benchErrors) {
		this.benchErrors.setText(Integer.toString(benchErrors));
		redraw();
	}

}
