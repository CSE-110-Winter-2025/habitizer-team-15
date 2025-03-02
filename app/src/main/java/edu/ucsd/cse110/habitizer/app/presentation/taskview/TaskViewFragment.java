package edu.ucsd.cse110.habitizer.app.presentation.taskview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.routineview.RoutineViewFragment;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.edit.AddTaskDialogFragment;
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

    private boolean isEditMode = false;
    public static final String BUNDLE_EDIT_MODE_KEY = "isEditMode";
    private MutableNotifiableSubject<Timer> uiTimerSubject;

    public TaskViewFragment() {
    }

    public static TaskViewFragment newInstance(boolean isEditMode) {
        var taskViewFragment = newInstance();
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_EDIT_MODE_KEY, isEditMode);
        taskViewFragment.setArguments(args);
        return taskViewFragment;
    }
    public static TaskViewFragment newInstance() {
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        this.isEditMode = getArguments().getBoolean(BUNDLE_EDIT_MODE_KEY);

        this.model = MainViewModel.getSingletonModel(getActivity());

        MutableNotifiableSubject<List<Task>> tasksSubject = model.getActiveRoutine()
                .getTasksSubject();
        this.adapter = new TaskViewAdapter(requireContext(), tasksSubject.getValue(),
                integer -> model.getActiveRoutine().checkOffById(integer));


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTaskViewBinding.inflate(inflater, container, false);

        view.toolbar.setTitle(model.getActiveRoutineName());
        view.taskListView.setAdapter(this.adapter);

        if (isEditMode) {
            view.editMode.setVisibility(View.VISIBLE);
            view.runMode.setVisibility(View.GONE);
        }

        if (!isEditMode) {
            model.getActiveRoutine().start();
        }

        setupModelViewHooks();
        updateTimeDisplayObservers();
        return view.getRoot();
    }

    private void updateTimeDisplayObservers() {
        uiTimerSubject.updateObservers();
    }

    private void setupModelViewHooks() {

        String string = getString(R.string.routine_total_time_format);
        uiTimerSubject.observe(t -> {
            long time = (long) model.getActiveRoutineElapsedTime().toMinutes();
            long total_time = (long) model.getActiveRoutine().getTotalTime().toMinutes();
            var str = String.format(string, time, total_time);
            view.routineTotalElapsed.setText(str);
        });

        model.getActiveRoutine().getNameSubject().observe(newName -> {
            view.toolbar.setTitle(newName);
        });
        model.getActiveRoutine().getTasksSubject().observe(newTasks -> {
            if (newTasks == null) return;
            adapter.notifyDataSetChanged();
        });

//        view.startRoutineButton.setOnClickListener(v -> {
////            TODO: What is this?
////            var str = view.routineTotalTime.getText().toString();
////            view.routineTotalTime.setText(str);
////            view.routineTotalTime.setFocusable(false);
////            view.routineTotalTime.setEnabled(false);
////            view.routineTotalTime.setCursorVisible(false);
////            view.routineTotalTime.setKeyListener(null);
//            // model.getRoutine().start();
//            view.startRoutineButton.setEnabled(false);
//        });

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
        }

        view.endRoutineButton.setOnClickListener(v -> {
            model.getActiveRoutine().end();
            // TODO: This shouldn't rely on the button getting clicked, but
            //  rather the model changing (i.e. this should observe the model)
            view.endRoutineButton.setText(R.string.routine_complete);
        });

        view.addTask.setOnClickListener(v -> {
            var frag = AddTaskDialogFragment.newInstance();
            frag.show(getParentFragmentManager(), "AddTaskDialogFragment");
        });

        view.backToMenu.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, RoutineViewFragment.newInstance())
                    .commit();
        });
    }
}