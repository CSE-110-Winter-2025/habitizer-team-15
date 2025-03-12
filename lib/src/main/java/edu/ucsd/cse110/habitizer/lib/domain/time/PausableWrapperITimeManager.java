package edu.ucsd.cse110.habitizer.lib.domain.time;

import androidx.annotation.NonNull;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Wrapper for TimeManager that allows for pausing
 */
public class PausableWrapperITimeManager implements ITimeManager {
    private boolean isPaused;
    private HabitizerTime pauseTime;
    private HabitizerTime diffTime;
    private final ITimeManager usedITimeManager;

    public PausableWrapperITimeManager(@NonNull ITimeManager usedITimeManager) {
        this.isPaused = false;
        this.pauseTime = new HabitizerTime(0);
        this.diffTime = new HabitizerTime(0);

        this.usedITimeManager = usedITimeManager;
    }

    @Override
    public HabitizerTime getCurrentTime() {
        // Either way, we add diffTime since unpaused time may still be different than time
        //      during a previous pause.
        if (isPaused) {
            return pauseTime;
        } else {
            return usedITimeManager.getCurrentTime().add(diffTime);
        }
    }

    public boolean switchPause() {
        HabitizerTime currTime = usedITimeManager.getCurrentTime();

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
        forward(new HabitizerTime(skipSeconds * HabitizerTime.secondsToNanoseconds));
    }

    public void forward(HabitizerTime time) {
        if (isPaused)
            pauseTime = pauseTime.add(time);
        else
            diffTime = diffTime.add(time);
    }

    public boolean isPaused() {
        return isPaused;
    }
}
