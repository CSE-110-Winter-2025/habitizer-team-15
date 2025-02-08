package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.TaskTime;

public class Routine {

    private final @NonNull List<Task> tasks;
    private final @NonNull DataRoutine data;
    private final @NonNull TimeTracker timeTracker;
    private @NonNull TaskTime time;


    public Routine(@NonNull List<Task> tasks, @NonNull DataRoutine data, @NonNull TimeTracker timeTracker){
        this.tasks = tasks;
        this.data = data;
        this.timeTracker = timeTracker;
        this.time = new TaskTime(0);
    }


    public void start(){
        timeTracker.start();
    }

    public void end(){
        timeTracker.stop();
        time = timeTracker.getElapsedTime();
    }

    public void checkOffById(Task task){
        task.recordTime(timeTracker.getCheckoffTime());
        task.setCheckedOff();
    }

    public int size(){
        return tasks.size();
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}
