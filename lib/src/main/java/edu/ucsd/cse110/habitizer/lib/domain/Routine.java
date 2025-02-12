package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

public class Routine {

    private final @NonNull MutableNotifiableSubject<List<Task>> tasks;
    private final @NonNull MutableSubject<String> name;
    private @NonNull DataRoutine data;
    private final @NonNull TimeTracker timeTracker;
    private @NonNull HabitizerTime time;


    protected Routine(@NonNull List<Task> tasks, @NonNull DataRoutine data, @NonNull TimeTracker timeTracker){

        this.tasks = new PlainMutableNotifiableSubject<>();
        this.tasks.observe(taskList -> {
            updateTaskIds();
        });
        // With the way Routine is constructed, `tasks` MUST be NonNull
        // (despite what the warnings say)
        this.tasks.setValue(tasks);

        this.name = new PlainMutableSubject<>();
        this.name.setValue(data.name());

        this.data = data;

        this.timeTracker = timeTracker;
        this.time = new HabitizerTime(0);
    }

    private void updateTaskIds() {
        List<Task> tasks = this.tasks.getValue();
        IntStream.range(0, size())
            .forEach(index -> {
                Task task = tasks.get(index);
                task.setId(index);
            });
    }

    /**
     * Public empty Routine constructor that doesn't require DataRoutine to populate Routine.
     * @param name The name of the Routine.
     * @param timeTracker The TimeTracker to keep track of time with.
     */
    public Routine(String name, TimeTracker timeTracker) {
        this(new ArrayList<>(), DataRoutine.createNull(name), timeTracker);
    }

    public String getName() {
        return name.getValue();
    }

    public int getId() {
        return data.id();
    }

    public void start() {
        timeTracker.start();
    }

    public void end() {
        timeTracker.stop();
        time = timeTracker.getElapsedTime();
    }

    public Task findTaskById(int id) {
        return tasks.getValue().get(id);
    }

    public void checkOffById(int id){
        Task task = findTaskById(id);
        task.recordTime(timeTracker.getCheckoffTimeAndCheckoff());
        task.checkOff();
    }

    public int size() {
        return tasks.getValue().size();
    }

    public void addTask(Task task){
        tasks.getValue().add(task);
        tasks.updateObservers();
    }

    public void removeTaskById(int id) {
        tasks.getValue().remove(id);
    }
}
