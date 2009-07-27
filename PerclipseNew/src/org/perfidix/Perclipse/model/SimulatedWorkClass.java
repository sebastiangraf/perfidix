package org.perfidix.Perclipse.model;

public class SimulatedWorkClass {

    private final int DURATION = 10;
    private IBenchRunSessionListener benchRunSession;

    public SimulatedWorkClass() {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        simulateLongOperation();

    }

    public void simulateLongOperation() {

        benchRunSession = new BenchRunSessionListener();
        benchRunSession.initTotalBenchProgress(DURATION, null);

        for (int i = 0; i < DURATION; i++) {
            try {
                Thread.sleep(1000);
                benchRunSession.updateCurrentRun(null);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

}
