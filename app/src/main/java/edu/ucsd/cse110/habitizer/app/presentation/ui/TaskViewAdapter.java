package edu.ucsd.cse110.habitizer.app.presentation.ui;

import android.content.Context;
import android.graphics.pdf.models.ListItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class TaskViewAdapter extends ArrayAdapter<Task> {

    Consumer<Integer> onTaskClick;
    public TaskViewAdapter(Context context, List<Task> tasks, Consumer<Integer> onTaskClick) {
        super(context, 0, tasks);
        this.onTaskClick = onTaskClick;
    }
    
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        assert task != null;

        // Check if a view is being reused...
        ListItemTaskBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemTaskBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemTaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.taskName.setOnClickListener(v -> {
            var id = task.getId();
            assert id == Objects.requireNonNull(id);
            onTaskClick.accept(id);
        });

        binding.taskName.setText(task.getName());

        String timeDisplay = getTimeDisplayString(task);
        binding.taskTime.setText(timeDisplay);

        getCheckmarkVisibility(task, binding);

        return binding.getRoot();
    }

    @NonNull
    private String getTimeDisplayString(Task task) {
        String timeDisplay = "-";
        if (task.isDone().getValue()) {
            HabitizerTime time = task.getRecordedTime();
            String format = getContext().getString(R.string.task_time_string_format);
            timeDisplay = String.format(format, time.toMinutes());
        }
        return timeDisplay;
    }

    public void getCheckmarkVisibility(Task task, ListItemTaskBinding binding) {
        if (task.isDone().getValue()) {
            binding.checkmark.setVisibility(View.VISIBLE);
        } else {
            binding.checkmark.setVisibility(View.INVISIBLE);
        }
    }
}
