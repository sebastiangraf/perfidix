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
 * $Id: NormalAnnotationTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class NormalAnnotationTest {

	private boolean build;

	private boolean beforeMethod;
	
	private boolean setUp;

	private boolean bench1;

	private boolean bench2;

	private boolean tearDown;
	
	private boolean afterMethod;

	private boolean cleanUp;

	public void setUp() {
		build = false;
		beforeMethod = false;
		setUp = false;
		bench1 = false;
		bench2 = false;
		tearDown = false;
		afterMethod = false;
		cleanUp = false;
	}

	@Test
	public void test() {
		final NormalBench benchclass = new NormalBench();
		final Benchmark benchmark = new Benchmark();
		benchmark.add(benchclass);
		benchmark.run(1);
		assertTrue(build);
		assertTrue(beforeMethod);
		assertTrue(setUp);
		assertTrue(bench1);
		assertTrue(bench2);
		assertTrue(tearDown);
		assertTrue(afterMethod);
		assertTrue(cleanUp);
	}

	class NormalBench {
		@BeforeBenchClass
		public void build() {
			build = true;
		}

		@BeforeBenchMethod
		public void beforeMethod() {
			beforeMethod = true;
		}
		
		@BeforeBenchRun
		public void setUp() {
			setUp = true;
		}

		@Bench
		public void bench1() {
			bench1 = true;
		}

		@Bench
		public void bench2() {
			bench2 = true;
		}

		@AfterBenchRun
		public void tearDown() {
			tearDown = true;
		}

		@AfterBenchMethod
		public void afterMethod() {
			afterMethod = true;
		}
		
		@AfterBenchClass
		public void cleanUp() {
			cleanUp = true;
		}
	}
}
