/**
 * <p>
 * This package contains all annotations needed for benchmarking classes with the help of perfidix. The
 * annotations are as follows (including their usage):
 * </p>
 * <ul>
 * <li>{@link AfterBenchClass}: Methods annotated with this annotation are executed after the last call of the
 * last benchmarking method within the class.</li>
 * <li>{@link AfterEachBench}: Methods annotated with this annotation are executed after each call of each
 * benchmarking method within the class.</li>
 * <li>{@link AfterLastBench}: Methods annotated with this annotation are executed after the last call of each
 * benchmarking method within the class.</li>
 * <li>{@link BeforeBenchClass}: Methods annotated with this annotation are executed before the first call of
 * the first benchmarking method within the class.</li>
 * <li>{@link BeforeEachRun}: Methods annotated with this annotation are executed before the each call of the
 * each benchmarking method within the class.</li>
 * <li>{@link BeforeFirstRun}: Methods annotated with this annotation are executed before the first call of
 * the each benchmarking method within the class.</li>
 * <li>{@link Bench}: Methods annotated with this annotation are marked as method to be benchmarked.</li>
 * <li>{@link Bench}: Classes annotated with this annotation are marked as classes to be benchmarked including
 * all methods within this class.</li>
 * <li>{@link BenchmarkConfig}: Methods annotated with this annotation are marked as configuration for a
 * benchmark.</li>
 * <li>{@link SkipBench}: Elements annotated with this annotation are skipped for all benching activities.</li>
 * </ul>
 */
package org.perfidix.annotation;

