package org.perfidix;

import org.perfidix.visitor.AsciiTable;
import org.perfidix.visitor.RawData;

public class StartClass {

    public static void main(String[] args) {
        final IMeter meter = Perfidix.createMeter("test", "t");
        final Benchmark bench = new Benchmark();
        bench.register(meter);
        bench.add(new ClassAnnoBenchmark());
        bench.add(new SomeAnnoBenchmark());
        bench.add(new SomeSpecificSetUpTearDownBenchmark());
        final Result res = bench.run(1);
        final AsciiTable table = new AsciiTable();
        table.visit(res);
        System.out.println(table);
    }

}
