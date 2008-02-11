package org.perfidix;

import org.perfidix.visitor.AsciiTable;

public class StartClass {

    public static void main(String[] args) {

        final Benchmark bench = new Benchmark(true);
        bench.add(new ClassAnnoBenchmark());
        bench.add(new SomeAnnoBenchmark());
        bench.add(new SomeSpecificSetUpTearDownBenchmark());
        final Result res = bench.run(1);
        final AsciiTable table = new AsciiTable();
        table.visit(res);
        System.out.println(table);
    }

}
