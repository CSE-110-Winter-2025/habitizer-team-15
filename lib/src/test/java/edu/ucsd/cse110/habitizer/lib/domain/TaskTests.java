package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.conversions.DataDomainConverter;

/**
 * Unit tests for Task
 */
public class TaskTests {
    private final HabitizerTime taskRecordedTime1 = new HabitizerTime(42);
    private final HabitizerTime taskRecordedTime2 = new HabitizerTime(-1);
    private final String taskName1 = "Test Task 1";
    private final String taskName2 = "Test Task 2";
    private final int taskId1 = 123;
    private final int taskId2 = -456;
    private final DataTask testDataTask1 = new DataTask(taskName1, taskId1);
    private final List<DataTask> testDataTasks1 = InMemoryDataSource.DATA_MORNING_ROUTINE.dataTasks();

    /**
     * Tests Constructor that is passed in a DataTask
     */
    @Test
    public void testTaskConstructorWithDataTask() {
        Task testTask = new Task(testDataTask1);

        assertNull(testTask.getRecordedTime());

        assertEquals("Test Task 1", testTask.getName());
        assertEquals(123, testTask.getId());

        assertNotNull(testTask.isDone());
        assertFalse(testTask.isDone().getValue());
    }

    /**
     * Tests Constructor that task "name" as a String
     */
    @Test
    public void testTaskConstructorWithTaskName() {
        Task testTask = new Task("Test Task 1");

        assertNull(testTask.getRecordedTime());

        assertEquals("Test Task 1", testTask.getName());
        assertEquals(-1, testTask.getId());

        assertNotNull(testTask.isDone());
        assertFalse(testTask.isDone().getValue());
    }

    /**
     * Test Getter and Setter together for recordTime
     * How to test separately?
     */
    @Test
    public void testGetAndSetRecordedTime() {
        Task testTask1 = new Task(testDataTask1);
        Task testTask2 = new Task(taskName2);

        assertNull(testTask1.getRecordedTime());
        assertNull(testTask1.getRecordedTime());

        testTask1.recordTime(taskRecordedTime1);
        testTask2.recordTime(taskRecordedTime2);

        // Check getRecordedTime()
        assertEquals(new HabitizerTime(42), testTask1.getRecordedTime());
        assertEquals(new HabitizerTime(-1), testTask2.getRecordedTime());
    }

    /**
     * Once again testing getter and setter together
     */
    @Test
    public void testGetAndSetName() {
        Task testTask1 = new Task(testDataTask1);
        Task testTask2 = new Task(taskName2);

        // Check getName() getter
        assertEquals("Test Task 1", testTask1.getName());
        assertEquals("Test Task 2", testTask2.getName());

        // Check setName() setter
        testTask1.setName("Test Task 2");
        testTask2.setName("Test Task 1");

        assertEquals("Test Task 2", testTask1.getName());
        assertEquals("Test Task 1", testTask2.getName());
    }

    @Test
    public void testGetAndSetId() {
        Task testTask1 = new Task(testDataTask1);
        Task testTask2 = new Task(taskName2);

        // Check getId() getter
        assertEquals(taskId1, testTask1.getId());
        assertEquals(-1, testTask2.getId());

        // Check setId() setter
        testTask1.setId(taskId2);
        testTask2.setId(taskId1);

        assertEquals(-456, testTask1.getId());
        assertEquals(123, testTask2.getId());
    }

    /**
     *
     */
    @Test
    public void testCheckOffAndIsDone() {
        Task testTask1 = new Task(testDataTask1);
        Task testTask2 = new Task(taskName2);

        // Test isDone when return should be false
        assertFalse(testTask1.isDone().getValue());
        assertFalse(testTask2.isDone().getValue());

        // Checks checkOff function
        testTask1.checkOff();
        testTask2.checkOff();
        // Test isDone when return should be true
        assertTrue(testTask1.isDone().getValue());
        assertTrue(testTask2.isDone().getValue());
    }

    @Test
    public void testCreateListFromDataTasks() {
        List<Task> tasks = new ArrayList<Task>();
        for (DataTask dataTask : testDataTasks1) {
            tasks.add(new Task(dataTask));
        }
        List<Task> testTasks = DataDomainConverter.dataTasksToTasks(testDataTasks1);

        assertEquals(tasks, testTasks);
    }

    /**
     * Tests all functions in a row, while checking that fields aren't changed per
     * non-getter call
     */
    @Test
    public void testAllFunctions() {
        Task testTask1 = new Task(testDataTask1);
        Task testTask2 = new Task(taskName2);

        testTask1.recordTime(new HabitizerTime(1));
        testTask2.recordTime(new HabitizerTime(-1));

        // Already tested set & get, so just check other values
        assertEquals("Test Task 1", testTask1.getName());
        assertEquals("Test Task 2", testTask2.getName());
        assertEquals(123, testTask1.getId());
        assertEquals(-1, testTask2.getId());
        assertFalse(testTask1.isDone().getValue());
        assertFalse(testTask2.isDone().getValue());

        testTask1.setId(taskId2);
        testTask2.setId(taskId1);

        // Already tested set & get, so just check other values
        assertEquals(new HabitizerTime(1), testTask1.getRecordedTime());
        assertEquals(new HabitizerTime(-1), testTask2.getRecordedTime());
        assertEquals("Test Task 1", testTask1.getName());
        assertEquals("Test Task 2", testTask2.getName());
        assertFalse(testTask1.isDone().getValue());
        assertFalse(testTask2.isDone().getValue());

        testTask1.setName("Test Task 2");
        testTask2.setName("Test Task 1");

        // Already tested set & get, so just check other values
        assertEquals(new HabitizerTime(1), testTask1.getRecordedTime());
        assertEquals(new HabitizerTime(-1), testTask2.getRecordedTime());
        assertEquals(taskId2, testTask1.getId());
        assertEquals(taskId1, testTask2.getId());
        assertFalse(testTask1.isDone().getValue());
        assertFalse(testTask2.isDone().getValue());

        testTask1.checkOff();
        testTask2.checkOff();

        // Already tested set & get, so just check other values
        assertEquals(new HabitizerTime(1), testTask1.getRecordedTime());
        assertEquals(new HabitizerTime(-1), testTask2.getRecordedTime());
        assertEquals("Test Task 2", testTask1.getName());
        assertEquals("Test Task 1", testTask2.getName());
        testTask1.setId(taskId2);
        testTask2.setId(taskId1);
    }

    // TODO: Add equals test
}