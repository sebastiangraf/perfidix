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

import org.perfidix.AbstractConfig;
import org.perfidix.Benchmark;
import org.perfidix.Perfidix;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * The SocketAdapter is the main-class for registration of the classes that will
 * be benched and creation of the socket stub to the ide view.
 * 
 * @author Lukas Lewandowski, University of Konstanz
 * @author Sebastian Graf, University of Konstanz
 */
public final class SocketAdapter {

    /** Instance for this run of the adapter */
    private transient Benchmark benchmark;

    /** View instance for communicating with the perclipse plugin */
    private transient final SocketViewProgressUpdater view;

    /**
     * private constructor.
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws SocketViewException
     */
    private SocketAdapter(final int port, final String[] classes)
            throws SocketViewException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        view = new SocketViewProgressUpdater(null, port);

        // config adaptions for including the view
        final AbstractConfig oldConf = Perfidix.getConfiguration(classes);
        final AbstractOutput[] outputs =
                new AbstractOutput[oldConf.getListener().length + 1];
        System.arraycopy(oldConf.getListener(), 0, outputs, 0, oldConf
                .getListener().length);
        outputs[outputs.length - 1] = new SocketListener(view);

        // Building up the benchmark object
        final AbstractConfig newConf =
                new AbstractConfig(
                        oldConf.getRuns(), oldConf.getMeters(), outputs,
                        oldConf.getArrangement(), oldConf.getGcProb()) {
                };
        benchmark = new Benchmark(newConf);

    }

    /**
     * Registering all classes and getting a mapping with the Methods and the
     * corresponding overall runs
     * 
     * @param classNames
     *            the names of the classes to be benched
     */
    private void registerClasses(final String[] classNames)
            throws SocketViewException {
        try {
            benchmark = Perfidix.setUpBenchmark(classNames, benchmark);

            final Map<BenchmarkMethod, Integer> vals =
                    benchmark.getNumberOfMethodsAndRuns();
            view.initProgressView(vals);

        } catch (final ClassNotFoundException e2) {
            view.updateErrorInElement(e2.toString(), e2);
        }
    }

    /**
     * This method starts the bench progress with the registered classes.
     * 
     * @throws SocketViewException
     */
    private void runBenchmark() throws SocketViewException {
        // final BenchmarkResult res = benchmark.run(new SocketListener(view));
        final BenchmarkResult res = benchmark.run();
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
        int viewPort = 0; // NOPMD by Sebastian on 26.08.09 21:10
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
        try {
            final SocketAdapter adapter =
                    new SocketAdapter(viewPort, classList
                            .toArray(new String[classList.size()]));

            adapter.registerClasses(classList.toArray(new String[classList
                    .size()]));
            adapter.runBenchmark();
        } catch (final SocketViewException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        //

        /****/

    }

}
