package org.perfidix.Perclipse.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This class is responsible for the counter panel in the eclipse view of
 * Perclipse. It updates the counter (runs, errors) which are sended by the
 * perfidix benching process.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchViewCounterPanel extends Composite {

    private Text benchRuns;
    private Text benchErrors;
    private int totalRuns;
    private final Image benchRunImage = BenchView.createImage("icons/time.png");
    private final Image benchErrorImage =
            BenchView.createImage("icons/error.png");

    /**
     * The constructor creates the counter panel for the BenchView.
     * 
     * @param parent
     *            This param is the composite in which the panel has to be
     *            created. In our case it is the BenchView.
     */
    public BenchViewCounterPanel(Composite parent) {
        super(parent, SWT.WRAP);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 9;
        gridLayout.makeColumnsEqualWidth = false;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);

        benchRuns = createLabel("Runs: ", benchRunImage, " 0/0 ");

        benchErrors = createLabel("Errors: ", benchErrorImage, " 0 ");

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                disposeIcons();
            }
        });

    }

    /**
     * This method is responsible for creating a label.
     * 
     * @param labelName
     * @param image
     * @param initValue
     * @return This method returns the Text widget containing the labeling and
     *         additional stuff.
     */
    private Text createLabel(String labelName, Image image, String initValue) {

        if (labelName != null && initValue != null) {
            Label label = new Label(this, SWT.NONE);
            if (image != null) {
                image.setBackground(label.getBackground());
                label.setImage(image);
            }

            label.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_BEGINNING));

            label = new Label(this, SWT.NONE);
            label.setText(labelName);
            label.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_BEGINNING));

            Text text = new Text(this, SWT.READ_ONLY);
            text.setText(initValue);
            text.setBackground(getDisplay().getSystemColor(
                    SWT.COLOR_WIDGET_BACKGROUND));
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                    | GridData.HORIZONTAL_ALIGN_BEGINNING));

            return text;
        }
        return null;
    }

    /**
     * This method resets the values of the counter labels.
     */
    public void resetRuns() {
        setBenchRuns(0);
        setBenchErrors(0);
        setTotalRuns(0);

    }

    /**
     * This method returns the totalRuns value.
     * 
     * @return the total runs.
     */
    public int getTotalRuns() {
        return totalRuns;
    }

    /**
     * Setter of total runs for the label in the view.
     * 
     * @param totalRuns
     *            The total runs value.
     */
    public void setTotalRuns(int totalRuns) {
        this.totalRuns = totalRuns;

    }

    /**
     * Setter of the current run value of the label in the view.
     * 
     * @param benchRuns
     *            This param contains the value of the current bench run.
     */
    public void setBenchRuns(int benchRuns) {

        this.benchRuns.setText(benchRuns + "/" + totalRuns);

        this.benchRuns.redraw();

    }

    /**
     * Setter of occurred errors.
     * 
     * @param benchErrors
     *            The int value for upcoming bench runs.
     */
    public void setBenchErrors(int benchErrors) {
        this.benchErrors.setText(Integer.toString(benchErrors));
        redraw();
    }

    /**
     * This method is responsible for disposing of used icons.
     */
    private void disposeIcons() {
        benchErrorImage.dispose();
        benchRunImage.dispose();

    }
}
