package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import edu.ucsd.cse110.habitizer.lib.data.TaskTime;

public class Task {
    private TaskTime recordedTime; // time taken for task
    private int id; // task id
    private @NonNull String taskName; // task name
    private boolean isCheckedOff; // if task is checked off

    public Task(@NonNull String name) { // constructor
        this.taskName = name;
        this.isCheckedOff = false;
    }

    private int getId() {
        return this.id;
    }

    // accessors/modifiers
    public void setCheckedOff() {
        this.isCheckedOff = true;
    }
    public boolean isCheckedOff() {
        return this.isCheckedOff;
    }
    public void recordTime(TaskTime time) {
        this.recordedTime = time;
    }
    public TaskTime getRecordedTime() {
        return this.recordedTime;
    }
    public void setTaskName(@NonNull String name) {
        this.taskName = name;
    }
    @NonNull
    public String getTaskName() {
        return this.taskName;
    }

}
