package edu.ucsd.cse110.habitizer.lib.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class for holding information about routines.
 * Negative ID's indicate "null" data that won't be saved or serialized.
 * @param name The name of the routine.
 * @param dataTasks A list of ID's of the routine's tasks.
 * @param id The ID of the routine that is unique.
 */
public record DataRoutine(
    @NotNull String name,
    List<Integer> dataTasks,
    int id)
{
    public static DataRoutine createNull(String name) {
        return new DataRoutine(name, new ArrayList<>(), -1);
    }
}