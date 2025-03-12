package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class InMemoryDataSource {
    public static final DataRoutine DATA_MORNING_ROUTINE =
        new DataRoutineBuilder()
            .setName("Morning")
            .setId(0)
            .addTask("Shower")
            .addTask("Brush teeth")
            .addTask("Dress")
            .addTask("Make coffee")
            .addTask("Make lunch")
            .addTask("Dinner prep")
            .addTask("Pack bag")
            .setTotalTime(HabitizerTime.fromMinutes(45))
            .build();
    public static final DataRoutine DATA_EVENING_ROUTINE =
        new DataRoutineBuilder()
            .setName("Evening")
            .setId(1)
            .addTask("Charge devices")
            .addTask("Prepare dinner")
            .addTask("Eat dinner")
            .addTask("Wash dishes")
            .addTask("Pack bag")
            .addTask("Homework")
            .setTotalTime(HabitizerTime.fromMinutes(45))
            .build();
    /**
     * This is used as a <a href="https://refactoring.guru/introduce-null-object">Null Object</a>
     * to prevent null references.
     */
    public static final DataRoutine NULL_ROUTINE =
            new DataRoutineBuilder()
                .setName("Lorem Ipsum")
                .setTotalTime(HabitizerTime.fromMinutes(123))
                .build();
    private IDataRoutineManager dataRoutineManager;
    public InMemoryDataSource(IDataRoutineManager dataRoutineManager) {
        this.dataRoutineManager = dataRoutineManager;
    }

    public void initializeFirstRun() {
        dataRoutineManager.addDataRoutine(DATA_MORNING_ROUTINE);
        dataRoutineManager.addDataRoutine(DATA_EVENING_ROUTINE);
    }

    public IDataRoutineManager getDataRoutineManager() {
        return dataRoutineManager;
    }



}
