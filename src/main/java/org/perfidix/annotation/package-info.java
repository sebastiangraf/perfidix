/**
 * Copyright (c) 2012, University of Konstanz, Distributed Systems Group All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. * Neither the name of the University of Konstanz nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * <p>
 * This package contains all annotations needed for benchmarking classes with the help of perfidix. The annotations are
 * as follows (including their usage):
 * </p>
 * <ul>
 * <li>{@link org.perfidix.annotation.AfterBenchClass}: Methods annotated with this annotation are executed after the
 * last call of the last benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.AfterEachRun}: Methods annotated with this annotation are executed after each call
 * of each benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.AfterLastRun}: Methods annotated with this annotation are executed after the last
 * call of each benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.BeforeBenchClass}: Methods annotated with this annotation are executed before the
 * first call of the first benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.BeforeEachRun}: Methods annotated with this annotation are executed before the
 * each call of the each benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.BeforeFirstRun}: Methods annotated with this annotation are executed before the
 * first call of the each benchmarking method within the class.</li>
 * <li>{@link org.perfidix.annotation.Bench}: Methods annotated with this annotation are marked as method to be
 * benchmarked.</li>
 * <li>{@link org.perfidix.annotation.Bench}: Classes annotated with this annotation are marked as classes to be
 * benchmarked including all methods within this class.</li>
 * <li>{@link org.perfidix.annotation.BenchmarkConfig}: Methods annotated with this annotation are marked as
 * configuration for a benchmark.</li>
 * <li>{@link org.perfidix.annotation.SkipBench}: Elements annotated with this annotation are skipped for all benching
 * activities.</li>
 * </ul>
 */
package org.perfidix.annotation;

