package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.IntStream;


import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class InMemoryDataRoutineManager implements IDataRoutineManager {

    public MutableNotifiableSubject<ArrayList<DataRoutine>> dataRoutines = new PlainMutableNotifiableSubject<>();
    public InMemoryDataRoutineManager() {
        dataRoutines.setValue(new ArrayList<>());
        dataRoutines.observe(d -> {
            updateRoutineIds();
        });
    }

    private void updateRoutineIds() {
        List<DataRoutine> routines = this.dataRoutines.getValue();
        IntStream.range(0, size())
            .forEach(index -> {
                DataRoutine routine = routines.get(index);
                routine.newWithId(index);
            });
    }

    public int size() {
        return dataRoutines.getValue().size();
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

    @Override
    public void setDataRoutine(int i, DataRoutine dataRoutine) {
        ArrayList<DataRoutine> list = dataRoutines.getValue();
        list.set(i, dataRoutine);
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
