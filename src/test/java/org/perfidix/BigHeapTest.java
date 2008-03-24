/*
 * Copyright 2007 University of Konstanz
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
 * $Id: BigHeapTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class BigHeapTest extends PerfidixTest {

	/**
	 * some simple class allocating too much heap space. i'm not calculating
	 * this.
	 */
	public class SomeHeap {

		private Object[] arr;

		@Bench
		public void benchSomething() {

			arr = new Object[140000];
			// arr = new Object[14000000]; see readme. this would cause
			// a java heap space error with 512 MB heap size
			Arrays.fill(arr, new String());
		}

	}



	@Test
	public void testOne() {

		Benchmark a = new Benchmark("my benchmark");
		// SomeHeap h1 = new SomeHeap();
		// SomeHeap h2 = new SomeHeap();
		a.add(new SomeHeap());
		a.add(new SomeHeap());
		assertFalse(a.exceptionsThrown());
		a.run(1);
		assertFalse(a.exceptionsThrown());

	}

}
