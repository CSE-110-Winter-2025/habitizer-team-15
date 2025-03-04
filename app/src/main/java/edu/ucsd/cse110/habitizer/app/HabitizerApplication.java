package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeManager;

public class HabitizerApplication extends Application {

    private InMemoryDataSource inMemoryDataSource;

    private TimeManager activeTimeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        inMemoryDataSource = new InMemoryDataSource(new InMemoryDataRoutineManager());

        // TODO: Initialize with routines only on first run!
        inMemoryDataSource.initializeFirstRun();

        activeTimeManager = new PausableTimeManager(new JavaTimeManager());
    }
    public TimeManager getActiveTimeManager() { return activeTimeManager; }
    public InMemoryDataSource getInMemoryDataSource() {
        return inMemoryDataSource;
    }
}
