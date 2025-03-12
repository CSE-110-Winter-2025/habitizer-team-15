package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class AsyncRoutineTimerBDDTest {
    // Async Routine Timer Tests

//    Scenario #1: Display how long finished Routine took
//    Given
//    The user has started the routine 15 minutes ago
//    The user is on the first task called “Shower”
//    The second task is called “Brush Teeth”
//    The third and last task is called “Dress”
//    When
//    The user taps “Shower”
//    And 5 minutes later taps “Brush Teeth”
//    And 5 minutes after than taps “Dress”
//    Then
//    The routine should display/indicate the routine took 25 minutes (15 + 5 + 5)


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

        mockTime.setMockTimeMinutes(15);

        testRoutine.checkOffById(0);

        mockTime.addMockTimeMinutes(5);

        testRoutine.checkOffById(1);

        mockTime.addMockTimeMinutes(5);

        assertEquals(25, (long) testRoutine.getElapsedTime().toMinutes());

        testRoutine.end();

        assertEquals(25, (long) testRoutine.getElapsedTime().toMinutes());
    }

//    BDD Scenario 1 for Story 14
//
//    Given that I am on the Routines screen
//    When I tap a routine to start it
//    Then I'm shown the routine's Routine screen and the the routine's elapsed time displays "0m"
//    When I wait 35 seconds (since I started the routine)
//    Then the routine's elapsed time (still) displays "0m"
//    When I wait 25 more seconds (60 total seconds have elapsed)
//    Then the routine's elapsed time displays "1m"

    @Test
    public void bddScenario2() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager pausableTimeManager = new PausableWrapperITimeManager(mockTime);
        TimeTracker mockTimeTracker = new TimeTracker(pausableTimeManager);
        Routine testRoutine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                mockTimeTracker);

        testRoutine.start();

        assertEquals(0, (long) testRoutine.getElapsedTime().toMinutes());

        mockTime.setMockTimeSeconds(35);

        assertEquals(0, (long) testRoutine.getElapsedTime().toMinutes());

        mockTime.addMockTimeSeconds(25);

        assertEquals(1, (long) testRoutine.getElapsedTime().toMinutes());
    }

//    BDD Scenario 2 for Story 14
//
//    Given that I am on the Routines screen
//    When I tap a routine to start it
//    Then I'm shown the routine's Routine screen and the the routine's elapsed time displays "0m"
//    When I tap the Stop button (Story 3b)
//    Then the Stop button turns into an Advance button (and, internally, the routine timer has been stopped)
//    When I tap the Advance button 2 times (i.e., advance time 30 seconds)
//    Then the routine's elapsed time (still) displays "0m"
//    When I tap the Advance button 2 (more) times (i.e., 60 seconds total)
//    Then the routine's elapsed time displays "1m"

    @Test
    public void bddScenario3() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager pausableTimeManager = new PausableWrapperITimeManager(mockTime);
        TimeTracker mockTimeTracker = new TimeTracker(pausableTimeManager);
        Routine testRoutine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                mockTimeTracker);

        testRoutine.start();

        assertEquals(0, (long) testRoutine.getElapsedTime().toMinutes());

        pausableTimeManager.switchPause();
        pausableTimeManager.forward(15);
        pausableTimeManager.forward(15);

        assertEquals(0, (long) testRoutine.getElapsedTime().toMinutes());

        pausableTimeManager.forward(15);
        pausableTimeManager.forward(15);

        assertEquals(1, (long) testRoutine.getElapsedTime().toMinutes());
    }
}
