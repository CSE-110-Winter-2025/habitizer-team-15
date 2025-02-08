package edu.ucsd.cse110.habitizer.lib.data;

import org.jetbrains.annotations.NotNull;

public record DataTask(
        @NotNull String name,
       int id)
{
}
