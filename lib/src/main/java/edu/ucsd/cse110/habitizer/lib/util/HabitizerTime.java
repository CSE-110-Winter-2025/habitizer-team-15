package edu.ucsd.cse110.habitizer.lib.util;

import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;

/**
 * Wrapper for any data type for time
 * @param time
 */
public record HabitizerTime(long time) {
    public static final HabitizerTime zero = new HabitizerTime(0);

    public HabitizerTime subtract(HabitizerTime time2) {
        return new HabitizerTime(this.time - time2.time);
    }
    public HabitizerTime add(HabitizerTime time2) {
        return new HabitizerTime(this.time + time2.time);
    }

    public long toSeconds() {
        return time / ITimeManager.secondsToNanoseconds;
    }
}