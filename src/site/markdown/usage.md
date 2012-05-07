<!--
~~
~~ Copyright (c) 2011, University of Konstanz, Distributed Systems Group
~~ All rights reserved.
~~
~~ Redistribution and use in source and binary forms, with or without
~~ modification, are permitted provided that the following conditions are met:
~~     * Redistributions of source code must retain the above copyright
~~       notice, this list of conditions and the following disclaimer.
~~     * Redistributions in binary form must reproduce the above copyright
~~       notice, this list of conditions and the following disclaimer in the
~~       documentation and/or other materials provided with the distribution.
~~     * Neither the name of the University of Konstanz nor the
~~       names of its contributors may be used to endorse or promote products
~~       derived from this software without specific prior written permission.
~~
~~ THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
~~ ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
~~ WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
~~ DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
~~ DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
~~ (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
~~ LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
~~ ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
~~ (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
~~ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
~~
-->

<a href="https://github.com/disy/perfidix"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://s3.amazonaws.com/github/ribbons/forkme_right_red_aa0000.png" alt="Fork me on GitHub"/></a>

Usage
==========

Description
--------

Using Perfidix is quite straight-forward and even easier if you are familiar with Unit-Testing like JUnit.
The aim of Perfidix is to reduce any generation of benchmarking code but to utilize existing testing-code:
What is more convenient than to use Unit-tests?

Annotations
----------

Perfidix relies on the following two components:

* The execution framework (either our Eclipse Plugin Perclipse) or a container class

* Annotations for methods and classes to be benchmarked

While the first component relies on classes, the second component consists out of Annotations 
applicable on all methods which are parameter-free and void. Similar to JUnit, setup and teardown methods
can be applied to the benchmarks even though Perfidix offers finer granularity of those utility methods.

The following annotations are applied on methoding including suitable parameters

### @BenchClass

* Has to be placed before the class declaration
* Each void-method without parameters and any bench annotation is benched

#### @BenchClass(runs=)

* Sets the number of runs for all benchs
* Can be overridden by the Bench-annotation with own run-parameter

### @BeforeFirstBenchRun

* Executed before every bench-method and after the BeforeBenchClass-annotated method
* Executed for all bench-methods but just once for all runs

### @BeforeEachBenchRun

* Executed before every bench-method and after the BeforeFirstBenchRun-annotated method
* Executed for all bench-methods before every run

### @Bench

* Annotates the method to bench

#### @Bench(beforeFirstBenchRun=)

* Specific setUp-method for this bench for settings before the bench is executed once for this bench

#### @Bench(beforeEachBenchRun=)

* Specific setUp-method for this bench for settings before the bench is executed for every run for this bench

#### @Bench(afterEachBenchRun=)

* Specific tearDown-method for this bench after the bench is executed after every run for this bench

#### @Bench(afterLastBenchRun=)

* Specific tearDown-method for this bench after the bench is executed after the last run of this bench

#### @Bench(runs=)

* Sets the number of runs for this method. Overrides the default value and a possible setting from a BenchClass annotation of the corresponding class.

### @AfterEachBenchRun

* Executed after every bench-method
* Executed for all bench-methods after every run

### @AfterLastBenchMethod

* Executed after every bench-method and after the AfterEachBenchRun-annotated method
* Executed for all bench-methods after the last run

### @AfterBenchClass

* Executed after the last bench-method and after the AfterLastBenchRun-annotated method
* Executed once per class

### @SkipBench

* Will be ignored by perfidix except the method is invoked as a specfic setUp-/tearDown method
* Useful in combination with the BenchClass annotation and a specific setUp-/tearDown method of one bench.

Execution
----------

The methods, marked by the defined annotations, need to be executed by a suitable framework aware of the meters to benchmark on the one hand plus the outputs to be generated on the other hand. The execution takes place either by a provided Eclipse-Plugin or by a suitable Benchmarking-Object executable as normal Java-program.
 