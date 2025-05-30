package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
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

    private final MutableSubject<Boolean> ended = new PlainMutableSubject<>();

    private final @NonNull MutableNotifiableSubject<Long> totalTime;

    /**
     * Updates when the Routine changes.
     */
    private final @NonNull NotifiableSubject<Object> onFlush;

    /**
     * This uses the Memento design pattern.
     * (<a href="https://refactoring.guru/design-patterns/memento">...</a>).
     */
    public static class RoutineSnapshot {
        public List<HabitizerTime> recordedTaskTimes;
        public long timeTrackerTime;
        public long timeTrackerLastCheckoff;
        public int routineId;

        public RoutineSnapshot() {
            recordedTaskTimes = new ArrayList<>();
        }
    }

    public RoutineSnapshot createSnapshot() {
        RoutineSnapshot routineSnapshot = new RoutineSnapshot();
        routineSnapshot.routineId = getId();
        ArrayList<HabitizerTime> recordedTaskTimes = new ArrayList<>();
        routineSnapshot.recordedTaskTimes = recordedTaskTimes;
        List<Task> taskList = tasks.getValue();
        for (Task t : taskList) {
            HabitizerTime recordedTime = t.getRecordedTime();
            if (Boolean.FALSE.equals(t.isDone().getValue()))
                recordedTime = null;
            recordedTaskTimes.add(recordedTime);
        }
        routineSnapshot.timeTrackerTime = timeTracker.getElapsedTime().time();
        routineSnapshot.timeTrackerLastCheckoff = timeTracker.getCheckoffTime().time();
        return routineSnapshot;
    }

    public void restoreSnapshot(RoutineSnapshot snapshot) {
        List<Task> taskList = tasks.getValue();
        for (int i = 0; i < taskList.size(); i++) {
            HabitizerTime time = snapshot.recordedTaskTimes.get(i);
            if (time == null)
                continue;
            taskList.get(i).recordTime(time);
        }
        HabitizerTime totalTime = new HabitizerTime(snapshot.timeTrackerTime);
        timeTracker.addStartTime(totalTime);
        HabitizerTime lastCheckoff = new HabitizerTime(snapshot.timeTrackerLastCheckoff);
        timeTracker.setTrackerLastCheckoffInit(totalTime.subtract(lastCheckoff));
    }

    public HabitizerTime getElapsedTime() {
        return timeTracker.getElapsedTime();
    }
    public HabitizerTime getTotalTime() {
        return new HabitizerTime(this.totalTime.getValue());
    }

    public Routine(@NonNull DataRoutine data, @NonNull TimeTracker timeTracker){
        this.name = new PlainMutableSubject<>();
        this.name.setValue(data.name());

        this.data = data;
        this.totalTime = new PlainMutableNotifiableSubject<>();
        this.totalTime.setValue(data.totalTime());
        this.totalTime.observe(totalUpdatedTime -> {
            flushToDataRoutine();
        });

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
        //  but that's too much refactoring and would interfere with other branches
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

    public void pausePlay() {
        if (!isStarted())
            return;
        timeTracker.switchPause();
    }

    public void end() {
        if (!isStarted())
            return;
        timeTracker.stop();
        time = timeTracker.getElapsedTime();
        ended.setValue(Boolean.TRUE);
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
        Boolean value = timeTracker.getIsStartedSubject().getValue();
        return Boolean.TRUE.equals(value);
    }

    public boolean isPaused() {
        return timeTracker.isPaused();
    }

    public void checkOff(Task task) {
        if (!isStarted() || isPaused() ||  task.isDone().getValue())
            return;
        task.recordTime(timeTracker.getCheckoffTime());
        timeTracker.checkoff();
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
    private void registerTaskSubjects(Task task) {
        tasks.updateObservers();
        task.getNameSubject().observe(s -> {
            tasks.updateObservers();
        });
        task.isDone().observe(s -> {
            if(allCheckedOff()) {
                end();
            }
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

    public void setTotalTime(long time){
        this.totalTime.setValue(time);
    }

    public void removeTask(@NonNull Task task) {
        task.getNameSubject().removeObservers();
        tasks.getValue().remove(task);
        tasks.updateObservers();
    }

    public void moveTaskUp(@NonNull Task task) {
        var size = tasks.getValue().size();
        var index = tasks.getValue().indexOf(task);
        if (index > 0 && index < size) {
            Collections.swap(tasks.getValue(), index, index - 1);
        }
        tasks.updateObservers();
    }

    public void moveTaskDown(@NonNull Task task) {
        var size = tasks.getValue().size();
        var index = tasks.getValue().indexOf(task);
        if (index >= 0 && index < size() - 1) {
            Collections.swap(tasks.getValue(), index, index + 1);
        }
        tasks.updateObservers();
    }

    public HabitizerTime getCheckoffTime() {
        return timeTracker.getCheckoffTime();
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