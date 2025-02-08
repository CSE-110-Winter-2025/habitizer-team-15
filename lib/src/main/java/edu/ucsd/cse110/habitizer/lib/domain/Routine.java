package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

public class Routine {
    private List<Task> tasks;
    private DataRoutine data;
    private TimeTracker timeTracker;

    public void start(){

    }

    public void end(){

    }

    public void setTimeManager(){

    }

    public void checkOff(Task task){

    }

    public int size(){
        return tasks.size();
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}
