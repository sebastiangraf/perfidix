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
 * $Id: ExceptionTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;

import org.junit.Test;

public class ExceptionTest {

	@Test
	public void testWithoutException() {
		final ExceptionBench test = new ExceptionBench();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testWithException() {
		final ExceptionBench test = new ExceptionBench();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(true);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	class ExceptionBench {
		
		@Bench
		public void bench() throws Exception {
			FileReader reader = new FileReader("/tmp/tralalaMichGibtsNet");
			reader.close();
		}
	}
}
