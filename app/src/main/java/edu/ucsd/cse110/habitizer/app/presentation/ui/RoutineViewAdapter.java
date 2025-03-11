package edu.ucsd.cse110.habitizer.app.presentation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.routineview.RoutineViewFragment;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;

public class RoutineViewAdapter extends ArrayAdapter<DataRoutine> {

    FragmentManager parentFragmentManager;
    @NonNull
    private final MainViewModel model;

    public RoutineViewAdapter(Context context, FragmentManager parentFragmentManager, MainViewModel model, List<DataRoutine> dataRoutines) {
        super(context, 0, dataRoutines);
        this.parentFragmentManager = parentFragmentManager;
        this.model = model;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var dataRoutine = getItem(position);
        assert dataRoutine != null;

        ListItemRoutineBinding binding;
        if (convertView != null) {
            binding = ListItemRoutineBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemRoutineBinding.inflate(layoutInflater, parent, false);
        }

        // TODO: I recommend using String formats here! (better for localization)
        String buttonName = "Start " + dataRoutine.name();
        binding.startRoutine.setText(buttonName);

        binding.startRoutine.setOnClickListener(v -> {
            model.setActiveRoutine(dataRoutine);
            this.parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance())
                    .commit();
        });

        binding.editRoutine.setOnClickListener(v -> {
            model.setActiveRoutine(dataRoutine);
            this.parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true))
                    .commit();
        });

        return binding.getRoot();
    }
}
