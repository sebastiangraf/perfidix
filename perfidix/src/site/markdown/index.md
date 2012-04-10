<!--~
~
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

Introduction
==========

Perfidix is a tool for developers to conveniently and 
consistently benchmark Java code. It can quickly answer which 
implementation is faster. Without the need to launch a full-fledged 
profiling application or rewrite the same Java code to 
measure the execution time again and again.

Even though Perfidix is simple to use, it still provides a sound 
statistical output for taking a decision.

Perfidix was presented 2007 as a *Work in Progess* at the Jazoon. The related paper can be found over [here](http://nbn-resolving.de/urn:nbn:de:bsz:352-opus-84446).

JUnit like handling
----------

Perfidix is intentionally designed to be used like 
[JUnit 4.x](http://www.junit.org/), 
i.e., it provides annotations to discover which methods should be 
benchmarked how many times. The eclipse plugin or custom 
code allows to run a package, class, or method as a Perfidix 
benchmark and to collect the execution time statistics for each 
method in the console as text or CVS output.

The output includes package, class, and method name together 
with the number of runs, min, max, average, standard 
deviation, and confidence intervals. The number of runs 
can individually be specified per package, class, or method. 
Besides this, the number of runs can be chosen by a probability 
distribution function to simulate typical workloads.

Define your own meters
----------

Even though equipped with multiple meters to measure your source code (time, memory, threads),
Perfidix offers the ability to inject own meters just by implement one single Interface.
This enables Perfidix not only to act as a out-of-the-box usable tool, but also to be adapted to
the specific program environment and therefore to any resulting problems. 

Do a quick benchmark, analyze later.
----------

Convenient outputs ranging from common ascii-tables to plain csv-output offer not only a quick
overlook about the benchmark but also in-depth analysis techniques with the help of any third-party
program. Therefore, all benchmark results could easily be made persistent for later analysis and 
compilation into charts. 

Own needs, own requirements
----------

Perfidix was created at the [University of Konstanz](http://www.uni-konstanz.de/) at the [Distributed Systems Group](http://www.disy.uni-konstanz.de) out of the necessity to provide an architecture for evaluating own implementations. 

It greatly assisted the research by allowing for quick benchmarking of different algorithms and data structures. 
Although not directly mentioned, Perfidix represents the benchmarking framework for multiple thesis' within
our working group and is maintained continuously since 2007.

Perfidix is hosted with https://github.com/disy/perfidix under the [BSD License](http://www.opensource.org/licenses/BSD-3-Clause).
