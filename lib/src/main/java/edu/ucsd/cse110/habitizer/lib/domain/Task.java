package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class Task {
    private @NonNull TaskTime recordedTime; // time taken for task
    private @NonNull int id; // task id
    private @NonNull String taskName; // task name
    private @NonNull boolean isCheckedOff; // if task is checked off

    public Task(String name) { // constructor
        this.name = name;
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
    public void recordTime(Time time) {
        this.time = time;
    }
    public Time getRecordedTime() {
        return this.time;
    }
    public void setTaskName(String name) {
        this.name = name;
    }
    public String getTaskName() {
        return this.name;
    }

}
