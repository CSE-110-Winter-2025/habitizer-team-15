package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class InMemoryDataRoutineManager implements IDataRoutineManager {
    public MutableNotifiableSubject<List<DataRoutine>> dataRoutines = new PlainMutableNotifiableSubject<>();
    public InMemoryDataRoutineManager() {
        dataRoutines.setValue(new ArrayList<>());
    }
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
            ), -1, HabitizerTime.fromMinutes(45).time());
    public static final DataRoutine DATA_EVENING_ROUTINE =
        new DataRoutine("Evening",
            List.of(
                DataTask.createEmpty("Charge devices"),
                DataTask.createEmpty("Prepare dinner"),
                DataTask.createEmpty("Eat dinner"),
                DataTask.createEmpty("Wash dishes"),
                DataTask.createEmpty("Pack bag"),
                DataTask.createEmpty("Homework")
            ), -1, HabitizerTime.fromMinutes(45).time());

    /**
     * This is used as a <a href="https://refactoring.guru/introduce-null-object">Null Object</a>
     * to prevent null references.
     */
    public static final DataRoutine NULL_ROUTINE =
            new DataRoutine("Lorem Ipsum", new ArrayList<>(), -1, HabitizerTime.fromMinutes(123).time());
    public void initializeDefaultRoutines() {
        addDataRoutine(DATA_MORNING_ROUTINE);
        addDataRoutine(DATA_EVENING_ROUTINE);
    }

    @Override
    public void clearDataRoutines() {
        List<DataRoutine> list = dataRoutines.getValue();
        list.clear();
        dataRoutines.updateObservers();
    }

    @Override
    public void addDataRoutine(DataRoutine dataRoutine) {
        List<DataRoutine> list = dataRoutines.getValue();
        list.add(dataRoutine);
        dataRoutines.updateObservers();
    }

    @Override
    public void addDataRoutine(int i, DataRoutine dataRoutine) {
        List<DataRoutine> list = dataRoutines.getValue();
        list.add(i, dataRoutine);
        dataRoutines.updateObservers();
    }

    /**
     * Returns a copy of the DataRoutines list.
     * @return
     */
    @Override
    public List<DataRoutine> getDataRoutines() {
        return List.copyOf(dataRoutines.getValue());
    }
}
