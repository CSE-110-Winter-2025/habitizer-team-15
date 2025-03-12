package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;


public class TaskReorderingBDDTest {

    //Scenario 1: Change task order
    //  Given
    //    User is viewing morning routine
    //    “Shower” task exists first
    //    “Brush Teeth” task exists second
    //  When
    //    The user taps up arrow to move Brush teeth up
    //  Then
    //    The morning routine now displays
    //    “Brush Teeth” task first
    //    “Shower” second
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

        // check start order
        assertEquals(routine.getTasksSubject().getValue().get(0), shower);
        assertEquals(routine.getTasksSubject().getValue().get(1), brush);
        assertEquals(routine.getTasksSubject().getValue().get(2), dress);

        // call moveTaskUp
        routine.moveTaskUp(brush);

        // check order after moving brush up
        assertEquals(routine.getTasksSubject().getValue().get(0), brush);
        assertEquals(routine.getTasksSubject().getValue().get(1), shower);
        assertEquals(routine.getTasksSubject().getValue().get(2), dress);

        // call moveTaskDown
        routine.moveTaskDown(brush);

        // check order after moving brush back down
        assertEquals(routine.getTasksSubject().getValue().get(0), shower);
        assertEquals(routine.getTasksSubject().getValue().get(1), brush);
        assertEquals(routine.getTasksSubject().getValue().get(2), dress);

    }
}
