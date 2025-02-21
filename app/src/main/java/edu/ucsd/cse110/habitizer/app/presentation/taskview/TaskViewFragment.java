package edu.ucsd.cse110.habitizer.app.presentation.taskview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.ui.TaskViewAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeManager;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskViewFragment extends Fragment {

    public static final int UI_UPDATE_PERIOD = 100;
    private MainViewModel model;
    private FragmentTaskViewBinding view;
    private TaskViewAdapter adapter;
    private boolean isRunning = false;
    private MutableNotifiableSubject<Timer> uiTimerSubject;

    public TaskViewFragment() {
    }

    public static TaskViewFragment newInstance(String param1, String param2) {
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Initialize the Model
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.model = modelProvider.get(MainViewModel.class);

        MutableNotifiableSubject<List<Task>> tasksSubject = model.getRoutine()
                .getTasksSubject();
        this.adapter = new TaskViewAdapter(requireContext(), tasksSubject.getValue(), model::checkOff);


        this.uiTimerSubject = new PlainMutableNotifiableSubject<>();

        FragmentActivity activity = getActivity();
        Timer t = new Timer();
        assert activity != null;
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> updateTimeDisplayObservers());
            }
        }, 0, UI_UPDATE_PERIOD);

        uiTimerSubject.setValue(t);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTaskViewBinding.inflate(inflater, container, false);

        view.toolbar.setTitle(model.getRoutineName());
        view.taskListView.setAdapter(this.adapter);

        setupModelViewHooks();
        updateTimeDisplayObservers();
        return view.getRoot();
    }

    private void updateTimeDisplayObservers() {
        uiTimerSubject.updateObservers();
    }

    private void setupModelViewHooks() {

        uiTimerSubject.observe(t -> {
            long time = model.getElapsedTime().toMinutes();
            var str = String.format(getString(R.string.routine_total_time_format), time);
            view.routineTotalElapsed.setText(str);
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

        view.startRoutineButton.setOnClickListener(v -> {
//            TODO: What is this?
//            var str = view.routineTotalTime.getText().toString();
//            view.routineTotalTime.setText(str);
//            view.routineTotalTime.setFocusable(false);
//            view.routineTotalTime.setEnabled(false);
//            view.routineTotalTime.setCursorVisible(false);
//            view.routineTotalTime.setKeyListener(null);
            model.getRoutine().start();
            view.startRoutineButton.setEnabled(false);
            this.isRunning = true;
        });

        TimeManager currTimeManager = model.getActiveTimeManager();
        PausableTimeManager pausable;

        if (currTimeManager instanceof PausableTimeManager)
            pausable = (PausableTimeManager) currTimeManager;
        else {
            pausable = null;
        }

        if (pausable != null) {
            view.pausePlayButton.setOnClickListener(v -> {
                pausable.switchPause();
            });

            view.forwardButton.setOnClickListener(v -> {
                pausable.forward(30);
            });
        } else {
            view.pausePlayButton.setOnClickListener(v -> {
                if (this.isRunning) {
                    model.getRoutine().end();
                    view.endRoutineButton.setText(R.string.routine_paused);
                }
            });
        }

        view.endRoutineButton.setOnClickListener(v -> {
            if (this.isRunning) {
                model.getRoutine().end();
                view.endRoutineButton.setText(R.string.routine_complete);
            }
        });
    }
}