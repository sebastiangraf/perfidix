/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group
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
    public void initTotalBenchProgress(final Map<String, Integer> benchElements) {
        int totalRun = 0;
        mapElements = (HashMap<String, Integer>) benchElements;
        final List<JavaElementsWithTotalRuns> list =
                new ArrayList<JavaElementsWithTotalRuns>();
        if (mapElements != null && !mapElements.isEmpty()) {
            final Set<String> theSet = mapElements.keySet();
            for (String elementName : theSet) {
                list.add(new JavaElementsWithTotalRuns(elementName, mapElements
                // by
                        // IceMan
                        // on
                        // 27.08.09
                        // 22:50
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
        finished = true;
        runSession.setFinished(finished);

    }

    /**
     * @return Return finished.
     */
    public boolean isFinished() {
        return finished;
    }

}
