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
 * $Id: AnnotationBeforeAfterTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AnnotationBeforeAfterTest {

	
	
	@Test
	public void testBeforeClassParam() {
		final BeforeClassParam test = new BeforeClassParam();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testBeforeClassReturn() {
		final BeforeClassReturn test = new BeforeClassReturn();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterClassParam() {
		final AfterClassParam test = new AfterClassParam();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterClassReturn() {
		final AfterClassReturn test = new AfterClassReturn();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateAfterClass() {
		final DuplicateAfterClass test = new DuplicateAfterClass();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateBeforeClass() {
		final DuplicateBeforeClass test = new DuplicateBeforeClass();
		final Benchmark bench = new Benchmark();
		bench.shouldThrowException(false);
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}

	@Test
	public void testBeforeMethodParam() {
		final BeforeMethodParam test = new BeforeMethodParam();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testBeforeMethodReturn() {
		final BeforeMethodReturn test = new BeforeMethodReturn();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterMethodParam() {
		final AfterMethodParam test = new AfterMethodParam();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterMethodReturn() {
		final AfterMethodReturn test = new AfterMethodReturn();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateAfterMethod() {
		final DuplicateAfterMethod test = new DuplicateAfterMethod();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateBeforeMethod() {
		final DuplicateBeforeMethod test = new DuplicateBeforeMethod();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}

	@Test
	public void testBeforeParam() {
		final BeforeParam test = new BeforeParam();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testBeforeReturn() {
		final BeforeReturn test = new BeforeReturn();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterParam() {
		final AfterParam test = new AfterParam();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testAfterReturn() {
		final AfterReturn test = new AfterReturn();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateAfter() {
		final DuplicateAfter test = new DuplicateAfter();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	@Test
	public void testDuplicateBefore() {
		final DuplicateBefore test = new DuplicateBefore();
		final Benchmark bench = new Benchmark();
		assertFalse(bench.exceptionsThrown());
		bench.setLogger(true);
		bench.add(test);
		bench.run();
		assertTrue(bench.exceptionsThrown());
	}
	
	class DuplicateBefore {
		@BeforeEachBenchRun
		public void setUp1(){
		}
		@BeforeEachBenchRun
		public void setUp2(){
		}
		@Bench
		public void bench() {
		}
	}
	
	class DuplicateAfter {
		@Bench
		public void bench() {
		}
		@AfterEachBenchRun
		public void tearDown1() {
		}
		@AfterEachBenchRun
		public void tearDown2() {
		}
	}
	
	class AfterReturn {
		@Bench
		public void bench() {
		}
		@AfterEachBenchRun
		public Object tearDown() {
			return null;
		}
	}
	
	class AfterParam {
		@Bench
		public void bench() {
		}
		@AfterEachBenchRun
		public void tearDown(Object obj) {
		}
	}
	
	class BeforeReturn {
		@BeforeEachBenchRun
		public Object setUp() {
			return null;
		}
		@Bench
		public void bench() {
		}
	}
	
	class BeforeParam {
		@BeforeEachBenchRun
		public void setUp(Object obj) {
		}
		@Bench
		public void bench() {
		}
	}
	
	class DuplicateBeforeMethod {
		@BeforeFirstBenchRun
		public void build1(){
		}
		@BeforeFirstBenchRun
		public void build2(){
		}
		@Bench
		public void bench() {
		}
	}
	
	class DuplicateAfterMethod {
		@Bench
		public void bench() {
		}
		@AfterFirstBenchRun
		public void cleanUp1() {
		}
		@AfterFirstBenchRun
		public void cleanUp2() {
		}
	}
	
	class AfterMethodReturn {
		@Bench
		public void bench() {
		}
		@AfterFirstBenchRun
		public Object cleanUp() {
			return null;
		}
	}
	
	class AfterMethodParam {
		@Bench
		public void bench() {
		}
		@AfterFirstBenchRun
		public void cleanUp(Object obj) {
		}
	}
	
	class BeforeMethodReturn {
		@BeforeFirstBenchRun
		public Object build() {
			return null;
		}
		@Bench
		public void bench() {
		}
	}
	
	class BeforeMethodParam {
		@BeforeFirstBenchRun
		public void build(Object obj) {
		}
		@Bench
		public void bench() {
		}
	}

	class DuplicateBeforeClass {
		@BeforeBenchClass
		public void build1(){
		}
		@BeforeBenchClass
		public void build2(){
		}
		@Bench
		public void bench() {
		}
	}
	
	class DuplicateAfterClass {
		@Bench
		public void bench() {
		}
		@AfterBenchClass
		public void cleanUp1() {
		}
		@AfterBenchClass
		public void cleanUp2() {
		}
	}
	
	class AfterClassReturn {
		@Bench
		public void bench() {
		}
		@AfterBenchClass
		public Object cleanUp() {
			return null;
		}
	}
	
	class AfterClassParam {
		@Bench
		public void bench() {
		}
		@AfterBenchClass
		public void cleanUp(Object obj) {
		}
	}
	
	class BeforeClassReturn {
		@BeforeBenchClass
		public Object build() {
			return null;
		}
		@Bench
		public void bench() {
		}
	}
	
	class BeforeClassParam {
		@BeforeBenchClass
		public void build(Object obj) {
		}
		@Bench
		public void bench() {
		}
	}
	
}
