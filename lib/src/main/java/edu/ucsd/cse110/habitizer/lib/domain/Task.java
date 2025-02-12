package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

public class Task {
    private @Nullable HabitizerTime recordedTime;
    private final @NonNull MutableSubject<String> name;
    private @NonNull DataTask data;
    private boolean isDone;

    protected Task(@NonNull DataTask data) {
        this.name = new PlainMutableSubject<>();
        this.name.setValue(data.name());

        this.data = data;

        this.isDone = false;
    }

    /**
     * Public constructor that doesn't require DataTask to populate Task.
     * @param name The name of the task.
     */
    public Task(String name) {
        this(DataTask.createNull(name));
    }

    public int getId() {
        return data.id();
    }

    public void setId(int id) {
        data = data.newWithId(id);
    }

    public void checkOff() {
        this.isDone = true;
    }
    public boolean isDone() {
        return this.isDone;
    }
    public void recordTime(HabitizerTime time) {
        this.recordedTime = time;
    }
    @androidx.annotation.Nullable
    public HabitizerTime getRecordedTime() {
        return this.recordedTime;
    }
    public void setName(@NonNull String name) {
        this.name.setValue(name);
    }

    public String getName() {
        return this.name.getValue();
    }

}
