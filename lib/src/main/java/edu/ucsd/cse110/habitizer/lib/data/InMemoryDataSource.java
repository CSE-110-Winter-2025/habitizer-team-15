package edu.ucsd.cse110.habitizer.lib.data;

import java.util.List;

public class InMemoryDataSource {
    public static final DataRoutine MORNING_ROUTINE =
        new DataRoutine("Morning",
            List.of(
                DataTask.createWithoutId("Shower"),
                DataTask.createWithoutId("Brush teeth"),
                DataTask.createWithoutId("Dress"),
                DataTask.createWithoutId("Make coffee"),
                DataTask.createWithoutId("Make lunch"),
                DataTask.createWithoutId("Dinner prep"),
                DataTask.createWithoutId("Pack bag")
            ), -1);
    public static final List<DataTask> EVENING_ROUTINE_TASKS = List.of(
    );
}
