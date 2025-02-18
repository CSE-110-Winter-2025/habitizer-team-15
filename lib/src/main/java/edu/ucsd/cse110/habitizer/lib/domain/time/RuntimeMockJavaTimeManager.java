package edu.ucsd.cse110.habitizer.lib.domain.time;

import androidx.annotation.NonNull;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class RuntimeMockJavaTimeManager extends TimeManager {
    private Boolean isPaused;
    private HabitizerTime pauseTime;
    private HabitizerTime diffTime;
    private final TimeManager usedTimeManager;

    public RuntimeMockJavaTimeManager(@NonNull TimeManager usedTimeManager) {
        this.isPaused = false;
        this.pauseTime = new HabitizerTime(0);
        this.diffTime = new HabitizerTime(0);

        this.usedTimeManager = usedTimeManager;
    }

    @Override
    public HabitizerTime getCurrentTimeNanoseconds() {
        // Either way, we add diffTime since unpaused time may still be different than time
        //      during a previous pause.
        if (isPaused) {
            return pauseTime;
        } else {
            return usedTimeManager.getCurrentTimeNanoseconds().add(diffTime);
        }
    }

    public void switchPause() {
        HabitizerTime currTime = usedTimeManager.getCurrentTimeNanoseconds();

        isPaused ^= true;

        // If it becomes paused: we stop at currTime.
        // Else it becomes unpaused: diffTime now closes the gap between time during pause
        if (isPaused) {
            pauseTime = currTime.add(diffTime);
        } else {
            diffTime = pauseTime.subtract(currTime);
        }
    }

    public void forward(long skipSeconds) {
        if (isPaused) {
            pauseTime = pauseTime.add(new HabitizerTime(skipSeconds * HabitizerTime.secondsToNanoseconds));
        } else {
            diffTime = diffTime.add(new HabitizerTime(skipSeconds * HabitizerTime.secondsToNanoseconds));
        }
    }

}
