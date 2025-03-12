package edu.ucsd.cse110.habitizer.lib.domain.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class ManuallyAdvanceTimeTests {

    /**
     * Scenario 1: Tester forwards from 14 to 29 seconds
     * Given
         * The tester has started "Morning" routine
         * 14 seconds has passed w/out checkoff
         * The tester is on the first task called "Shower"
     * When
         * The tester taps the forward button once
         * And then taps the "Shower" task
     * Then
         * The routine should now display the tasks as “Shower [30 s]”
         * And the rest of the tasks as "Task [-]"
     */
    @Test
    public void bddScenario1() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager pausableTimeManager = new PausableWrapperITimeManager(mockTime);
        TimeTracker mockTimeTracker = new TimeTracker(pausableTimeManager);
        Routine testRoutine = new Routine(InMemoryDataSource.DATA_MORNING_ROUTINE,
                mockTimeTracker);

        testRoutine.start();
        mockTime.setMockTimeSeconds(14);

        int showerId = 0;
        assertEquals(HabitizerTime.fromSeconds(14), testRoutine.getElapsedTime());
        pausableTimeManager.forward(15);


        // Although time is recorded time is 29 seconds, TaskViewAdapter handles displaying rounded
        // task time.
        assertEquals(HabitizerTime.fromSeconds(29), testRoutine.getElapsedTime());

        testRoutine.checkOffById(showerId);

        assertEquals(HabitizerTime.fromSeconds(29), testRoutine.findTaskById(showerId)
                                                               .getRecordedTime());

        for (int i = 1; i < testRoutine.size(); ++i) {
            Task task = testRoutine.findTaskById(i);
            assertNull(task.getRecordedTime());
            assertFalse(task.isDone().getValue());
        }
    }
}
