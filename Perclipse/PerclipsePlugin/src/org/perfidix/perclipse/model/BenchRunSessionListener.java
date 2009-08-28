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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.perfidix.perclipse.launcher.PerclipseActivator;

/**
 * This class is the implementation of the interface
 * {@link IBenchRunSessionListener}. It receives data from the
 * PerclipseViewSkeleton and sets the BenchRunSession.
 * 
 * @author Lewandowski Lukas, DiSy, University of Konstanz
 */
public class BenchRunSessionListener implements IBenchRunSessionListener {

    final private transient BenchRunSession runSession;
    final private transient BenchRunViewUpdater viewUpdater;
    private transient Map<String, Integer> mapElements;
    private transient boolean finished = false;

    /**
     * The constructor creates new instances of the BenchRunSession and
     * BenchRunViewUpdater
     */
    public BenchRunSessionListener() {
        runSession = new BenchRunSession();
        viewUpdater = new BenchRunViewUpdater();
    }

    /** {@inheritDoc} */
    public void initTotalBenchProgress(
            final Map<String, Integer> benchElements) {
        int totalRun = 0;
        mapElements = (HashMap<String, Integer>) benchElements;
        final List<JavaElementsWithTotalRuns> list =
                new ArrayList<JavaElementsWithTotalRuns>();
        if (mapElements != null && !mapElements.isEmpty()) {
            final Set<String> theSet = mapElements.keySet();
            for (String elementName : theSet) {
                list.add(new JavaElementsWithTotalRuns(elementName, mapElements // NOPMD by IceMan on 27.08.09 22:50
                        .get(elementName)));
                totalRun = totalRun + mapElements.get(elementName);
            }
        }
        runSession.setBenchRunSession(totalRun, list);
        viewUpdater.updateView(runSession);
    }

    /** {@inheritDoc} */
    public void updateCurrentRun(final String currentElement) {
        if (mapElements != null && mapElements.containsKey(currentElement)) {
            runSession.setCurrentRun(currentElement);
            viewUpdater.updateView(runSession);
        }
    }

    /** {@inheritDoc} */
    public void updateError(final String element, final String exception) {
        if (mapElements != null && mapElements.containsKey(element)) {
            runSession.updateError(element);
            if (exception != null) {
                PerclipseActivator.logInfo("An exception occurred in ".concat(
                        element).concat(
                        " - Exception in run="
                                + runSession.getCurrentCount()
                                + ": ").concat(exception));
            }
            viewUpdater.updateView(runSession);
        }
    }

    /** {@inheritDoc} */
    public void finishedBenchRuns() {
        finished=true;
        runSession.setFinished(finished);

    }

    /**
     * @return Return finished.
     */
    public boolean isFinished() {
        return finished;
    }

}
