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
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class RoutinePauseTests {

    /**
     * Scenario 1: User taps pause in ongoing routine
     * Given
         * The "Morning" routine has been started
         * And the task "Brush Teeth" has been checked off
     * When
         * The user taps on the "Pause" button
     * Then
         * The live time stops updating
         * The time since last checkoff stops updating
         * The pause button becomes the "Resume" button
         * The user is unable to check off a task
     */
    @Test
    public void bddScenario1() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager pausableTimeManager = new PausableWrapperITimeManager(mockTime);
        TimeTracker timeTracker = new TimeTracker(pausableTimeManager);

        int brushTeethId = 1;

        Routine testRoutine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                                          timeTracker);

        testRoutine.start();
        mockTime.setMockTimeSeconds(5);

        assertFalse(testRoutine.isPaused());

        testRoutine.checkOffById(brushTeethId);

        // Normal checkoff
        Task brushTeethTask = testRoutine.findTaskById(brushTeethId);
        assertTrue(brushTeethTask.isDone().getValue());
        assertNotNull(brushTeethTask.getRecordedTime());

        mockTime.setMockTimeMinutes(6);

        HabitizerTime beforePauseTime = testRoutine.getElapsedTime();
        // Should be 1 since we just checked off
        HabitizerTime timeSinceLastCheckoffBeforePause = timeTracker.getCheckoffTime();
        // User "taps" on pause button
        assertTrue(timeTracker.switchPause());

        // When time is paused, changing mockTime shouldn't change elapsed nor lastcheckoff time
        mockTime.setMockTimeMinutes(7);

        assertEquals(beforePauseTime, testRoutine.getElapsedTime());

        // Elapsed hasn't changed & lastCheckOffTime shouldn't change so getCheckoffTime shouldn't either
        assertEquals(timeSinceLastCheckoffBeforePause, timeTracker.getCheckoffTime());

        for (int i = 0; i < testRoutine.size(); ++i) {
            if (i == brushTeethId) continue;

            testRoutine.checkOffById(i);
            Task currTask = testRoutine.findTaskById(i);

            assertFalse(currTask.isDone().getValue());
            assertNull(currTask.getRecordedTime());
        }
    }
}
