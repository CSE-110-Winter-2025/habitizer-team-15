package edu.ucsd.cse110.habitizer.lib.data;

/**
 * Wrapper for any data type for time
 * @param time
 */
public record TaskTime(long time) {
    public long subtract(long time2) {
        return time - time2;
    }
    public long add(long time2) {
        return time + time2;
    }
}