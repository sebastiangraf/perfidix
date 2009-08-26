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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.perfidix.Benchmark;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

//TODO Javadoc
/**
 * @author Lukas Lewandowski, University of Konstanz
 * @author Sebastian Graf, University of Konstanz
 */
public final class SocketAdapter {

    /** Instance for this run of the adapter */
    private transient final Benchmark benchmark;

    /** View instance for communicating with the perclipse plugin */
    private transient final SocketViewProgressUpdater view;

    /**
     * private constructor.
     */
    private SocketAdapter(final int port) {
        benchmark = new Benchmark();
        view = new SocketViewProgressUpdater(null, port);
    }

    /**
     * Registering all classes and getting a mapping with the Methods and the
     * corresponding overall runs
     * 
     * @param classNames
     *            the names of the classes to be benched
     */
    private void registerClasses(final List<String> classNames) {
        for (final String className : classNames) {
            try {
                benchmark.add(Class.forName(className));
            } catch (final ClassNotFoundException e) {
                // TODO view#updateErrorInElement

            }
        }
        final Map<BenchmarkMethod, Integer> vals =
                benchmark.getNumberOfMethodsAndRuns();
        view.initProgressView(vals);
    }

    // TODO javadoc
    private void runBenchmark() {
        final BenchmarkResult res =
                benchmark.run(new SocketListener(view));
        new TabularSummaryOutput().visitBenchmark(res);
        view.finished();
    }

    /**
     * Main method for invoking benchs with classes as strings.
     * 
     * @param args
     *            the classes
     */
    public static void main(final String[] args) {
        // init of the connection to the plugin
        int viewPort = 0;
        final List<String> classList = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-Port")) {
                if (args[i + 1] != null) {
                    viewPort = Integer.parseInt(args[i + 1]);
                }
                break;
            } else {
                classList.add(args[i]);
            }
        }

        final SocketAdapter adapter = new SocketAdapter(viewPort);
        adapter.registerClasses(classList);
        adapter.runBenchmark();
        //

        /****/

    }
}
