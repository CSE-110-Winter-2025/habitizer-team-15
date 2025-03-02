package edu.ucsd.cse110.habitizer.app;

import static edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager.NULL_ROUTINE;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;

    private TimeManager activeTimeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        inMemoryDataSource = new InMemoryDataSource();

        // TODO: Initialize with routines only on first run!
        inMemoryDataSource.initializeFirstRun();

        activeTimeManager = new PausableTimeManager(new JavaTimeManager());
    }
    public TimeManager getActiveTimeManager() { return activeTimeManager; }
}
