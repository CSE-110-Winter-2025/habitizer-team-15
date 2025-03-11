package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;

public interface IDataRoutineManager {
    void clearDataRoutines();

    void addDataRoutine(DataRoutine dataRoutine);

    void addDataRoutine(int i, DataRoutine dataRoutine);

    void setDataRoutine(int i, DataRoutine dataRoutine);

    List<DataRoutine> getDataRoutines();

    MutableNotifiableSubject<ArrayList<DataRoutine>> getDataRoutineSubject();
}
