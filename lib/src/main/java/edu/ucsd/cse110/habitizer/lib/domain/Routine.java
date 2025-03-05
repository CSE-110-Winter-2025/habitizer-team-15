package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    private final MutableSubject<Boolean> ended = new PlainMutableSubject<>();


    public HabitizerTime getElapsedTime() {
        return timeTracker.getElapsedTime();
    }
    public HabitizerTime getTotalTime() {
        return new HabitizerTime(data.totalTime());
    }

    public Routine(@NonNull DataRoutine data, @NonNull TimeTracker timeTracker){

        this.tasks = new PlainMutableNotifiableSubject<>();

        this.tasks.observe(taskList -> {
            updateTaskIds();
        });
        this.tasks.setValue(Task.createListFromDataTasks(data.dataTasks()));

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
        this(DataRoutine.createEmpty(name), timeTracker);
    }

    public String getName() {
        return getNameSubject().getValue();
    }

    public MutableSubject<String> getNameSubject() {
        return name;
    }

    public int getId() {
        return data.id();
    }

    public void start() {
        timeTracker.start();
    }

    public void end() {
        if (!isStarted())
            return;
        timeTracker.stop();
        time = timeTracker.getElapsedTime();
    }

    public Task findTaskById(int id) {
        return tasks.getValue().get(id);
    }

    public Task findTaskByName(String name) {
        Optional<Task> first = tasks.getValue().stream()
                .filter(task -> task.getName().equals(name))
                .findFirst();
        if (first.isPresent())
            return first.get();
        return null;
    }

    public boolean isStarted() {
        Boolean value = timeTracker.isStarted().getValue();
        return Boolean.TRUE.equals(value);
    }

    public void checkOff(Task task) {
        if (!isStarted() || task.isDone().getValue())
            return;
        task.recordTime(timeTracker.getCheckoffTimeAndCheckoff());
        task.checkOff();
        ended.setValue(allCheckedOff());
    }

    public MutableSubject<Boolean> getIsEndedSubject() {
        return ended;
    }

    public boolean allCheckedOff(){
        for(Task task: tasks.getValue()) {
            if(!task.isDone().getValue()) {
                return false;
            }
        }
        return true;
    }


    public void checkOffById(int id){
        Task task = findTaskById(id);
        checkOff(task);
    }

    public int size() {
        return tasks.getValue().size();
    }

    // update corresponding DataRoutine
    public void addTask(Task task){
        tasks.getValue().add(task);
        tasks.updateObservers();
    }

    // update corresponding DataRoutine
    public void addTask(int i, Task task){
        tasks.getValue().add(i, task);
        tasks.updateObservers();
    }

    // update corresponding DataRoutine
    public void removeTask(Task task) {
        tasks.getValue().remove(task);
        tasks.updateObservers();
    }

    // update corresponding DataRoutine
    public void removeTaskById(int id)
    {
        removeTask(findTaskById(id));
    }

    public MutableNotifiableSubject<List<Task>> getTasksSubject() {
        return tasks;
    }
}
