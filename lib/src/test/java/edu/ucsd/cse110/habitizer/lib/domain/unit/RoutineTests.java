package edu.ucsd.cse110.habitizer.lib.domain.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.conversions.DataDomainConverter;

/**
 * Unit tests for Routine.
 */
public class RoutineTests {
    private List<Task> routineTasks1 = DataDomainConverter.dataTasksToTasks(InMemoryDataSource.DATA_MORNING_ROUTINE.dataTasks());
    private List<Task> routineTasks2 = new ArrayList<>();
    private final String routineName1 = "Test Routine 1";
    private final String routineName2 = "Test Routine 2";
    private DataRoutine testDataRoutine1 = InMemoryDataSource.DATA_MORNING_ROUTINE;
    private DataRoutine testEmptyDataRoutine = DataRoutine.createEmpty(routineName1);
    private final MockITimeManager mockTimeManager = new MockITimeManager();
    private final TimeTracker mockTimeTracker = new TimeTracker(mockTimeManager);
    private final HabitizerTime routineTime1 = new HabitizerTime(42);
    private final HabitizerTime routineTime2 = new HabitizerTime(-1);
    private final Routine testRoutine = new Routine(testDataRoutine1, mockTimeTracker);
    public final DataRoutine MORNING_ROUTINE_WITH_IDS =
            new DataRoutineBuilder()
                .setName("Morning")
                .setTotalTime(45)
                .addTask("Shower", 4)
                .addTask("Brush teeth", 3)
                .addTask("Dress", 6)
                .addTask("Make coffee", 1)
                .addTask("Make lunch", 7)
                .addTask("Dinner prep", 5)
                .addTask("Pack bag", 2)
                .build();

    @Test
    public void testConstructorWithDataRoutine() {
        MockITimeManager mockTime = new MockITimeManager();
        TimeTracker mockTimeTracker = new TimeTracker(mockTime);

        Routine testRoutine = new Routine(testDataRoutine1, mockTimeTracker);

        List<Task> tasks = testRoutine.getTasksSubject().getValue();

        assertEquals(routineTasks1.size(), tasks.size());

        IntStream.range(0, testRoutine.size())
            .forEach(index -> {
                Task task = tasks.get(index);
                Task compareToTask = routineTasks1.get(index);

                assertNotNull(task);
                // Since index is set from updateTaskIds, we can't simply check for Task object equality
                //      Thus, we check index is set correctly from updateTaskIds
                assertEquals(index, task.getId());
                assertEquals(compareToTask.getRecordedTime(), task.getRecordedTime());
                assertEquals(compareToTask.getName(), task.getName());
                assertEquals(compareToTask.isDone().getValue(), task.isDone().getValue());
            });

        assertEquals("Morning", testRoutine.getName());
        assertEquals(testDataRoutine1.id(), testRoutine.getId());

        // Test if TimeTracker was set correctly
        assertEquals(HabitizerTime.zero, testRoutine.getElapsedTime());

        // Is there a better more efficient way to check if TimeTracker is correct?
        // Perhaps leave this test to just the start/getElapsedTime Test?
        mockTime.setMockTimeMinutes(5);
        testRoutine.start();
        mockTime.setMockTimeMinutes(10);
        assertEquals(5, testRoutine.getElapsedTime().toMinutes(), 0.01);
    }

    @Test
    public void testConstructorWithRoutineName() {
        MockITimeManager mockTime = new MockITimeManager();
        TimeTracker mockTimeTracker = new TimeTracker(mockTime);

        Routine testRoutine = new Routine(routineName1, mockTimeTracker);

        List<Task> tasks = new ArrayList<>();

        assertEquals(DataDomainConverter.dataTasksToTasks(new ArrayList<>()), tasks);
        assertEquals(-1, testRoutine.getId());

        // Test if TimeTracker was set correctly
        assertEquals(HabitizerTime.zero, testRoutine.getElapsedTime());

        // Is there a better more efficient way to check if TimeTracker is correct?
        // Perhaps leave this test to just the start/getElapsedTime Test?
        mockTime.setMockTimeMinutes(5);
        testRoutine.start();
        mockTime.setMockTimeMinutes(10);
        assertEquals(5, testRoutine.getElapsedTime().toMinutes(), 0.01);
    }

