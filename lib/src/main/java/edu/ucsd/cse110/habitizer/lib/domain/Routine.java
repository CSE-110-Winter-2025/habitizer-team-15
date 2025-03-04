package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.conversions.DataDomainConverter;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.NotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;
import edu.ucsd.cse110.observables.MutableSubject;
import edu.ucsd.cse110.observables.PlainMutableSubject;

public class Routine {

    private final @NonNull MutableNotifiableSubject<List<Task>> tasks;
    private final @NonNull MutableSubject<String> name;

    @NonNull
    public DataRoutine getData() {
        return data;
    }

    private @NonNull DataRoutine data;
    private final @NonNull TimeTracker timeTracker;
    private @NonNull HabitizerTime time;

    /**
     * Updates when the Routine changes.
     */
    private final @NonNull NotifiableSubject<Object> onFlush;


    public HabitizerTime getElapsedTime() {
        return timeTracker.getElapsedTime();
    }
    public HabitizerTime getTotalTime() {
        return new HabitizerTime(data.totalTime());
    }

    public Routine(@NonNull DataRoutine data, @NonNull TimeTracker timeTracker){
        this.name = new PlainMutableSubject<>();
        this.name.setValue(data.name());

        this.data = data;

        PlainMutableNotifiableSubject<Object> onFlushObject = new PlainMutableNotifiableSubject<>();
        onFlushObject.setValue(new Object());
        this.onFlush = onFlushObject;

        this.tasks = new PlainMutableNotifiableSubject<>();
        this.tasks.observe(taskList -> {
            flushToDataRoutine();
        });

        List<Task> tasks = DataDomainConverter.dataTasksToTasks(data.dataTasks());
        this.tasks.setValue(tasks);
        tasks.forEach(this::registerTaskSubjects);


        this.timeTracker = timeTracker;
        this.time = HabitizerTime.zero;
    }

    private void flushToDataRoutine() {
        updateTaskIds();

        List<DataTask> list = DataDomainConverter.tasksToDataTasks(tasks.getValue());

        // TODO: It'd probably be much easier to do this if data was a MutableSubject
        data = new DataRoutine(getName(), list, getId(), getTotalTime().time());
        onFlush.updateObservers();
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
    }

    public void checkOffById(int id){
        Task task = findTaskById(id);
        checkOff(task);
    }

    public int size() {
        return tasks.getValue().size();
    }
    private void registerTaskSubjects(Task task) {
        tasks.updateObservers();
        task.getNameSubject().observe(s -> {
            tasks.updateObservers();
        });
    }

    public void addTask(Task task){
        tasks.getValue().add(task);
        registerTaskSubjects(task);
    }

    public void addTask(int i, Task task){
        tasks.getValue().add(i, task);
        registerTaskSubjects(task);
    }

    public void removeTask(Task task) {
        task.getNameSubject().removeObservers();
        tasks.getValue().remove(task);
        tasks.updateObservers();
    }

    public void removeTaskById(int id)
    {
        removeTask(findTaskById(id));
    }

    public MutableNotifiableSubject<List<Task>> getTasksSubject() {
        return tasks;
    }

    @NonNull
    public NotifiableSubject<Object> getOnFlushSubject() {
        return onFlush;
    }
}
