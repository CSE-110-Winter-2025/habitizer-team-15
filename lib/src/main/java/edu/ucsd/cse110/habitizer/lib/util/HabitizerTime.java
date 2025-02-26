package edu.ucsd.cse110.habitizer.lib.util;

import java.util.Objects;

/**
 * Nanosecond time wrapper
 * @param time
 */
public record HabitizerTime(long time) {
    public static final HabitizerTime zero = new HabitizerTime(0);
    public static final int minutesToSeconds = 60;
    public static final int secondsToNanoseconds = 1000000000;

    public static HabitizerTime fromSeconds(double seconds) {
        return new HabitizerTime((long) (seconds * secondsToNanoseconds));
    }

    public static HabitizerTime fromMinutes(double minutes) {
        return fromSeconds(minutes * minutesToSeconds);
    }

    public HabitizerTime subtract(HabitizerTime time2) {
        return new HabitizerTime(this.time - time2.time);
    }
    public HabitizerTime add(HabitizerTime time2) {
        return new HabitizerTime(this.time + time2.time);
    }

    public double toSeconds() {
        return (double)time / secondsToNanoseconds;
    }

    public double toMinutes() {
        return toSeconds() / minutesToSeconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HabitizerTime that = (HabitizerTime) o;
        return time == that.time;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(time);
    }
}
