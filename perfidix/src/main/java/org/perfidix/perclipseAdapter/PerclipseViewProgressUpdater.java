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
package org.perfidix.perclipseAdapter;

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
public class PerclipseViewProgressUpdater {

    private PerclipseViewStub viewStub;

    /**
     * The constructor initializes the host and port for creation a client
     * socket.
     * 
     * @param host
     *            Host is the host name of the view provider.
     * @param port
     *            Port represent the port number of the eclipse view.
     */
    public PerclipseViewProgressUpdater(String host, int port) {
        if (host == "" || host == null)
            host = "localhost";
        viewStub = new PerclipseViewStub(host, port);

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
            Set<BenchmarkMethod> methodSet = mapping.keySet();

            HashMap<String, Integer> finalMap =
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
     * @param fullQualifiedElementName
     *            This param represents the java element which is currently
     *            benched.
     */
    public void updateCurrentElement(String fullQualifiedElementName) {
        if (fullQualifiedElementName != null) {
            viewStub.updateCurrentRun(fullQualifiedElementName);
        }
    }

    /**
     * This method informs the view that an error occurred while benching the
     * current element.
     * 
     * @param fullQualifiedElementName
     *            Element represents the java element which has not been
     *            executed successfully.
     */
    public void updateErrorInElement(String fullQualifiedElementName) {
        if (fullQualifiedElementName != null) {
            viewStub.updateError(fullQualifiedElementName);
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
