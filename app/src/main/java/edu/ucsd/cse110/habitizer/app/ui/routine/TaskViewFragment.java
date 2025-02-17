package edu.ucsd.cse110.habitizer.app.ui.routine;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentTaskViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskViewFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskViewBinding view;


    public TaskViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskViewFragment newInstance(String param1, String param2) {
        TaskViewFragment fragment = new TaskViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = FragmentTaskViewBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        setupMvp();
        return view.getRoot();
    }

    private void setupMvp() {
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