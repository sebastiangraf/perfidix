package org.perfidix.Perclipse.util;

public interface IBenchRunSessionListener {
	void sessionAdded(BenchRunSession runSession);
	void sessionRemoved(BenchRunSession runSession);
}
