package edu.ucsd.cse110.habitizer.lib.data;

import java.util.List;

public class InMemoryDataSource {
    public static final DataRoutine MORNING_ROUTINE =
        new DataRoutine("Morning",
            List.of(
                DataTask.createEmpty("Shower"),
                DataTask.createEmpty("Brush teeth"),
                DataTask.createEmpty("Dress"),
                DataTask.createEmpty("Make coffee"),
                DataTask.createEmpty("Make lunch"),
                DataTask.createEmpty("Dinner prep"),
                DataTask.createEmpty("Pack bag")
            ), -1);
    public static final List<DataTask> EVENING_ROUTINE = List.of(
    );

    public static final DataRoutine MORNING_ROUTINE_WITH_IDS =
        new DataRoutine("Morning",
            List.of(
                new DataTask("Shower", 4),
                new DataTask("Brush teeth", 3),
                new DataTask("Dress", 6),
                new DataTask("Make coffee", 1),
                new DataTask("Make lunch", 7),
                new DataTask("Dinner prep", 5),
                new DataTask("Pack bag", 2)
            ), -1);
}
