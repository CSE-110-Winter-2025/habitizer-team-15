package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class InMemoryDataSource {
    public static final DataRoutine DATA_MORNING_ROUTINE =
        new DataRoutine("Morning",
            List.of(
                DataTask.createEmpty("Shower"),
                DataTask.createEmpty("Brush teeth"),
                DataTask.createEmpty("Dress"),
                DataTask.createEmpty("Make coffee"),
                DataTask.createEmpty("Make lunch"),
                DataTask.createEmpty("Dinner prep"),
                DataTask.createEmpty("Pack bag")
            ), 0, HabitizerTime.fromMinutes(45).time());
    public static final DataRoutine DATA_EVENING_ROUTINE =
        new DataRoutine("Evening",
            List.of(
                DataTask.createEmpty("Charge devices"),
                DataTask.createEmpty("Prepare dinner"),
                DataTask.createEmpty("Eat dinner"),
                DataTask.createEmpty("Wash dishes"),
                DataTask.createEmpty("Pack bag"),
                DataTask.createEmpty("Homework")
            ), 1, HabitizerTime.fromMinutes(45).time());
    /**
     * This is used as a <a href="https://refactoring.guru/introduce-null-object">Null Object</a>
     * to prevent null references.
     */
    public static final DataRoutine NULL_ROUTINE =
            new DataRoutine("Lorem Ipsum", new ArrayList<>(), -1, HabitizerTime.fromMinutes(123).time());
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
