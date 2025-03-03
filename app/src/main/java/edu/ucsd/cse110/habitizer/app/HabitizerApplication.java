package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;

    private Routine activeRoutine;
    private ITimeManager activeTimeManager;

    @Override
    public void onCreate() {
        super.onCreate();
        activeTimeManager = new PausableWrapperTimeManager(new JavaTimeManager());
        activeRoutine = new Routine(InMemoryDataSource.MORNING_ROUTINE,
                new TimeTracker(activeTimeManager));
    }

    public Routine getActiveRoutine() {
        return activeRoutine;
    }
    public ITimeManager getActiveTimeManager() { return activeTimeManager; }
}
