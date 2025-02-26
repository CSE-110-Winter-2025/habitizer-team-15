package edu.ucsd.cse110.habitizer.lib.data;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

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
            ), -1, HabitizerTime.fromMinutes(45).time());
    public static final List<DataTask> EVENING_ROUTINE = List.of(
    );
}
