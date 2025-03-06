package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;

public class HabitizerApplication extends Application {

    private InMemoryDataSource inMemoryDataSource;

    private ITimeManager activeTimeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        inMemoryDataSource = new InMemoryDataSource(new InMemoryDataRoutineManager());

        // TODO: Initialize with routines only on first run!
        inMemoryDataSource.initializeFirstRun();

        activeTimeManager = new PausableWrapperTimeManager(new JavaTimeManager());
    }
    public ITimeManager getActiveTimeManager() { return activeTimeManager; }
    public InMemoryDataSource getInMemoryDataSource() {
        return inMemoryDataSource;
    }
}