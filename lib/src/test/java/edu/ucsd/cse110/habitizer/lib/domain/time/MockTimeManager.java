package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class MockTimeManager extends ITimeManager {

    private double mockTime;

    @Override
    public HabitizerTime getCurrentTimeNanoseconds() {
        return new HabitizerTime((long) (this.mockTime * secondsToNanoseconds));
    }

    public void setMockTime(long mockTime) {
        this.mockTime = mockTime;
    }
    public double getMockTime() {
        return mockTime;
    }

    public void addMockTime(long elapsed) {
        this.mockTime += elapsed;
    }
}