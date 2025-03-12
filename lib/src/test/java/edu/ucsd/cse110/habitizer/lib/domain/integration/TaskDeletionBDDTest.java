package edu.ucsd.cse110.habitizer.lib.domain.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class TaskDeletionBDDTest {

    //Scenario 1: Remove “task
    //  Given
    //      User is viewing morning routine
    //      “Shower” task exists
    //      “Brush Teeth” task exists
    //      "Dress" task exists
    //  When
    //      User taps the remove/trash button on the “Brush Teeth” task
    //  Then
    //      The "Brush Teeth" task gets removed
    //      And now the morning routine displays brush teeth and dress task
    @Test
    public void testScenario1() {
        MockITimeManager mockTime = new MockITimeManager();
        Routine routine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                new TimeTracker(mockTime));

        int showerId = 0;
        int brushTeethId = 1;
        int dressId = 2;

        Task shower = routine.findTaskById(showerId);
        Task brush = routine.findTaskById(brushTeethId);
        Task dress = routine.findTaskById(dressId);

        // check start tasks
        assertEquals(routine.getTasksSubject().getValue().get(0), shower);
        assertEquals(routine.getTasksSubject().getValue().get(1), brush);
        assertEquals(routine.getTasksSubject().getValue().get(2), dress);

        // call delete on brush
        routine.removeTask(brush);

        // check tasks after delete
        assertEquals(routine.getTasksSubject().getValue().get(0), shower);
        assertEquals(routine.getTasksSubject().getValue().get(1), dress);
    }

}
