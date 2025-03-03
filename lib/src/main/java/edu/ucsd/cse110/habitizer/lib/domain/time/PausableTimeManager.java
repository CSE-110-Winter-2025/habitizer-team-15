package edu.ucsd.cse110.habitizer.lib.domain.time;

import androidx.annotation.NonNull;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Wrapper for TimeManager that allows for pausing
 * TODO: Copy pause functionality to TimeTracker
 */
public class PausableTimeManager extends TimeManager {
    private boolean isPaused;
    private HabitizerTime pauseTime;
    private HabitizerTime diffTime;
    private final TimeManager usedTimeManager;

    public PausableTimeManager(@NonNull TimeManager usedTimeManager) {
        this.isPaused = false;
        this.pauseTime = new HabitizerTime(0);
        this.diffTime = new HabitizerTime(0);

        this.usedTimeManager = usedTimeManager;
    }

    @Override
    public HabitizerTime getCurrentTime() {
        // Either way, we add diffTime since unpaused time may still be different than time
        //      during a previous pause.
        if (isPaused) {
            return pauseTime;
        } else {
            return usedTimeManager.getCurrentTime().add(diffTime);
        }
    }

    public boolean switchPause() {
        HabitizerTime currTime = usedTimeManager.getCurrentTime();

        isPaused ^= true;

        // If it becomes paused: we stop at currTime.
        // Else it becomes unpaused: diffTime now closes the gap between time during pause
        if (isPaused)
            pauseTime = currTime.add(diffTime);
        else
            diffTime = pauseTime.subtract(currTime);
        return isPaused;
    }

    public void forward(long skipSeconds) {
        if (isPaused)
            pauseTime = pauseTime.add(new HabitizerTime(skipSeconds * HabitizerTime.secondsToNanoseconds));
        else
            diffTime = diffTime.add(new HabitizerTime(skipSeconds * HabitizerTime.secondsToNanoseconds));
    }

}
