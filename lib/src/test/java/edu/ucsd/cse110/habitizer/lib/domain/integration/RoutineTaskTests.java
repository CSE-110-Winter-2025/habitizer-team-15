package edu.ucsd.cse110.habitizer.lib.domain.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

/**
 * Domain integration tests between Routine and Task.
 */
public class RoutineTaskTests {

    /**
     * General test scenario.
     */
    @Test
    public void testScenario0() {
        MockITimeManager mockTime = new MockITimeManager();
        Routine routine = new Routine("Test Routine", new TimeTracker(mockTime));

        Task testTask1 = new Task("Test Task 1");
        routine.addTask(testTask1);

        Task testTask2 = new Task("Test Task 2");
        routine.addTask(testTask2);

        Task testTask3 = new Task("Test Task 3");
        routine.addTask(testTask3);

        // Check that ID's are updated
        assertEquals(0, testTask1.getId());
        assertEquals(1, testTask2.getId());
        assertEquals(2, testTask3.getId());

        mockTime.setMockTimeMinutes(15);
        routine.start();

        mockTime.setMockTimeMinutes(20);

        assertEquals(20, mockTime.getMockTimeMinutes(), 0.001);
        assertEquals(20, mockTime.getCurrentTime().toMinutes(), 0.001);

        routine.checkOffById(testTask2.getId());

        mockTime.setMockTimeMinutes(26);
        routine.end();

        // Time passes after the routine ends
        mockTime.setMockTimeMinutes(30);

        // We try checking off a task after ending the routine,
        // which should NOT work
        routine.checkOffById(testTask3.getId());

        assertFalse(testTask1.isDone().getValue());
        assertTrue(testTask2.isDone().getValue());
        assertFalse(testTask3.isDone().getValue());

        assertNull(testTask1.getRecordedTime());
        assertNotNull(testTask2.getRecordedTime());
        assertNull(testTask3.getRecordedTime());

        assertEquals(5, testTask2.getRecordedTime().toMinutes(), 0.001);
    }

    /**
     * Given
         * The user has started the routine 15 minutes ago
         * The user is on the first task called “Shower”
         * The second task is called “Brush Teeth”
         * The third task and last task is called “Dress”
     * When
         * The user taps “Shower”
         * And 5 minutes later taps “Dress”
     * Then
         * The routine should now display the tasks as “Shower [15m]”
         * And “Brush Teeth [-]”
         * And “Dress [5m]”
     */
    @Test
    public void bddScenario1() {
        MockITimeManager mockTime = new MockITimeManager();
        Routine routine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                new TimeTracker(mockTime));

        routine.start();
        mockTime.setMockTimeMinutes(15);

        /**
         * TODO: Add find functionality in case Morning Routine changes!
         */
        int showerId = 0;
        int brushTeethId = 1;
        int dressId = 2;

        Task shower = routine.findTaskById(showerId);
        Task brush = routine.findTaskById(brushTeethId);
        Task dress = routine.findTaskById(dressId);

        routine.checkOffById(showerId);

        mockTime.setMockTimeMinutes(20);

        routine.checkOffById(dressId);

        assertTrue(shower.isDone().getValue());
        assertFalse(brush.isDone().getValue());
        assertTrue(dress.isDone().getValue());

        assertEquals(15, shower.getRecordedTime().toMinutes(), 0.001);
        assertEquals(5, dress.getRecordedTime().toMinutes(), 0.001);
    }
}