    // Private function, so we can only test relying on the constructor's call to updateTaskIds()
    @Test
    public void testUpdateTaskIds() {
        DataRoutine testDataRoutine = testDataRoutine1;

        // All Ids of tasks in testDataRoutine1 start as -1
        //      because InMemoryDataSource.MORNING_ROUTINE sets them so
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);

        List<Task> tasks1 = testRoutine1.getTasksSubject().getValue();

        // updateTaskIds() already called in the constructor, so we check if it was updated.
        IntStream.range(0, testRoutine1.size())
            .forEach(index -> {
                Task task = tasks1.get(index);

                assertNotNull(task);
                assertEquals(index, task.getId());
            });


        testDataRoutine = MORNING_ROUTINE_WITH_IDS;

        // All Ids of tasks in testDataRoutine1 start as a set insignificantly defined order
        //      because InMemoryDataSource.MORNING_ROUTINE_WITH_IDS sets them so
        Routine testRoutine2 = new Routine(testDataRoutine, mockTimeTracker);

        List<Task> tasks2 = testRoutine1.getTasksSubject().getValue();

        // updateTaskIds() already called in the constructor, so we check if it was updated.
        IntStream.range(0, testRoutine2.size())
        .forEach(index -> {
            Task task = tasks2.get(index);

            assertNotNull(task);
            assertEquals(index, task.getId());
        });
    }

    // HOW do you test getElapsedTime if its a function that calls another function?
    // Im assuming we rely on the fact we are supposed to test that function already
    @Test
    public void testGetNameAndNameSubject() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        assertEquals(testRoutine1.getNameSubject().getValue(), testDataRoutine1.name());
        assertEquals(testRoutine2.getNameSubject().getValue(), testEmptyDataRoutine.name());
        assertEquals(testRoutine3.getNameSubject().getValue(), routineName2);

        assertEquals(testRoutine1.getName(), testDataRoutine1.name());
        assertEquals(testRoutine2.getName(), testEmptyDataRoutine.name());
        assertEquals(testRoutine3.getName(), routineName2);
    }

    @Test
    public void testGetId() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        assertEquals(testDataRoutine1.id(), testRoutine1.getId());
        assertEquals(testEmptyDataRoutine.id(), testRoutine2.getId());
        assertEquals(-1, testRoutine2.getId());
    }

    /**
     * We test if start and end sets time fields correctly.
     * TimeTracker specific returns should be tested in TimeTracker specific tests
     */
    @Test
    public void testStartAndEnd() {
        MockITimeManager mockTime = new MockITimeManager();
        TimeTracker mockTimeTracker = new TimeTracker(mockTime);

        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);


        mockTime.setMockTimeMinutes(5);

        testRoutine1.start();

        mockTime.setMockTimeMinutes(10);

        testRoutine1.end();

        assertEquals(5, testRoutine1.getElapsedTime().toMinutes(), 0.01);
        assertEquals(5, testRoutine2.getElapsedTime().toMinutes(), 0.01);
        assertEquals(5, testRoutine3.getElapsedTime().toMinutes(), 0.01);
    }

    @Test
    public void testFindTask() {
        Routine testRoutine1 = testRoutine;

        List<Task> tasks = testRoutine1.getTasksSubject().getValue();

        IntStream.range(0, testRoutine1.size())
            .forEach(index -> {
                Task task = tasks.get(index);

                assertNotNull(task);
                assertEquals(task, testRoutine1.findTaskById(index));
                assertEquals(task, testRoutine1.findTaskByName(task.getName()));
            });

    }

    @Test
    public void testIsStarted() {
        MockITimeManager mockTime = new MockITimeManager();
        TimeTracker mockTimeTracker = new TimeTracker(mockTime);

        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        assertFalse(testRoutine1.isStarted());
        assertFalse(testRoutine2.isStarted());
        assertFalse(testRoutine3.isStarted());

        testRoutine1.start();

        assertTrue(testRoutine1.isStarted());
        assertTrue(testRoutine2.isStarted());
        assertTrue(testRoutine3.isStarted());

        testRoutine1.end();

        assertFalse(testRoutine1.isStarted());
        assertFalse(testRoutine2.isStarted());
        assertFalse(testRoutine3.isStarted());
    }

    // Includes checkOff by Task and by Id
    @Test
    public void testCheckOff() {
        MockITimeManager mockTime1 = new MockITimeManager();
        MockITimeManager mockTime2 = new MockITimeManager();
        TimeTracker mockTimeTracker1 = new TimeTracker(mockTime1);
        TimeTracker mockTimeTracker2 = new TimeTracker(mockTime2);

        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker1);
        Routine testRoutine2 = new Routine(testDataRoutine1, mockTimeTracker2);
        List<Task> tasks1 = testRoutine1.getTasksSubject().getValue();
        List<Task> tasks2 = testRoutine2.getTasksSubject().getValue();

        mockTime1.setMockTimeMinutes(5);
        mockTime2.setMockTimeMinutes(5);

        // Nothing should happen if the routine never started
        IntStream.range(0, testRoutine1.size())
            .forEach(index -> {
                Task task = tasks1.get(index);

                assertNotNull(task);

                testRoutine1.checkOff(task);

                assertNull(task.getRecordedTime());
                assertFalse(task.isDone().getValue());

                testRoutine1.checkOffById(index);

                assertNull(task.getRecordedTime());
                assertFalse(task.isDone().getValue());
            });

        testRoutine1.start();
        mockTime1.setMockTimeMinutes(20);

        testRoutine2.start();
        mockTime2.setMockTimeMinutes(25);



        // Both routines started so checking off should set recordedTime and isDone of Task
        IntStream.range(0, testRoutine1.size())
            .forEach(index -> {
                Task task1 = tasks1.get(index);
                Task task2 = tasks2.get(index);

                assertNotNull(task1);
                assertNotNull(task2);

                testRoutine1.checkOff(task1);
                testRoutine2.checkOffById(index);

                assertNotNull(task1.getRecordedTime());
                assertEquals(15, task1.getRecordedTime().toMinutes(), 0.01);
                assertTrue(task1.isDone().getValue());

                assertNotNull(task2.getRecordedTime());
                assertEquals(20, task2.getRecordedTime().toMinutes(), 0.01);
                assertTrue(task2.isDone().getValue());

                // First add by 10 to check that checkOff does not change anything
                mockTime1.addMockTimeMinutes(10);

                // First add by 15 to check that checkOff does not change anything
                mockTime2.addMockTimeMinutes(15);

                testRoutine1.checkOff(task1);
                testRoutine2.checkOffById(index);

                // checkOff should not change anything now since task isDone
                assertNotNull(task1.getRecordedTime());
                assertNotEquals(10, task1.getRecordedTime().toMinutes(), 0.01);
                assertTrue(task1.isDone().getValue());

                // checkOff should not change anything now since task isDone
                assertNotNull(task2.getRecordedTime());
                assertNotEquals(15, task2.getRecordedTime().toMinutes(), 0.01);
                assertTrue(task2.isDone().getValue());

                // Add 5 to complete the 15 minute interval
                mockTime1.addMockTimeMinutes(5);

                // Add 5 to complete the 20 minute interval
                mockTime2.addMockTimeMinutes(5);
            });

        // We add time, and check to see that calling checkOff doesn't set new time after end
        mockTime1.addMockTimeMinutes(420);
        testRoutine1.end();

        mockTime2.addMockTimeMinutes(421);
        testRoutine2.end();

        // Should not work again after
        IntStream.range(0, testRoutine1.size())
        .forEach(index -> {
            Task task = tasks1.get(index);

            assertNotNull(task);

            testRoutine1.checkOff(task);

            assertNotNull(task.getRecordedTime());
            assertEquals(15, task.getRecordedTime().toMinutes(), 0.01);
            assertTrue(task.isDone().getValue());

            testRoutine1.checkOffById(index);

            assertNotNull(task.getRecordedTime());
            assertEquals(15, task.getRecordedTime().toMinutes(), 0.01);
            assertTrue(task.isDone().getValue());
        });
    }

    @Test
    public void testSize() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        assertEquals(testDataRoutine1.dataTasks().size(), testRoutine1.size());
        assertEquals(0, testRoutine2.size());
        assertEquals(0, testRoutine3.size());
    }

    @Test
    public void testAddTask() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        List<Task> tasks1 = DataDomainConverter.dataTasksToTasks(testDataRoutine1.dataTasks());
        IntStream.range(0, tasks1.size())
            .forEach(index -> {
                Task task = tasks1.get(index);

                task.setId(index);
            });
        List<Task> tasks2 = new ArrayList<>();
        List<Task> tasks3 = new ArrayList<>();

        int currSize1 = testRoutine1.size();
        int currSize2 = testRoutine2.size();
        int currSize3 = testRoutine3.size();

        for (int index = 0; index < 30; ++index) {
            Task newTask1 = new Task(new DataTask("Task " + (1 * index), 1 * index));
            Task newTask2 = new Task(new DataTask("Task " + (2 * index), 2 * index));
            Task newTask3 = new Task(new DataTask("Task " + (3 * index), 3 * index));

            Task idealNewTask1 = new Task(new DataTask("Task " + (1 * index), currSize1));
            Task idealNewTask2 = new Task(new DataTask("Task " + (2 * index), currSize2));
            Task idealNewTask3 = new Task(new DataTask("Task " + (3 * index), currSize3));

            testRoutine1.addTask(newTask1);
            testRoutine2.addTask(newTask2);
            testRoutine3.addTask(newTask3);

            tasks1.add(idealNewTask1);
            tasks2.add(idealNewTask2);
            tasks3.add(idealNewTask3);

            // Size should have increased by one
            assertEquals(currSize1 + 1, testRoutine1.size());
            assertEquals(currSize2 + 1, testRoutine2.size());
            assertEquals(currSize3 + 1, testRoutine3.size());

            // Should be added to the end of the routine
            Task task1 = testRoutine1.findTaskById(currSize1);
            Task task2 = testRoutine2.findTaskById(currSize2);
            Task task3 = testRoutine3.findTaskById(currSize3);

            // Tasks should be equivalent to idealNewTask#
            assertEquals(idealNewTask1, task1);
            assertEquals(idealNewTask2, task2);
            assertEquals(idealNewTask3, task3);

            // Everything else should be the same
            assertEquals(tasks1, testRoutine1.getTasksSubject().getValue());
            assertEquals(tasks2, testRoutine2.getTasksSubject().getValue());
            assertEquals(tasks3, testRoutine3.getTasksSubject().getValue());

            ++currSize1;
            ++currSize2;
            ++currSize3;

        }
    }

    // Includes removal by Task and by Id
    @Test
    public void testRemoveTask() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        List<Task> tasks1 = new ArrayList<>(DataDomainConverter.dataTasksToTasks(testDataRoutine1.dataTasks()));
        IntStream.range(0,tasks1.size())
            .forEach(index -> {
                Task task = tasks1.get(index);

                task.setId(index);
            });
        List<Task> tasks2 = new ArrayList<>();
        List<Task> tasks3 = new ArrayList<>();

        int currSize1 = testRoutine1.size();
        int currSize2 = testRoutine2.size();
        int currSize3 = testRoutine3.size();

        for (int index = 0; index < 30; ++index) {

            if (currSize1 == 0) break;

            int removeIndex = (int) Math.floor(Math.random() * currSize1);
            Task task1 = testRoutine1.getTasksSubject().getValue().get(removeIndex);

            // task1 should be removed from 1st routine, but not for 2nd/3rd routine
            testRoutine1.removeTask(task1);
            testRoutine2.removeTask(task1);
            testRoutine3.removeTask(task1);

            // Nothing should be removed from passing null
            // Contractually, the Task should be non-null now
            // testRoutine1.removeTask(null);
            // testRoutine2.removeTask(null);
            // testRoutine3.removeTask(null);

            tasks1.remove(removeIndex);

            // Size should remain the same for empty routines, else decrease 1
            assertEquals(currSize1 - 1, testRoutine1.size());
            assertEquals(currSize2, testRoutine2.size());
            assertEquals(currSize3, testRoutine3.size());

            currSize1 = tasks1.size();
            currSize2 = tasks2.size();
            currSize3 = tasks3.size();
        }

    }

    @Test
    public void testGetTasksSubject() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testEmptyDataRoutine, mockTimeTracker);
        Routine testRoutine3 = new Routine(routineName2, mockTimeTracker);

        List<Task> tasks1 = DataDomainConverter.dataTasksToTasks(testDataRoutine1.dataTasks());
        IntStream.range(0, tasks1.size())
            .forEach(index -> {
                Task task = tasks1.get(index);

                assertNotNull(task);
                task.setId(index);
            });

        List<Task> tasks2 = new ArrayList<>();
        List<Task> tasks3 = new ArrayList<>();

        assertEquals(tasks1, testRoutine1.getTasksSubject().getValue());
        assertEquals(tasks2, testRoutine2.getTasksSubject().getValue());
        assertEquals(tasks3, testRoutine3.getTasksSubject().getValue());
    }

    @Test
    public void testMoveTaskUp() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testDataRoutine1, mockTimeTracker);


        int showerId = 0;
        int brushTeethId = 1;
        int dressId = 2;

        Task shower1 = testRoutine1.findTaskById(showerId);
        Task brush1 = testRoutine1.findTaskById(brushTeethId);
        Task dress1 = testRoutine1.findTaskById(dressId);

        Task shower2 = testRoutine2.findTaskById(showerId);
        Task brush2 = testRoutine2.findTaskById(brushTeethId);
        Task dress2 = testRoutine2.findTaskById(dressId);


        // Routine1
        // check start order
        assertEquals(testRoutine1.getTasksSubject().getValue().get(0), shower1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(1), brush1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(2), dress1);

        // call moveTaskUp
        testRoutine1.moveTaskUp(brush1);

        // check order after moving brush up
        assertEquals(testRoutine1.getTasksSubject().getValue().get(0), brush1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(1), shower1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(2), dress1);

        //Routine2
        // check start order
        assertEquals(testRoutine2.getTasksSubject().getValue().get(0), shower2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(1), brush2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(2), dress2);

        // call moveTaskUp on top task
        testRoutine2.moveTaskUp(shower2);

        // order should stay the same
        assertEquals(testRoutine2.getTasksSubject().getValue().get(0), shower2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(1), brush2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(2), dress2);
    }

    @Test
    public void testMoveTaskDown() {
        Routine testRoutine1 = new Routine(testDataRoutine1, mockTimeTracker);
        Routine testRoutine2 = new Routine(testDataRoutine1, mockTimeTracker);


        int showerId = 0;
        int brushTeethId = 1;
        int dressId = 2;
        int packId = 6;

        Task shower1 = testRoutine1.findTaskById(showerId);
        Task brush1 = testRoutine1.findTaskById(brushTeethId);
        Task dress1 = testRoutine1.findTaskById(dressId);

        Task shower2 = testRoutine2.findTaskById(showerId);
        Task brush2 = testRoutine2.findTaskById(brushTeethId);
        Task dress2 = testRoutine2.findTaskById(dressId);
        Task pack = testRoutine2.findTaskById(packId);


        // Routine1
        // check start order
        assertEquals(testRoutine1.getTasksSubject().getValue().get(0), shower1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(1), brush1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(2), dress1);

        // call moveTaskDown
        testRoutine1.moveTaskDown(shower1);

        // check order after moving shower down
        assertEquals(testRoutine1.getTasksSubject().getValue().get(0), brush1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(1), shower1);
        assertEquals(testRoutine1.getTasksSubject().getValue().get(2), dress1);

        //Routine2
        // check start order
        assertEquals(testRoutine2.getTasksSubject().getValue().get(0), shower2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(1), brush2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(6), pack);

        // call moveTaskDown on bottom task
        testRoutine2.moveTaskDown(pack);

        // order should stay the same
        assertEquals(testRoutine2.getTasksSubject().getValue().get(0), shower2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(1), brush2);
        assertEquals(testRoutine2.getTasksSubject().getValue().get(6), pack);
    }
}