package edu.ucsd.cse110.habitizer.lib.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for holding information about routines.
 * Negative ID's indicate "null" data that won't be saved or serialized.
 * @param name The name of the routine.
 * @param dataTasks A list of the routine's tasks.
 * @param id The ID of the routine that is unique.
 */
public record DataRoutine(
    @NotNull String name,
    List<DataTask> dataTasks,
    int id,

    long totalTime)
{
    @Deprecated(since = "Use `new DataRoutineBuilder().build()` for default empty routines.")
    public static DataRoutine createEmpty(String name) {
        return new DataRoutine(name, new ArrayList<>(), -1, 0);
    }
    public DataRoutine newWithId(int id) {
        return new DataRoutine(name, dataTasks, id, totalTime);
    }

    @Override
    public String toString() {
        return "DataRoutine{" +
                "name='" + name + '\'' +
                ", dataTasks=" + dataTasks +
                ", id=" + id +
                ", totalTime=" + totalTime +
                '}';
    }
}