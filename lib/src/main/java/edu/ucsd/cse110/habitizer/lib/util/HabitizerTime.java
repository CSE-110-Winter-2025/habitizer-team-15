package edu.ucsd.cse110.habitizer.lib.util;

/**
 * Wrapper for any data type for time
 * @param time
 */
public record HabitizerTime(long time) {
    public static final HabitizerTime zero = new HabitizerTime(0);
    public static final int minutesToSeconds = 60;
    public static final int secondsToNanoseconds = 1000000000;

    public HabitizerTime subtract(HabitizerTime time2) {
        return new HabitizerTime(this.time - time2.time);
    }
    public HabitizerTime add(HabitizerTime time2) {
        return new HabitizerTime(this.time + time2.time);
    }

    public long toSeconds() {
        return time / secondsToNanoseconds;
    }

    public long toMinutes() {
        return toSeconds() / minutesToSeconds;
    }

    public long ceilToMinutes() {
        return (long) Math.ceil(toSeconds() / (double) minutesToSeconds);
    }

}
