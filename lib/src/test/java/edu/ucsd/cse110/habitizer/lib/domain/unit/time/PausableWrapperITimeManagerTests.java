package edu.ucsd.cse110.habitizer.lib.domain.unit.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.PausableWrapperITimeManager;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Unit tests for PausableWrapperITimeManager
 */
public class PausableWrapperITimeManagerTests {

    @Test
    public void testConstructor() {
        MockITimeManager mockTime = new MockITimeManager();
        mockTime.setMockTimeMinutes(0);

        PausableWrapperITimeManager testPausableTimeManager = new PausableWrapperITimeManager(mockTime);

        // Should be initialized to false
        assertFalse(testPausableTimeManager.isPaused());

        // Should always return the same time as mockTime
        // Since diffTime should be 0
        for (int i = 0; i < 10; ++i) {
            int newTime = (int) (100 * Math.random());
            mockTime.setMockTimeMinutes(newTime);
            assertEquals(mockTime.getCurrentTime(), testPausableTimeManager.getCurrentTime());
        }
    }

    @Test
    public void testGetCurrentTime() {
        MockITimeManager mockTime = new MockITimeManager();
        mockTime.setMockTimeMinutes(0);

        PausableWrapperITimeManager testPausableTimeManager = new PausableWrapperITimeManager(mockTime);

        // Pause starts as false -> tested by constructor test. Diff time is 0
        assertEquals(mockTime.getCurrentTime(), testPausableTimeManager.getCurrentTime());

        assertTrue(testPausableTimeManager.switchPause());

        mockTime.setMockTimeMinutes(5);

        // pauseTime (0) should be returned
        assertEquals(HabitizerTime.zero, testPausableTimeManager.getCurrentTime());

        assertFalse(testPausableTimeManager.switchPause());

        // diffTime is now -5
        assertEquals(HabitizerTime.zero, testPausableTimeManager.getCurrentTime());

        // Pausable Manager's getCurrentTime is 0 when unpaused, but now should become 2
        // mockTime's time = 7. diffTime = -5. getCurrentTime should be 7 + (-5) = 2
        mockTime.setMockTimeMinutes(7);
        assertEquals(HabitizerTime.fromMinutes(2), testPausableTimeManager.getCurrentTime());
    }

    @Test
    public void testSwitchPause() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager testPausableTimeManager = new PausableWrapperITimeManager(mockTime);

        mockTime.setMockTimeMinutes(12);

        assertTrue(testPausableTimeManager.switchPause());

        // Pause time should be 12
        assertTrue(testPausableTimeManager.isPaused());

        mockTime.setMockTimeMinutes(15);

        assertFalse(testPausableTimeManager.switchPause());

        // getCurrentTime should still return 12 -> tested in testGetCurrentTime

        assertFalse(testPausableTimeManager.isPaused());
    }

    @Test
    public void testForward() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager testPausableTimeManager = new PausableWrapperITimeManager(mockTime);

        mockTime.setMockTimeMinutes(4);

        int currForwardedSeconds = 0;
        for (int i = 1; i < 10; ++i) {
            assertTrue(testPausableTimeManager.switchPause());

            testPausableTimeManager.forward(15 * i);
            currForwardedSeconds += 15 * i;
            assertEquals(HabitizerTime.fromSeconds(4 * HabitizerTime.minutesToSeconds + currForwardedSeconds),
                         testPausableTimeManager.getCurrentTime());

            assertFalse(testPausableTimeManager.switchPause());

            testPausableTimeManager.forward(15 * i);
            currForwardedSeconds += 15 * i;
            assertEquals(HabitizerTime.fromSeconds(4 * HabitizerTime.minutesToSeconds + currForwardedSeconds),
                         testPausableTimeManager.getCurrentTime());
        }

    }

    @Test
    public void testIsPaused() {
        MockITimeManager mockTime = new MockITimeManager();
        PausableWrapperITimeManager testPausableTimeManager = new PausableWrapperITimeManager(mockTime);

        for (int i = 0; i < 10; ++i) {
            mockTime.setMockTimeSeconds(i);
            assertFalse(testPausableTimeManager.isPaused());
            assertTrue(testPausableTimeManager.switchPause());
            assertTrue(testPausableTimeManager.isPaused());

            mockTime.setMockTimeSeconds(i * 2);
            assertFalse(testPausableTimeManager.switchPause());
        }
    }
}
