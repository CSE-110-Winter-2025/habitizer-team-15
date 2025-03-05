package edu.ucsd.cse110.habitizer.lib.util.conversions;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

/**
 * TODO: There's definitely a better way of doing this!
 *  Adapters wouldn't work since Data classes are records and aren't meant to have interfaces
 *  Should Task and Routine be both responsible for conversion to Data classes and vice versa?
 *  Or should a separate class like this do that?
 */
public class DataDomainConverter {
    /**
     * Creates a List of Tasks given a List of DataTasks.
     * @param dataTasks The List of DataTasks.
     * @return List of tasks.
     */
    public static List<Task> dataTasksToTasks(List<DataTask> dataTasks) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (DataTask dataTask : dataTasks) {
            tasks.add(new Task(dataTask));
        }
        return tasks;
    }

    public static List<DataTask> tasksToDataTasks(List<Task> tasks) {
        ArrayList<DataTask> outTask = new ArrayList<>();
        for (Task t : tasks)
            outTask.add(t.getData());
        return outTask;
    }
}
