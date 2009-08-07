package org.perfidix.Perclipse.model;

import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.views.BenchView;

/**
 * This class is the connection to the view updater process. It start to update
 * the view.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchRunViewUpdater {

    /**
     * The updateView method gets a given benchSession and starts to update the
     * BenchView via the startUpdateJobs method.
     * 
     * @param benchSession
     *            The bench session which contain information about the complete
     *            running session process.
     */
    public void updateView(BenchRunSession benchSession) {
        BenchView benchview = PerclipseActivator.getDefault().getBenchView();
        if (benchview != null && benchSession != null) {
            // benchview.stopUpdateJobs();
            benchview.startUpdateJobs(benchSession);
        }
    }

}
