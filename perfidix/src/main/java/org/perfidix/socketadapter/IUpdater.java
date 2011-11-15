 
 package org.perfidix.socketadapter;

import java.util.Map;

import org.perfidix.element.BenchmarkMethod;
import org.perfidix.exceptions.SocketViewException;
import org.perfidix.meter.AbstractMeter;

/**
 * This class creates the connection to the eclipse view via
 * {@link SocketViewStub}. It contains the methods which update the view to
 * inform about the bench process progress.
 * 
 * @author Nuray Guerler, University of Konstanz
 *
 */
public interface IUpdater
{

    /**
     * This method initializes the values of the eclipse view and resets the
     * progress bar.
     * 
     * @param mapping
     *            a mapping with all methods to benchmark and the related runs
     * @throws SocketViewException
     */
    public void initProgressView(final Map<BenchmarkMethod, Integer> mapping)
            throws SocketViewException;

    /**
     * This method notifies the eclipse view which element is currently benched.
     * 
     * @param meter
     *            The current meter.
     * @param name
     *            This param represents the java element which is currently
     *            benched and which is fully qualified.
     * @throws SocketViewException
     */
    public void updateCurrentElement(
            final AbstractMeter meter, final String name)
            throws SocketViewException;
        
    /**
     * This method informs the view that an error occurred while benching the
     * current element.
     * 
     * @param name
     *            Element represents the java element which has not been
     *            executed successfully.
     * @param exception
     *            The exception caused by the element.
     * @throws SocketViewException
     */
    public void updateErrorInElement(
            final String name, final Exception exception)
                    throws SocketViewException;

    /**
     * This method notifies the view that all benches have been executed and the
     * bench progress is finished.
     * 
     * @throws SocketViewException
     */
    public void finished() throws SocketViewException;
            
}
