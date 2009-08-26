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
package org.perfidix.socketadapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.perfidix.element.BenchmarkMethod;

/**
 * This class creates the connection to the eclipse view via
 * {@link PerclipseViewStub}. It contains the methods which update the view to
 * inform about the bench process progress.
 * 
 * @author Lewandowski Lukas, University of Konstanz
 */
public final class PerclipseViewProgressUpdater {

    private transient final PerclipseViewStub viewStub;

    /**
     * The constructor initializes the host and port for creation a client
     * socket.
     * 
     * @param host
     *            Host is the host name of the view provider.
     * @param port
     *            Port represent the port number of the eclipse view.
     */
    public PerclipseViewProgressUpdater(final String host, final int port) {
        String strubParam = host;
        if (host == null || host.equals("")) {
            strubParam = "localhost";
        }
        viewStub = new PerclipseViewStub(strubParam, port);

    }

    /**
     * This method initializes the values of the eclipse view and resets the
     * progress bar.
     * 
     * @param mapping
     *            a mapping with all methods to benchmark and the related runs
     */
    public void initProgressView(
            final Map<BenchmarkMethod, Integer> mapping) {
        if (mapping != null) {
            final Set<BenchmarkMethod> methodSet = mapping.keySet();

            final Map<String, Integer> finalMap =
                    new HashMap<String, Integer>();
            for (BenchmarkMethod benchmarkMethod : methodSet) {

                finalMap.put(
                        benchmarkMethod.getMethodWithClassName(), mapping
                                .get(benchmarkMethod));
            }

            viewStub.initTotalBenchProgress(finalMap);
        }
    }

    /**
     * This method notifies the eclipse view which element is currently benched.
     * 
     * @param name
     *            This param represents the java element which is currently
     *            benched and which is fully qualified.
     */
    public void updateCurrentElement(final String name) {
        if (name != null) {
            viewStub.updateCurrentRun(name);
        }
    }

    /**
     * This method informs the view that an error occurred while benching the
     * current element.
     * 
     * @param name
     *            Element represents the java element which has not been
     *            executed successfully.
     */
    public void updateErrorInElement(final String name) {
        if (name != null) {
            viewStub.updateError(name);
        }
    }

    /**
     * This method notifies the view that all benches have been executed and the
     * bench progress is finished.
     */
    public void finished() {
        viewStub.finishedBenchRuns();
    }

}
