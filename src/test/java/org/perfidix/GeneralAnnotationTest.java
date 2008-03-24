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
 * $Id: ClassAnnotationTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
public class GeneralAnnotationTest {

	private static final int classAnnoRuns = 20;
	private static final int method1Runs = 50;
	private static final int method2Runs = 25;
	
	private int beforeClass;
	private int beforeMethod;
	private int setUp;
	private int tearDown;
	private int afterMethod;
	private int afterClass;
	private int bench1;
	private int bench2;
	
	@Before
	public void setUp() {
		beforeClass = 0;
		beforeMethod = 0;
		setUp = 0;
		bench1 = 0;
		bench2 = 0;
		tearDown = 0;
		afterMethod = 0;
		afterClass = 0;
	}
	
	@Test
	public void testClassAnnos() {
		final TestBenchClass benchClass = new TestBenchClass();
		final Benchmark bench = new Benchmark();
		bench.add(benchClass);
		bench.run();
		
		assertEquals(1, beforeClass);
		assertEquals(2, beforeMethod);
		assertEquals(2*classAnnoRuns, setUp);
		assertEquals(classAnnoRuns, bench1);
		assertEquals(classAnnoRuns, bench2);
		assertEquals(2*classAnnoRuns,tearDown);
		assertEquals(2, afterMethod);
		assertEquals(1, afterClass);
	}
	
	@Test
	public void testMethodAnnos() {
		final TestBenchMethods benchClass = new TestBenchMethods();
		final Benchmark bench = new Benchmark();
		bench.add(benchClass);
		bench.run();
		
		assertEquals(1, beforeClass);
		assertEquals(2, beforeMethod);
		assertEquals(method1Runs + method2Runs, setUp);
		assertEquals(method1Runs, bench1);
		assertEquals(method2Runs, bench2);
		assertEquals(method1Runs + method2Runs,tearDown);
		assertEquals(2, afterMethod);
		assertEquals(1, afterClass);
	}
	
	class TestBenchMethods {
		@BeforeBenchClass
		public void build(){
			beforeClass++;
		}
		
		@BeforeFirstBenchRun
		public void beforeMethod() {
			beforeMethod++;
		}
		
		@BeforeEachBenchRun
		public void setUp() {
			setUp++;
		}
		
		@Bench(runs=method1Runs)
		public void bench1(){
			bench1++;
		}
		
		@Bench(runs=method2Runs)
		public void bench2(){
			bench2++;
		}
		
		@AfterEachBenchRun
		public void tearDown() {
			tearDown++;
		}
		
		@AfterLastBenchRun
		public void afterMethod() {
			afterMethod++;
		}
		
		@AfterBenchClass
		public void clean() {
			afterClass++;
		}
		
		
	}
	
	@BenchClass(runs=classAnnoRuns)
	class TestBenchClass {
		
		@BeforeBenchClass
		public void build(){
			beforeClass++;
		}
		
		@BeforeFirstBenchRun
		public void beforeMethod() {
			beforeMethod++;
		}
		
		@BeforeEachBenchRun
		public void setUp() {
			setUp++;
		}
		
		public void bench1(){
			bench1++;
		}
		
		public void bench2(){
			bench2++;
		}
		
		@AfterEachBenchRun
		public void tearDown() {
			tearDown++;
		}
		
		@AfterLastBenchRun
		public void afterMethod() {
			afterMethod++;
		}
		
		@AfterBenchClass
		public void clean() {
			afterClass++;
		}
	}
	
}
