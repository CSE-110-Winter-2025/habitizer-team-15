package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.DebugJavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class HabitizerApplication extends Application {
    private InMemoryDataSource inMemoryDataSource;

    private Routine activeRoutine;

    @Override
    public void onCreate() {
        super.onCreate();
        activeRoutine = new Routine(InMemoryDataSource.MORNING_ROUTINE,
                new TimeTracker(new DebugJavaTimeManager()));
        // activeRoutine.start();
    }

    public Routine getActiveRoutine() {
        return activeRoutine;
    }
}
