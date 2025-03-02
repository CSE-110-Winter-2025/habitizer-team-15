package edu.ucsd.cse110.habitizer.lib.data;

public class InMemoryDataSource {
    private InMemoryDataRoutineManager inMemoryDataRoutineManager;
    public InMemoryDataSource(InMemoryDataRoutineManager inMemoryDataRoutineManager) {
        this.inMemoryDataRoutineManager = inMemoryDataRoutineManager;
    }

    public void initializeFirstRun() {
        inMemoryDataRoutineManager.initializeDefaultRoutines();
    }

    public InMemoryDataRoutineManager getDataRoutineManager() {
        return inMemoryDataRoutineManager;
    }



}
