package edu.ucsd.cse110.habitizer.app.presentation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ListItemTaskBinding;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class TaskViewAdapter extends ArrayAdapter<Task> {
    public TaskViewAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
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

        binding.taskName.setText(task.getName());

        String timeDisplay = getTimeDisplayString(task);
        binding.taskTime.setText(timeDisplay);

        return binding.getRoot();
    }

    @NonNull
    private String getTimeDisplayString(Task task) {
        String timeDisplay = "-";
        if (task.isDone()) {
            HabitizerTime time = task.getRecordedTime();
            String format = getContext().getString(R.string.task_time_string_format);
            timeDisplay = String.format(format, time.toSeconds());
        }
        return timeDisplay;
    }
}
