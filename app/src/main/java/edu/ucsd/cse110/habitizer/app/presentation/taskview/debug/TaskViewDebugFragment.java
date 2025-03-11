package edu.ucsd.cse110.habitizer.app.presentation.taskview.debug;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskViewDebugBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class TaskViewDebugFragment extends Fragment {

    private static final long UI_UPDATE_PERIOD = 10;
    private FragmentTaskViewDebugBinding view;
    private MainViewModel model;
    private MutableNotifiableSubject<Timer> uiTimerSubject;

    public TaskViewDebugFragment() {

    }

    private void updateTimeDisplayObservers() {
        uiTimerSubject.updateObservers();
    }

    public static TaskViewDebugFragment newInstance() {
        TaskViewDebugFragment fragment = new TaskViewDebugFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.model = MainViewModel.getSingletonModel(getActivity());

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentTaskViewDebugBinding.inflate(inflater, container, false);
        setupMvpHooks();
        return view.getRoot();
    }

    @SuppressLint("DefaultLocale")
    private void setupMvpHooks() {
        ITimeManager currITimeManager = model.getActiveTimeManager();
        PausableWrapperITimeManager pausable;

        if (currITimeManager instanceof PausableWrapperITimeManager)
            pausable = (PausableWrapperITimeManager) currITimeManager;
        else
            pausable = null;

        uiTimerSubject.observe(t -> {
            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("Routine elapsed time (s): ");
            debugInfo.append(String.format("%.2f", model.getActiveRoutineElapsedTime().toSeconds()));
            view.debugInfo.setText(debugInfo.toString());
        });

        // Note that these are SEPARATE from the pause/play buttons that the user usually has
        // access to
        // This controls the GLOBAL TimeManager, while the user should only control the routine's
        // TimeTracker

        if (pausable != null) {
            view.pausePlayButton2.setOnClickListener(v -> {
                pausable.switchPause();
            });
            view.forwardButton2.setOnClickListener(l -> {
                pausable.forward(15);
            });
        }
    }
}