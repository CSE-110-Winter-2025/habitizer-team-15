package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;
import edu.ucsd.cse110.observables.Subject;

public class Task {
    private @Nullable HabitizerTime recordedTime;
    private final @NonNull MutableSubject<String> name;

    @NonNull
    public DataTask getData() {
        return data;
    }

    private @NonNull DataTask data;
    private @NonNull PlainMutableSubject<Boolean> isDone;

    public Task(@NonNull DataTask data) {
        this.name = new PlainMutableSubject<>();
        this.name.setValue(data.name());

        this.name.observe(s -> {
            this.data = DataTask.createEmpty(s).newWithId(getId());
        });

        this.data = data;

        this.isDone = new PlainMutableSubject<>();
        this.isDone.setValue(false);
    }

    /**
     * Public constructor that doesn't require DataTask to populate Task.
     * @param name The name of the task.
     */
    public Task(String name) {
        this(DataTask.createEmpty(name));
    }

    public int getId() {
        return data.id();
    }

    public void setId(int id) {
        data = data.newWithId(id);
    }

    public void checkOff() {
        this.isDone.setValue(true);
    }
    public Subject<Boolean> isDone() {
        return this.isDone;
    }
    public void recordTime(HabitizerTime time) {
        this.recordedTime = time;
        checkOff();
    }
    @androidx.annotation.Nullable
    public HabitizerTime getRecordedTime() {
        return this.recordedTime;
    }
    public void setName(@NonNull String name) {
        this.name.setValue(name);
    }
    public Subject<String> getNameSubject() {
        return this.name;
    }

    public String getName() {
        return this.name.getValue();
    }

    // Is this fine to have? If so, fine-tune and make less messy.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        if (!((getRecordedTime() == null && task.getRecordedTime() == null)
                    || (getRecordedTime() != null && task.getRecordedTime() != null))) {
            return false;
        }

        return ((getRecordedTime() == null && task.getRecordedTime() == null)
                        || getRecordedTime().equals(task.getRecordedTime()))
                    && Objects.equals(getName(), task.getName())
                    && Objects.equals(data.id(), task.data.id())
                    && Objects.equals(isDone.getValue(), task.isDone.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecordedTime(), getName(), data, isDone);
    }
}
