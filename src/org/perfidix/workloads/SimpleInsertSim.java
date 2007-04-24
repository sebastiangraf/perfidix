package org.perfidix.workloads;

import java.util.Random;

import org.perfidix.AfterLastBenchRun;
import org.perfidix.Bench;
import org.perfidix.Benchmark;
import org.perfidix.IMeter;
import org.perfidix.Perfidix;
import org.perfidix.Result;

public class SimpleInsertSim {

  private IMeter getMeter, hasSpaceMeter, updateMeter, allocateMeter;

  private short[] dataStructure;

  private static int RUNS = 1;
  
  private static int NUMBER_OF_ELEMENTS = 4096;

  private static int DATASTRUCTURE_SIZE = 128;

  private static int BUCKET_SIZE = 128;

  private double loadfactor = 0.5;
  
  private static short fill = 0;

  private int allocationIndex = 0;

  public SimpleInsertSim(
      final IMeter getMeter,
      final IMeter hasSpaceMeter,
      final IMeter updateMeter,
      final IMeter allocateMeter) {

    this.getMeter = getMeter;
    this.hasSpaceMeter = hasSpaceMeter;
    this.updateMeter = updateMeter;
    this.allocateMeter = allocateMeter;

    dataStructure = new short[DATASTRUCTURE_SIZE];
    // calculate fill with loadfactor and bucket size
    fill = (short) (loadfactor * BUCKET_SIZE);
    // start size 
    int startSize = (int)(0.5 * DATASTRUCTURE_SIZE);
    //fill dateStructure 
    for (int i = 0; i < startSize; i++) {
      dataStructure[i] = fill;
    }
    // set allocationIndex to the end
    allocationIndex = startSize;
  }

  public void setUp() {

  }

  /**
   * Get costs for a tree height 7 without caching 7 disk touches
   * hasSpace costs 1 disk touch. With caching 0
   * update costs 1 disk touch. With caching 0
   * allocate cost 1 disk touch independent from cache
   * 
   * @param get
   * @param update
   * @param hasSpace
   * @param allocate
   */
  public void calcCosts(int get, int update, int hasSpace, int allocate) {
    
    
  }
  
  public void get() {

    //    get_Counter++;
    getMeter.tick();
  }

  public void hasSpace() {

    //    hasSpace_Counter++;
    hasSpaceMeter.tick();
  }

  public void update() {

    //    update_Counter++;
    updateMeter.tick();
  }

  public void allocate() {

    //    allocate_Counter++;
    allocateMeter.tick();
  }

  @Bench
  public void benchPut() {
    Random r = new Random();
    for(int i = 0; i<NUMBER_OF_ELEMENTS; i++) {
    try {
      if(i % 10 == 0) {
        System.out.print("\n");
      }
      int index = Math.abs(r.nextInt() % allocationIndex);
//      System.out.print(index + ", ");
      //hasSpace
      short prev = dataStructure[index];
      if (prev < BUCKET_SIZE) {
        System.out.print(0 + ", ");
        dataStructure[index] = (short) (prev + 1);
        //costs
        get();
        hasSpace();
        update();
      } else {
        // 
        // check if last bucket still have capacity
        if (dataStructure[allocationIndex - 1] < BUCKET_SIZE) {
          System.out.print(allocationIndex - index - 1 + ", ");
          // insert element
          dataStructure[allocationIndex - 1] = (short) (dataStructure[allocationIndex - 1] + 1);
          //costs
          get();
          hasSpace();
          update();
          get();
          hasSpace();
        } else {
          // allocate new Bucket
          System.out.print(allocationIndex - index + ", ");
          dataStructure[allocationIndex] = 1;
          allocationIndex++;
          //costs
          get();
          hasSpace();
          update();
          get();
          allocate();
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    }
  }

  @AfterLastBenchRun
  public void tearDown() {
    for (int i = 0; i < DATASTRUCTURE_SIZE; i++) {
      dataStructure[i] = 0;
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    try {
      IMeter getMeter = Perfidix.createMeter("getMeter", "- no description -");
      IMeter hasSpaceMeter = Perfidix.createMeter(
          "hasSpaceMeter",
          "- no description -");
      IMeter updateMeter = Perfidix.createMeter(
          "updateMeter",
          "- no description -");
      IMeter allocateMeter = Perfidix.createMeter(
          "allocateMeter",
          "- no description -");

      final Benchmark bench = new Benchmark();
      bench.add(new SimpleInsertSim(
          getMeter,
          hasSpaceMeter,
          updateMeter,
          allocateMeter));

      //    Randomizer rand = new Randomizer();

      final Result res = bench.run(RUNS);
      System.out.println("\n");
      System.out.println("get: " + getMeter.getValue());
      System.out.println("hasSpace: " + hasSpaceMeter.getValue());
      System.out.println("update: " + updateMeter.getValue());
      System.out.println("allocate: " + allocateMeter.getValue());
      System.out.println("fill: " + fill);
      
      /*
      final AsciiTable v = new AsciiTable();
      v.visit(res);
      System.out.println(v.toString());
      */
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
