package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Creational design pattern for creating DataRoutines.
 * (We didn't really need this pattern for the longest time.
 * We just added it to appease the Gang of Four.)
 */
public class DataRoutineBuilder {
    private String name = "New Routine";
    private final List<DataTask> dataTasks;
    private int id = -1;
    private long totalTime = 0;
    public DataRoutineBuilder() {
        dataTasks = new ArrayList<>();
    }

    public DataRoutineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DataRoutineBuilder addTask(String name) {
        dataTasks.add(DataTask.createEmpty(name));
        return this;
    }

    public DataRoutineBuilder addTask(String name, int id) {
        dataTasks.add(DataTask.createEmpty(name).newWithId(id));
        return this;
    }

    public DataRoutineBuilder setId(int id) {
        this.id = id;
        return this;
    }
    public DataRoutineBuilder setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public DataRoutineBuilder setTotalTime(HabitizerTime time) {
        this.totalTime = time.time();
        return this;
    }

    public DataRoutine build() {
        return new DataRoutine(name, dataTasks, id, totalTime);
    }

}
