/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.perfidix.socketadapter;

import org.perfidix.AbstractConfig;
import org.perfidix.Benchmark;
import org.perfidix.Perfidix;
import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.PerfidixMethodCheckException;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

import java.util.*;

/**
 * The SocketAdapter is the main-class for registration of the classes that will
 * be benched and creation of the socket stub to the ide view.
 *
 * @author Lukas Lewandowski, University of Konstanz
 * @author Sebastian Graf, University of Konstanz
 */
public final class SocketAdapter {

	/**
	 * View instance for communicating with the perclipse plugin
	 */
	private transient final IUpdater view;
	/**
	 * Instance for this run of the adapter
	 */
	private transient Benchmark benchmark;

	/**
	 * Public constructor.
	 *
	 * @param update
	 *            updater for this output
	 * @param classes
	 *            to be benchmarked
	 * @throws IllegalAccessException
	 *             if communication with socket fails
	 * @throws InstantiationException
	 *             if initiation fails
	 * @throws ClassNotFoundException
	 *             if class is not found
	 * @throws SocketViewException
	 *             if socket communication fails
	 */
	public SocketAdapter(final IUpdater update, final String... classes)
			throws SocketViewException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		view = update;

		// config adaptions for including the view
		final AbstractConfig oldConf = Perfidix.getConfiguration(classes);
		final AbstractOutput[] outputs = new AbstractOutput[oldConf
				.getListener().length + 1];
		System.arraycopy(oldConf.getListener(), 0, outputs, 0,
				oldConf.getListener().length);
		outputs[outputs.length - 1] = new SocketListener(view);

		Set<AbstractMeter> meters = new HashSet<AbstractMeter>();
		meters.addAll(Arrays.asList(oldConf.getMeters()));

		Set<AbstractOutput> listeners = new HashSet<AbstractOutput>();
		listeners.addAll(Arrays.asList(outputs));

		// Building up the benchmark object
		final AbstractConfig newConf = new AbstractConfig(oldConf.getRuns(),
				meters, listeners, oldConf.getArrangement(),
				oldConf.getGcProb()) {
		};
		benchmark = new Benchmark(newConf);

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
		try {

			final IUpdater updater = new SocketViewProgressUpdater(null,
					viewPort);

			final SocketAdapter adapter = new SocketAdapter(updater,
					classList.toArray(new String[classList.size()]));

			adapter.registerClasses(classList.toArray(new String[classList
					.size()]));
			adapter.runBenchmark();
		} catch (final SocketViewException | ClassNotFoundException
				| InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Registering all classes and getting a mapping with the Methods and the
	 * corresponding overall runs
	 *
	 * @param classNames
	 *            the names of the classes to be benched
	 * @throws SocketViewException
	 *             exception if socket communication fails
	 * @return if register succeeds or not
	 */
	public boolean registerClasses(final String... classNames)
			throws SocketViewException {
		try {
			benchmark = Perfidix.setUpBenchmark(classNames, benchmark);

			final Map<BenchmarkMethod, Integer> vals = benchmark
					.getNumberOfMethodsAndRuns();
			return view.initProgressView(vals);
		} catch (final ClassNotFoundException | PerfidixMethodCheckException e2) {
			return view.updateErrorInElement(e2.toString(), e2);
		}
	}

	/**
	 * This method starts the bench progress with the registered classes.
	 *
	 * @throws SocketViewException
	 *             if runs can not be communicated
	 * @return if run succeeds or not
	 */
	public boolean runBenchmark() throws SocketViewException {
		final BenchmarkResult res = benchmark.run();
		new TabularSummaryOutput().visitBenchmark(res);
		view.finished();
		return true;
	}

}
