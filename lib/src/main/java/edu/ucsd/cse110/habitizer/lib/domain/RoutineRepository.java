package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
public class RoutineRepository {
    private final InMemoryDataSource dataSource;

    public RoutineRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataRoutine getMorningRoutine() {
        return dataSource.getAllRoutines().get(0);
    }

    public DataRoutine getEveningRoutine() {
        return dataSource.getAllRoutines().get(1);
    }

}
