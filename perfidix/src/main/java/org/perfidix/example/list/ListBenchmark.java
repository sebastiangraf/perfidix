/**
 * 
 */
package org.perfidix.example.list;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import org.perfidix.Benchmark;
import org.perfidix.annotation.BeforeBenchClass;
import org.perfidix.annotation.Bench;
import org.perfidix.example.Config;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * This benchmark benches 
 *  [@link IntArrayList],
 *  [@link java.util.ArrayList] and
 *  [@link java.util.Vector]
 * @author Nuray Guerler, University of Konstanz
 *
 */
public class ListBenchmark extends ElementList
{

    /**
     * Number of runs.
     */
    private static final int RUNS = 100;

    /**
     * Size of array to be tested.
     */
    private static final int ARRAYSIZE = 100;

    /** Data to be written and read. */
    private transient int[] intData;

    /** IntArrayList instance. */
    private transient IntArrayList list;
    
    /** ArrayList instance */
    private transient ArrayList<Integer> arrayList;
    
    /** vector instance */
    private transient Vector<Integer> vector;


    /**
     * Generating the data, just once per runtime.
     */
    @BeforeBenchClass
    public void generateData()
    {
        final Random ran = new Random();
        intData = new int[ARRAYSIZE];
        int counter = 0;
        while (counter < ARRAYSIZE)
        {
            intData[counter] = ran.nextInt();
            counter++;
        }
    }

    /**
     * Bench for adding the data to the {@link IntArrayList}.
     */
    @Bench(runs = RUNS)
    public void intArrayListAdd()
    {
        list = new IntArrayList();
        list.add(intData);
    }

    /**
     * bench for retrieving an element at a specified index  
     */
    @Bench(runs = RUNS, beforeEachRun = "intArrayListAdd")
    public void intArrayListGet()
    {
         for(int i=0;i<list.size();i++)
         {
             list.get(i);
         }
    }

    /** bench for adding data to the [@link ArrayList] */
    @Bench(runs = RUNS)
    public void arrayListAdd()
    {
        arrayList = new ArrayList<Integer>();
        for (final int i : intData)
        {
            arrayList.add(i);
        }
    }
    
    /** benchmark for retrieving an element at a specified index */
    @Bench(runs = RUNS, beforeEachRun = "arrayListAdd")
    public void arrayListGet()
    {
        for(int i=0;i<list.size();i++)
        {
            arrayList.get(i);
        }
    }
    
    /** benchmark for adding data to [@link java.util.Vector] */
    @Bench(runs = RUNS)
    public void vectorAdd()
    {
        vector = new Vector<Integer>();
        for (final int i : intData)
        {
            vector.add(i);
        }
    }

    /** benchmark for retrieving an element at a specified index */
    @Bench(runs = RUNS, beforeEachRun = "vectorAdd")
    public void vectorGet()
    {
        for(int i=0;i<vector.size();i++)
        {
            vector.get(i);
        }
    }

    /**
     * Simple setUp of a benchmark. The {@link Benchmark} is initialized with
     * two Meters (<code>TimeMeter</code> and <code>MemMeter</code>). Afterwards
     * the benchmark is running with a TabularOutput as a listener registered.
     * The result of the benchmark is displayed in a complete table at the end.
     * 
     * @param args
     * not used here
     */
    public static void main(String[] args)
    {
        final Benchmark bench = new Benchmark(new Config());
        bench.add(ListBenchmark.class);

        final BenchmarkResult res = bench.run();
        new TabularSummaryOutput().visitBenchmark(res);
    }

}
