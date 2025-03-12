package edu.ucsd.cse110.habitizer.lib.domain.time;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class MockITimeManager implements ITimeManager {

    private double mockTimeMinutes;

    @Override
    public HabitizerTime getCurrentTime() {
        return new HabitizerTime((long) (this.mockTimeMinutes * HabitizerTime.minutesToSeconds * HabitizerTime.secondsToNanoseconds));
    }

    public void setMockTimeMinutes(long mockTimeMinutes) {
        this.mockTimeMinutes = mockTimeMinutes;
    }
    public double getMockTimeMinutes() {
        return mockTimeMinutes;
    }

    public void addMockTimeMinutes(long elapsedMockTimeMinutes) {
        this.mockTimeMinutes += elapsedMockTimeMinutes;
    }

    public void setMockTimeSeconds(double mockTimeSeconds) {
        this.mockTimeMinutes = mockTimeMinutes = mockTimeSeconds / HabitizerTime.minutesToSeconds;
    }
}