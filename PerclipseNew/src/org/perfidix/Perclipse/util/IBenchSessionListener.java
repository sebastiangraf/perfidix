package org.perfidix.Perclipse.util;

/**
 * A listener interface for observing the execution of a bench session.
 */
public interface IBenchSessionListener {
	/**
	 * a bench run has started.
	 */
	public void sessionStarted();

	/**
	 * A bench run has ended.
	 * 
	 * @param elapsedTime the total elapsed time of the bench run.
	 */
	public void sessionEnded(long elapsedTime);

	/**
	 * A bench run has been stopped prematurely.
	 * 
	 * @param elapsedTime
	 *            the time elapsed before the test run was stopped
	 */
	public void sessionStopped(long elapsedTime);

	/**
	 * The VM instance performing the benchs has terminated.
	 */
	public void sessionTerminated();

	/**
	 * A bench has been added to the plan.
	 * 
	 * @param testElement
	 *            the test
	 */
//	public void testAdded(TestElement testElement);
//
//	/**
//	 * All bench have been added and running begins
//	 */
//	public void runningBegins();
//
//	/**
//	 * An individual bench has started.
//	 * 
//	 * @param testCaseElement
//	 *            the test
//	 */
//	public void testStarted(TestCaseElement testCaseElement);
//
//	/**
//	 * An individual bench has ended.
//	 * 
//	 * @param testCaseElement
//	 *            the test
//	 */
//	public void testEnded(TestCaseElement testCaseElement);
//
//	/**
//	 * An individual bench has failed with a stack trace.
//	 * 
//	 * @param testElement
//	 *            the test
//	 * @param status
//	 *            the outcome of the test; one of
//	 *            {@link TestElement.Status#ERROR} or
//	 *            {@link TestElement.Status#FAILURE}
//	 * @param trace
//	 *            the stack trace
//	 * @param expected
//	 *            expected value
//	 * @param actual
//	 *            actual value
//	 */
//	public void testFailed(TestElement testElement, Status status,
//			String trace, String expected, String actual);
//
//	/**
//	 * An individual bench has been rerun.
//	 * 
//	 * @param testCaseElement
//	 *            the test
//	 * @param status
//	 *            the outcome of the test that was rerun; one of
//	 *            {@link TestElement.Status#OK},
//	 *            {@link TestElement.Status#ERROR}, or
//	 *            {@link TestElement.Status#FAILURE}
//	 * @param trace
//	 *            the stack trace in the case of abnormal termination, or the
//	 *            empty string if none
//	 * @param expectedResult
//	 *            expected value
//	 * @param actualResult
//	 *            actual value
//	 */
//	public void testReran(TestCaseElement testCaseElement, Status status,
//			String trace, String expectedResult, String actualResult);

	/**
	 * @return <code>true</code> if the bench run session can be swapped to disk
	 *         although this listener is still installed
	 */
	public boolean acceptsSwapToDisk();

}
