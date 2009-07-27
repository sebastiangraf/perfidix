package org.perfidix.Perclipse.model;

import org.perfidix.Perclipse.launcher.PerclipseActivator;
import org.perfidix.Perclipse.views.BenchView;
import org.perfidix.Perclipse.model.BenchRunSession;

public class BenchRunViewUpdater {



    public void updateView(BenchRunSession benchSession) {
        BenchView benchview =
            PerclipseActivator.getDefault().getBenchView();
        if (benchview != null)
            benchview.startUpdateJobs(benchSession);
    }

}
