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
import edu.ucsd.cse110.habitizer.app.presentation.taskview.debug.TaskViewDebugFragment;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.edit.AddTaskDialogFragment;
import edu.ucsd.cse110.habitizer.app.presentation.ui.TaskViewAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperTimeManager;
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

        // TODO
        this.adapter = new TaskViewAdapter(model, requireContext(), tasksSubject.getValue(),
                integer -> model.getActiveRoutine().checkOffById(integer), isEditMode, this);

        initializeUiTimer();
    }

    private void initializeUiTimer() {
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

        view.debugMenu.setOnClickListener(v -> {
            if (view.debugMenuCard.getVisibility() == View.VISIBLE)
                view.debugMenuCard.setVisibility(View.GONE);
            else
                view.debugMenuCard.setVisibility(View.VISIBLE);
        });

        view.pauseResumeButton.setOnClickListener(v -> {
            model.getActiveRoutine().pausePlay();
            if(model.getActiveRoutine().isPaused()) {
                view.pauseResumeButton.setText(R.string.routine_paused);
                view.endRoutineButton.setEnabled(false);
                view.backToMenuButton.setEnabled(false);
            }
            else {
                view.pauseResumeButton.setText(R.string.routine_unpaused);
                view.endRoutineButton.setEnabled(true);
                view.backToMenuButton.setEnabled(true);
            }
        });


        view.endRoutineButton.setOnClickListener(v -> {
            model.getActiveRoutine().end();
            view.pauseResumeButton.setEnabled(false);
            view.endRoutineButton.setText(R.string.routine_complete);

        });

        model.getActiveRoutine().getIsEndedSubject().observe(ended -> {
            view.pauseResumeButton.setEnabled(false);
            view.endRoutineButton.setText(R.string.routine_complete);
        });

        view.addTaskButton.setOnClickListener(v -> {
            var frag = AddTaskDialogFragment.newInstance();
            frag.show(getParentFragmentManager(), "AddTaskDialogFragment");
        });

        view.backToMenuButton.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, RoutineViewFragment.newInstance())
                    .commit();
        });
    }
}
