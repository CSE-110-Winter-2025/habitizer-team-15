package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataTask;

public class RoutinePopulator {
    public static List<Task> createTasksFromDataTasks(List<DataTask> dataTasks) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (DataTask dataTask : dataTasks) {
            tasks.add(new Task(dataTask));
        }
        return tasks;
    }
//
//    // creates routines from data routines
//    public static List<Routine> createRoutinesFromDataRoutines(List<DataRoutine> dataRoutines) {
//        ArrayList<Routine> routines = new ArrayList<>();
//        for (DataRoutine dataRoutine : dataRoutines) {
//            routines.add(new Routine(dataRoutine));
//        }
//        return routines;
//    }
}
