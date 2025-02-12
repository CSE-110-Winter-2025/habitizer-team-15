package edu.ucsd.cse110.habitizer.lib.data;

import java.util.List;

public class InMemoryDataSource {
    public static final List<DataTask> MORNING_ROUTINE_TASKS = List.of(
        DataTask.createNull("Shower"),
        DataTask.createNull("Brush teeth"),
        DataTask.createNull("Dress"),
        DataTask.createNull("Make coffee"),
        DataTask.createNull("Make lunch"),
        DataTask.createNull("Dinner prep"),
        DataTask.createNull("Pack bag")
    );
    public static final List<DataTask> EVENING_ROUTINE_TASKS = List.of(
    );
}
