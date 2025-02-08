package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Routine {

    private final @NonNull List<Task> tasks;
    private final @NonNull DataRoutine data;
    private final @NonNull TimeTracker timeTracker;
    private final @NonNull Time time;


    public Routine(@Nullable List<Task> tasks, @Nullable DataRoutine data, @Nullable TimeTracker timeTracker){
        this.tasks = tasks;
        this.data = data;
        this.timeTracker = timeTracker;
    }


    public void start(){
        timeTracker.start();
    }

    public void end(){
        timeTracker.stop();
        time = timeTracker.getElapsedTime();
    }

    public void checkOffById(Task task){
        task.setTime(timeTracker.getCheckOffTime());
        task.setCheckedOff();
    }

    public int size(){
        return tasks.size();
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}
