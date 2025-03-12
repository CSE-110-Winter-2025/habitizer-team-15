package edu.ucsd.cse110.habitizer.app;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.RoomDataRoutineManager;
import edu.ucsd.cse110.habitizer.app.data.db.DataRoutineDatabase;
import edu.ucsd.cse110.habitizer.app.util.RoutineSnapshotSerializer;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.JavaITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;

public class HabitizerApplication extends Application {

    public static final String HABITIZER_DATABASE_NAME = "habitizer-database";
    public static final String PREF_IS_FIRST_RUN = "isFirstRun";
    public static final String PREF_NAME = "habitizer";
    public static final String PREF_SAVED_ROUTINE_SNAPSHOT = "savedRoutineSnapshot";
    private InMemoryDataSource inMemoryDataSource;

    /**
     * The TimeManager for the entire application.
     */
    private ITimeManager activeITimeManager;

    @Override
    public void onCreate() {
        super.onCreate();

        DataRoutineDatabase database = Room.databaseBuilder(
                getApplicationContext(),
                DataRoutineDatabase.class,
                HABITIZER_DATABASE_NAME
        ).allowMainThreadQueries().build();

//        inMemoryDataSource = new InMemoryDataSource(new InMemoryDataRoutineManager());
        inMemoryDataSource = new InMemoryDataSource(
                new RoomDataRoutineManager(database.dataRoutineDao()));

        var sharedPrefs = getSharedPreferences();
        var isFirstRun = sharedPrefs.getBoolean(PREF_IS_FIRST_RUN, true);

        if (isFirstRun) {
            inMemoryDataSource.initializeFirstRun();
            sharedPrefs.edit().putBoolean(PREF_IS_FIRST_RUN, false).apply();
        }

        activeITimeManager = new PausableWrapperITimeManager(new JavaITimeManager());
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    /**
     * Gets the application's global TimeManager.
     * @return The application's global TimeManager.
     */
    public ITimeManager getActiveTimeManager() { return activeITimeManager; }

    public InMemoryDataSource getInMemoryDataSource() {
        return inMemoryDataSource;
    }
}