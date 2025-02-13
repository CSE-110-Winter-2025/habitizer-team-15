package edu.ucsd.cse110.habitizer.app.presentation;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.habitizer.app.presentation.ui.TaskViewAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;

/**
 * TODO: Move this to a separate "TaskViewFragment"
 */
public class MainActivity extends AppCompatActivity {

    private MainViewModel model;
    private ActivityMainBinding view;
    private TaskViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.view.getRoot());

        // Initialize the Model
        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.model = modelProvider.get(MainViewModel.class);

        MutableNotifiableSubject<List<Task>> tasksSubject = model.getRoutine()
                .getTasksSubject();
        this.adapter = new TaskViewAdapter(this, tasksSubject.getValue());

        view.toolbar.setTitle(model.getRoutineName());
        view.taskListView.setAdapter(this.adapter);

        setupModelViewHooks();
    }
    private void setupModelViewHooks() {
        model.getRoutine().getNameSubject().observe(newName -> {
            view.toolbar.setTitle(newName);
        });
        model.getRoutine().getTasksSubject().observe(newTasks -> {
            if (newTasks == null) return;
            adapter.clear();
            adapter.addAll(newTasks);
            adapter.notifyDataSetChanged();
        });
    }

}