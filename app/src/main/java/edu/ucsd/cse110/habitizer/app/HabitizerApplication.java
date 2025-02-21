package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;

    private Routine activeRoutine;
    private TimeManager activeTimeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        activeTimeManager = new PausableTimeManager(new JavaTimeManager());
        activeRoutine = new Routine(InMemoryDataSource.MORNING_ROUTINE,
                new TimeTracker(activeTimeManager));
    }

    public Routine getActiveRoutine() {
        return activeRoutine;
    }
    public TimeManager getActiveTimeManager() { return activeTimeManager; }
}
