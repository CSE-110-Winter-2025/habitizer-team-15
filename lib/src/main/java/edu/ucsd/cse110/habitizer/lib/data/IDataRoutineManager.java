package edu.ucsd.cse110.habitizer.lib.data;

import java.util.List;

public interface IDataRoutineManager {
    void clearDataRoutines();

    void addDataRoutine(DataRoutine dataRoutine);

    void addDataRoutine(int i, DataRoutine dataRoutine);

    void setDataRoutine(int i, DataRoutine dataRoutine);

    List<DataRoutine> getDataRoutines();
}
