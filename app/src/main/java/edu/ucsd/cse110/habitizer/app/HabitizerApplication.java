package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.time.DebugJavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;
    private RoutineRepository routineRepository;

    private Routine activeRoutine;

    @Override
    public void onCreate() { // setup routine display so that onCreate opens first
        super.onCreate();
        this.activeRoutine = null;

        this.inMemoryDataSource = InMemoryDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(inMemoryDataSource);
    }

    public RoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }

    public Routine startMorningRoutine() {
        this.activeRoutine = new Routine(inMemoryDataSource.getAllRoutines().get(0),
                new TimeTracker(new DebugJavaTimeManager()));
        this.activeRoutine.start();
        return this.activeRoutine;
    }

    public Routine getActiveRoutine() {
        return activeRoutine;
    }
}
