package edu.ucsd.cse110.habitizer.app.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.habitizer.app.HabitizerApplication;
import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.app.util.RoutineSnapshotSerializer;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MainViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        this.model = MainViewModel.getSingletonModel(this);

        HabitizerApplication application = (HabitizerApplication) getApplication();
        var sharedPrefs = application.getSharedPreferences();
        InMemoryDataSource inMemoryDataSource = application.getInMemoryDataSource();

        var savedRoutine = sharedPrefs.getString(HabitizerApplication.PREF_SAVED_ROUTINE_SNAPSHOT, null);
        if (savedRoutine != null) {
            Routine.RoutineSnapshot snapshot = RoutineSnapshotSerializer.fromJson(savedRoutine);
            DataRoutine dataRoutine = inMemoryDataSource
                    .getDataRoutineManager()
                    .getDataRoutines()
                    .get(snapshot.routineId);
            Routine routine = new Routine(dataRoutine, new TimeTracker(model.getActiveTimeManager()));

            routine.restoreSnapshot(snapshot);
            model.setActiveRoutine(routine);

            this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance())
                .commit();

            sharedPrefs.edit().putString(HabitizerApplication.PREF_SAVED_ROUTINE_SNAPSHOT, null).apply();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        HabitizerApplication application = (HabitizerApplication) getApplication();

        Routine activeRoutine = model.getActiveRoutine();
        Boolean isEnded = activeRoutine.getIsEndedSubject().getValue();

        if (!activeRoutine.isStarted() || Boolean.TRUE.equals(isEnded))
            return;


        Routine.RoutineSnapshot snapshot = activeRoutine.createSnapshot();
        application.getSharedPreferences()
                .edit()
                .putString(
                        HabitizerApplication.PREF_SAVED_ROUTINE_SNAPSHOT,
                        RoutineSnapshotSerializer.toJson(snapshot))
                .apply();
    }
}
