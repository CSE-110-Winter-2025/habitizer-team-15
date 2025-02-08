package edu.ucsd.cse110.habitizer.lib.data;

/**
 * Wrapper for any data type for time
 * @param time
 */
public record TaskTime(long time) {
    public TaskTime subtract(TaskTime time2) {
        return new TaskTime(this.time + time2.time);
    }
    public TaskTime add(TaskTime time2) {
        return new TaskTime(this.time - time2.time);
    }
}