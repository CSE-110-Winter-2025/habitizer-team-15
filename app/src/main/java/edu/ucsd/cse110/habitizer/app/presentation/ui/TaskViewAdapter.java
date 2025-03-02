package edu.ucsd.cse110.habitizer.app.presentation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.edit.RenameTaskDialogFragment;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class TaskViewAdapter extends ArrayAdapter<Task> {

    private final MainViewModel model;
    Consumer<Integer> onTaskClick;
    private final boolean isEditMode;
    private final TaskViewFragment taskViewFragment;

    public TaskViewAdapter(MainViewModel model, Context context, List<Task> tasks,
                           Consumer<Integer> onTaskClick, boolean isEditMode, TaskViewFragment taskViewFragment) {
        super(context, 0, tasks);
        this.model = model;
        this.onTaskClick = onTaskClick;
        this.isEditMode = isEditMode;
        this.taskViewFragment = taskViewFragment;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        assert task != null;

        ListItemTaskBinding binding;
        if (convertView != null) {
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        if (isEditMode) {
            binding.editMode.setVisibility(View.VISIBLE);
            binding.runMode.setVisibility(View.GONE);
        }

        setupMvpHooks(binding, task);

        return binding.getRoot();
    }

    private void setupMvpHooks(ListItemTaskBinding binding, Task task) {
        binding.taskName.setText(task.getName());

        task.getNameSubject().observe(newName -> {
            binding.taskName.setText(newName);
        });

        setupTimeDisplay(task, binding);

        getCheckmarkVisibility(task, binding);

        binding.taskBox.setOnClickListener(v -> {
            var id = task.getId();
            onTaskClick.accept(id);
            getCheckmarkVisibility(task, binding);
            setupTimeDisplay(task, binding);
        });

        // Edit mode buttons
        binding.delete.setOnClickListener(v -> {
            model.getRoutine().removeTask(task);
        });

        binding.rename.setOnClickListener(v -> {
            var frag = RenameTaskDialogFragment.newInstance(task);
            frag.show(taskViewFragment.getParentFragmentManager(), "RenameTaskDialogFragment");
        });
    }

    private void setupTimeDisplay(Task task, ListItemTaskBinding binding) {
        String timeDisplay = getTimeDisplayString(task);
        binding.taskTime.setText(timeDisplay);
    }

    @NonNull
    private String getTimeDisplayString(Task task) {
        String timeDisplay = getContext().getString(R.string.task_empty_time);
        if (Boolean.TRUE.equals(task.isDone().getValue())) {
            HabitizerTime time = task.getRecordedTime();
            String format = getContext().getString(R.string.task_time_string_format);
            timeDisplay = String.format(format, (long) Math.ceil(time.toMinutes()));
        }
        return timeDisplay;
    }

    private void getCheckmarkVisibility(Task task, ListItemTaskBinding binding) {
        if (Boolean.TRUE.equals(task.isDone().getValue())) {
            binding.checkmark.setVisibility(View.VISIBLE);
            binding.taskBox.setOnClickListener(null);
        } else {
            binding.checkmark.setVisibility(View.INVISIBLE);
        }
    }
}
