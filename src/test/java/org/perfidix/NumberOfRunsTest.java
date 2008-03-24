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
 * $Id: NumberOfRunsTest.java 2624 2007-03-28 15:08:52Z kramis $
 * 
 */

package org.perfidix;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class NumberOfRunsTest extends PerfidixTest {

	@Test
	public void testIgnoreMethods() {
		Benchmark b = new Benchmark("my test");
		PerfidixTest.BenchmarkableTestingStub stub = new PerfidixTest.BenchmarkableTestingStub();
		b.add(stub);
		IRandomizer.Randomizer rand = new IRandomizer.Randomizer();
		rand.doIgnoreMethod(findMethod(stub, "benchA"));
		rand.doInvokeMethod(findMethod(stub, "benchB"));
		IResult.BenchmarkResult r = (IResult.BenchmarkResult) b.run(10, rand);

		IResult.ClassResult cls = r.getChildren().get(0);

		IResult.MethodResult benchA = cls.getChildren().get(0);
		IResult.MethodResult benchB = cls.getChildren().get(1);
		IResult.SingleResult benchAmillis = benchA.getChildren().get(0);
		printChildren(benchA);
		printChildren(benchB);
		// assertEquals(1, benchA.getChildren().size());
		assertEquals(0l, benchAmillis.getNumberOfRuns());
		assertEquals("benchA", benchA.getName());
		assertEquals(0l, benchA.getNumberOfRuns());
		assertEquals(10l, benchB.getNumberOfRuns());
		assertEquals(10l, cls.getNumberOfRuns());
		assertEquals(10l, r.getNumberOfRuns());
		System.out.println(r);
	}

	private void printChildren(IResult.MethodResult m) {
		Iterator<IResult.SingleResult> children = m.getChildren().iterator();
		while (children.hasNext()) {
			IResult.SingleResult myChild = children.next();
			System.out.println(" " + myChild.getName() + "  --- "
					+ myChild.getNumberOfRuns()
					+ NiceTable.Util.implode(",", myChild.getResultSet()));
		}
	}

}
