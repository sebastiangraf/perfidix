/*
 * Copyright 2009 Distributed Systems Group, University of Konstanz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 *
 */
package org.perfidix.perclipse.model;

import org.perfidix.perclipse.launcher.PerclipseActivator;
import org.perfidix.perclipse.views.BenchView;

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
    public void updateView(final BenchRunSession benchSession) {
        final BenchView benchview = PerclipseActivator.getDefault().getBenchView();
        if (benchview != null && benchSession != null) {
            // benchview.stopUpdateJobs();
            benchview.startUpdateJobs(benchSession);
        }
    }

}
