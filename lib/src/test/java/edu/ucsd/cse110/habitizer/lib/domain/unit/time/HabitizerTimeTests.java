package edu.ucsd.cse110.habitizer.lib.domain.unit.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

/**
 * Unit Tests for HabitizerTime
 */
public class HabitizerTimeTests {

    @Test
    public void HabitizerTimeCreation() {
        HabitizerTime testTime = new HabitizerTime(1_000_000_000);
        assertNotNull(testTime);
        assertEquals(1_000_000_000, testTime.time());
    }

    @Test
    public void testEqualsAndHashCode() {
        HabitizerTime testTime1 = new HabitizerTime(1_000_000_000);
        assertTrue(testTime1.equals(testTime1));
        assertEquals(testTime1.hashCode(), testTime1.hashCode());

        assertFalse(testTime1.equals(null));
        assertFalse(testTime1.equals(new Task(DataTask.createEmpty("Brush Teeth"))));
        HabitizerTime time2 = HabitizerTime.fromSeconds(1);

        assertTrue(testTime1.equals(time2));
        assertEquals(testTime1.hashCode(), time2.hashCode());
    }
}
