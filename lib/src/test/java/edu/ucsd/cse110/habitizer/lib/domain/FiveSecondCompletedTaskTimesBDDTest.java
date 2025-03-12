package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;

public class FiveSecondCompletedTaskTimesBDDTest {

    // Scenario 1: User sees task time while on shower
    //   Given
    //       User is in morning routine
    //       Shower exists first
    //       Brush Teeth exists second
    //       Dress exists third
    //   When
    //       The user taps Shower 27 secs after routine has started
    //   Then
    //       The user should see the Shower task display 30s as its task time
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

        routine.start();
        mockTime.setMockTimeSeconds(27);

        routine.checkOff(shower);

        // REPLICA CODE OF 5 SECOND INCREMENTER IN TASKVIEWADAPTER
        double actual;
        var time = shower.getRecordedTime();
        long calculatedTime = (long) (Math.ceil(time.toSeconds() / 5.0) * 5);
        if (calculatedTime < 60){
            actual = calculatedTime;
        }
        else {
            actual = (long) Math.ceil(time.toMinutes());
        }

        assertEquals(27, shower.getRecordedTime().toSeconds(), 0.0001);
        assertEquals(30, actual, 0.0001);
    }
}
