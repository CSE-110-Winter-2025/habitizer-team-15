package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDataSource {
    private InMemoryDataRoutineManager inMemoryDataRoutineManager = new InMemoryDataRoutineManager();
    public InMemoryDataSource() {}

    public void initializeFirstRun() {
        inMemoryDataRoutineManager.initializeDefaultRoutines();
    }

    public InMemoryDataRoutineManager getDataRoutineManager() {
        return inMemoryDataRoutineManager;
    }



}
