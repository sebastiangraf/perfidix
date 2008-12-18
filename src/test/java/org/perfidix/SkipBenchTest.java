package org.perfidix;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.perfidix.annotation.AfterBenchClass;
import org.perfidix.annotation.AfterEachRun;
import org.perfidix.annotation.AfterLastRun;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.BeforeEachRun;
import org.perfidix.annotation.BeforeFirstRun;
import org.perfidix.annotation.Bench;
import org.perfidix.annotation.BenchClass;
import org.perfidix.annotation.SkipBench;
import org.perfidix.depreacted.Benchmark;

public class SkipBenchTest {
    private static final int classAnnoRuns = 20;
    private static final int methodAnnoRuns = 40;

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
    @Ignore
    public void testClassAnnos() {
        final TestBenchClass benchClass = new TestBenchClass();
        final Benchmark bench = new Benchmark();
        bench.add(benchClass);
        bench.run();

        assertEquals(classAnnoRuns, setUp);
        assertEquals(0, bench1);
        assertEquals(classAnnoRuns, bench2);
        assertEquals(classAnnoRuns, tearDown);
    }

    @Test
    @Ignore
    public void testMethodAnnos() {

        final TestSetUpTearDown benchClass = new TestSetUpTearDown();
        final Benchmark bench = new Benchmark();
        bench.add(benchClass);
        bench.run(methodAnnoRuns);

        assertEquals(1, beforeClass);
        assertEquals(1, beforeMethod);
        assertEquals(methodAnnoRuns, setUp);
        assertEquals(0, bench1);
        assertEquals(methodAnnoRuns, bench2);
        assertEquals(methodAnnoRuns, tearDown);
        assertEquals(1, afterMethod);
        assertEquals(1, afterClass);
    }

    @BenchClass(runs = classAnnoRuns)
    class TestBenchClass {

        @SkipBench
        public void bench1() {
            bench1++;
        }

        @SkipBench
        public void setUp() {
            setUp++;
        }

        @Bench(beforeEachRun = "setUp", afterEachRun = "tearDown")
        public void bench2() {
            bench2++;
        }

        @SkipBench
        public void tearDown() {
            tearDown++;
        }
    }

    class TestSetUpTearDown {

        @BeforeBenchClass
        @SkipBench
        public void build() {
            beforeClass++;
        }

        @BeforeFirstRun
        @SkipBench
        public void beforeMethod() {
            beforeMethod++;
        }

        @BeforeEachRun
        @SkipBench
        public void setUp() {
            setUp++;
        }

        @Bench
        @SkipBench
        public void bench1() {
            bench1++;
        }

        @Bench
        public void bench2() {
            bench2++;
        }

        @AfterEachRun
        @SkipBench
        public void tearDown() {
            tearDown++;
        }

        @AfterLastRun
        @SkipBench
        public void afterMethod() {
            afterMethod++;
        }

        @AfterBenchClass
        @SkipBench
        public void clean() {
            afterClass++;
        }
    }

}
