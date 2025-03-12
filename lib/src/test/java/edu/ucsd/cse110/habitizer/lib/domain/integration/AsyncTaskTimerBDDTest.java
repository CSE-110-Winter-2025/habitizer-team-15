package edu.ucsd.cse110.habitizer.lib.domain.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class AsyncTaskTimerBDDTest {
    // Async Task Timer Tests

//    Scenario 1:
//    Given
//    The Morning routine has been started
//    And 12 minutes have passed
//    And nothing has been checked off
//    And 2 live timers with the same numbers are visible in the status bar
//    When
//    The user checks off “Shower”
//    Then
//    The checkoff time is recorded for the “Shower” task
//    The left timer restarts from 0
//    The right timer continues as-is

    @Test
    public void bddScenario1() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager pausableTimeManager = new PausableWrapperITimeManager(mockTime);
        TimeTracker mockTimeTracker = new TimeTracker(pausableTimeManager);
        Routine testRoutine = new Routine("Test Routine", mockTimeTracker);

        Task testTask1 = new Task("Shower");
        testRoutine.addTask(testTask1);

        Task testTask2 = new Task("Brush Teeth");
        testRoutine.addTask(testTask2);

        Task testTask3 = new Task("Dress");
        testRoutine.addTask(testTask3);

        testRoutine.start();

        mockTime.setMockTimeMinutes(12);

        assertEquals((long) testRoutine.getCheckoffTime().toMinutes(), (long) testRoutine.getElapsedTime().toMinutes());

        testRoutine.checkOffById(0);

        assertEquals(0, (long) testRoutine.getCheckoffTime().toMinutes());
        assertEquals(12, (long) testRoutine.getElapsedTime().toMinutes());

        testRoutine.end();
    }
}
