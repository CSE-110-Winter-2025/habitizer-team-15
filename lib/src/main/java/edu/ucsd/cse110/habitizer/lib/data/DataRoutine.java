package edu.ucsd.cse110.habitizer.lib.data;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record DataRoutine(
        @NotNull String name,
        List<Integer> dataTasks,
        int id)
{
}
