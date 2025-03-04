package edu.ucsd.cse110.habitizer.app.presentation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemRoutineBinding;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.routineview.RoutineViewFragment;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class RoutineViewAdapter extends ArrayAdapter<DataRoutine> {

    Consumer<Integer> onRoutineClick;
    FragmentManager parentFragmentManager;
    public RoutineViewAdapter(Context context, FragmentManager parentFragmentManager, List<DataRoutine> dataRoutines, Consumer<Integer> onRoutineClick) {
        super(context, 0, dataRoutines);
        this.onRoutineClick = onRoutineClick;
        this.parentFragmentManager = parentFragmentManager;
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

        String buttonName = "Start " + dataRoutine.name();
        binding.startRoutine.setText(buttonName);

        binding.startRoutine.setOnClickListener(v -> {
            var id = dataRoutine.id();
            onRoutineClick.accept(id);
            this.parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance())
                    .commit();
        });

        binding.editRoutine.setOnClickListener(v -> {
            var id = dataRoutine.id();
            onRoutineClick.accept(id);
            this.parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true))
                    .commit();
        });

        return binding.getRoot();
    }


//    private void setupTimeDisplay(Task task, ListItemTaskBinding binding) {
//        String timeDisplay = getTimeDisplayString(task);
//        binding.taskTime.setText(timeDisplay);
//    }
//
//    @NonNull
//    private String getTimeDisplayString(Task task) {
//        String timeDisplay = "-";
//        if (Boolean.TRUE.equals(task.isDone().getValue())) {
//            HabitizerTime time = task.getRecordedTime();
//            String format = getContext().getString(R.string.task_time_string_format);
//            timeDisplay = String.format(format, (long) Math.ceil(time.toMinutes()));
//        }
//        return timeDisplay;
//    }
//
//    private void getCheckmarkVisibility(Task task, ListItemTaskBinding binding) {
//        if (Boolean.TRUE.equals(task.isDone().getValue())) {
//            binding.checkmark.setVisibility(View.VISIBLE);
//            binding.taskBox.setOnClickListener(null);
//        } else {
//            binding.checkmark.setVisibility(View.INVISIBLE);
//        }
//    }
}
