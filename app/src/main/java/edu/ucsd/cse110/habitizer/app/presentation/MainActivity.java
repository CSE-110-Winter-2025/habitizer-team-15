package edu.ucsd.cse110.habitizer.app.presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.presentation.ui.TaskViewAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

/**
 * TODO: Move this to a separate "TaskViewFragment"
 */
public class MainActivity extends AppCompatActivity {

    public static final int UI_UPDATE_PERIOD = 100;
    private MainViewModel model;
    private ActivityMainBinding view;
    private TaskViewAdapter adapter;
    /**
     * This subject actually holds no value; it's purpose is to simply notify on ui thread calls
     */
    private MutableNotifiableSubject<Timer> uiTimerSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.view.getRoot());

        // Initialize the Model
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.model = modelProvider.get(MainViewModel.class);

        MutableNotifiableSubject<List<Task>> tasksSubject = model.getRoutine()
                .getTasksSubject();
        this.adapter = new TaskViewAdapter(this, tasksSubject.getValue());

        view.toolbar.setTitle(model.getRoutineName());
        view.taskListView.setAdapter(this.adapter);

        this.uiTimerSubject = new PlainMutableNotifiableSubject<>();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateTimeDisplayObservers());
            }
        }, 0, UI_UPDATE_PERIOD);

        uiTimerSubject.setValue(t);

        setupModelViewHooks();

        updateTimeDisplayObservers();
    }

    private void updateTimeDisplayObservers() {
        uiTimerSubject.updateObservers();
    }

    private void setupModelViewHooks() {
        uiTimerSubject.observe(t -> {
            long time = model.getElapsedTime().toMinutes();
            var str = String.format(getString(R.string.routine_total_time_format), time, 123);
            view.routineTotalTime.setText(str);
        });

        model.getRoutine().getNameSubject().observe(newName -> {
            view.toolbar.setTitle(newName);
        });
        model.getRoutine().getTasksSubject().observe(newTasks -> {
            if (newTasks == null) return;
            adapter.clear();
            adapter.addAll(newTasks);
            adapter.notifyDataSetChanged();
        });

        view.endRoutineButton.setOnClickListener(v -> {
            model.getRoutine().end();
        });
    }

}