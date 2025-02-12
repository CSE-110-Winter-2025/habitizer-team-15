package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.domain.time.MockTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class RoutineTaskTests {
    @Test
    public void testScenario1() {
        MockTimeManager mockTime = new MockTimeManager();
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

        mockTime.setMockTime(15);
        routine.start();

        mockTime.setMockTime(20);

        assertEquals(20, mockTime.getMockTime(), 0.001);
        assertEquals(20, mockTime.getCurrentTimeSeconds(), 0.001);

        routine.checkOffById(testTask2.getId());

        mockTime.setMockTime(26);
        routine.end();

        // Time passes after the routine ends
        mockTime.setMockTime(30);

        // We try checking off a task after ending the routine,
        // which should use the frozen routine time instead of the current time
        routine.checkOffById(testTask3.getId());

        assertFalse(testTask1.isDone());
        assertTrue(testTask2.isDone());
        assertTrue(testTask3.isDone());

        assertNull(testTask1.getRecordedTime());

        assertNotNull(testTask2.getRecordedTime());
        assertEquals(5, testTask2.getRecordedTime().toSeconds());

        assertNotNull(testTask3.getRecordedTime());
        assertEquals(6, testTask3.getRecordedTime().toSeconds());
    }
}
