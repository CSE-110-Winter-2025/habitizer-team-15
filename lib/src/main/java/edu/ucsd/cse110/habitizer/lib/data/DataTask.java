package edu.ucsd.cse110.habitizer.lib.data;

import org.jetbrains.annotations.NotNull;

/**
 * Data class for holding information about tasks.
 * Negative ID's indicate "null" data that won't be saved or serialized.
 * @param name The name of the task.
 * @param id The ID of the task that is unique to the routine.
 */
public record DataTask(
        @NotNull String name,
       int id)
{
    public static DataTask createWithoutId(String name) {
        return new DataTask(name, -1);
    }

    public DataTask newWithId(int id) {
        return new DataTask(name, id);
    }
}