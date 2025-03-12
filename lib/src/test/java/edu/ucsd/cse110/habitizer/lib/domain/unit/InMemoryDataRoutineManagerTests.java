package edu.ucsd.cse110.habitizer.lib.domain.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.xml.crypto.Data;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

/**
 * Unit tests for InMemoryDataRoutineManager
 */
public class InMemoryDataRoutineManagerTests {
    List<DataRoutine> routines = new ArrayList<>(
                                         List.of(InMemoryDataSource.NULL_ROUTINE,
                                                InMemoryDataSource.DATA_EVENING_ROUTINE,
                                                InMemoryDataSource.DATA_MORNING_ROUTINE)
                                     );

    @Test
    public void testConstructor() {
        InMemoryDataRoutineManager routineManager = new InMemoryDataRoutineManager();

        assertEquals(0, routineManager.getDataRoutines().size());

        // Check if dataRoutines are updatingIds on change.

        // Old id is -1
        routineManager.addDataRoutine(InMemoryDataSource.NULL_ROUTINE);

        // Old id is 1
        routineManager.addDataRoutine(InMemoryDataSource.DATA_EVENING_ROUTINE);

        // Old id = 0
        routineManager.addDataRoutine(InMemoryDataSource.DATA_MORNING_ROUTINE);

        List<DataRoutine> dataRoutines = routineManager.getDataRoutines();

        assertEquals(InMemoryDataSource.NULL_ROUTINE.newWithId(0),
                     dataRoutines.get(0));
        assertEquals(InMemoryDataSource.DATA_EVENING_ROUTINE.newWithId(1),
                     dataRoutines.get(1));
        assertEquals(InMemoryDataSource.DATA_MORNING_ROUTINE.newWithId(2),
                     dataRoutines.get(2));
    }

    @Test
    public void testUpdateRoutineIds() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        List<DataRoutine> routines = new ArrayList<>(List.copyOf(this.routines));

        IntStream.range(0, routines.size())
        .forEach(index -> {
            DataRoutine routine = routines.get(index);
            routines.set(index, routine.newWithId(index));
        });

        // Old id is -1
        testRoutineManager.addDataRoutine(InMemoryDataSource.NULL_ROUTINE);

        // Old id is 1
        testRoutineManager.addDataRoutine(InMemoryDataSource.DATA_EVENING_ROUTINE);

        // Old id = 0
        testRoutineManager.addDataRoutine(InMemoryDataSource.DATA_MORNING_ROUTINE);

        List<DataRoutine> dataRoutines = testRoutineManager.getDataRoutines();

        assertEquals(routines.get(0), dataRoutines.get(0));
        assertEquals(routines.get(1), dataRoutines.get(1));
        assertEquals(routines.get(2), dataRoutines.get(2));
    }

    @Test
    public void testSize() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        for (int i = 0; i < 10; ++i) {
            assertEquals(i, testRoutineManager.size());
            testRoutineManager.addDataRoutine(InMemoryDataSource.NULL_ROUTINE);
        }
    }

    @Test
    public void testClearDataRoutines() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        for (int i = 0; i < 50; ++i) {
            if (i % 5 == 0) {
                testRoutineManager.clearDataRoutines();
                assertEquals(0, testRoutineManager.getDataRoutines().size());
                continue;
            }
            testRoutineManager.addDataRoutine(InMemoryDataSource.NULL_ROUTINE);
            assertEquals(i % 5, testRoutineManager.getDataRoutines().size());
        }
    }

    /**
     * Tests the two overrided methods
     */
    @Test
    public void testAddDataRoutine() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        assertEquals(0, testRoutineManager.size());

        for (int i = 0; i < 10; ++i) {
            testRoutineManager.addDataRoutine(routines.get(i % 3));
            assertEquals(routines.get(i % 3).newWithId(i),
                         testRoutineManager.getDataRoutines().get(i));
        }

        InMemoryDataRoutineManager testRoutineManager2 = new InMemoryDataRoutineManager();
        assertEquals(0, testRoutineManager2.size());

        for (int i = 0; i < 10; ++i) {
            testRoutineManager2.addDataRoutine(0, routines.get(i % 3));
            assertEquals(routines.get(i % 3).newWithId(0),
                         testRoutineManager2.getDataRoutines().get(0));
        }
    }

    @Test
    public void testSetDataRoutine() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        assertEquals(0, testRoutineManager.size());

        for (int i = 0; i < 10; ++i) {
            testRoutineManager.addDataRoutine(routines.get(i % 3));
        }

        for (int i = 0; i < 10; ++i) {
            testRoutineManager.setDataRoutine(i, routines.get((i + 1) % 3));
            assertEquals(routines.get((i + 1) % 3).newWithId(i),
                         testRoutineManager.getDataRoutines().get(i));
        }
    }

    /**
     * Testing getters together
     */
    @Test
    public void getDataRoutines() {
        InMemoryDataRoutineManager testRoutineManager = new InMemoryDataRoutineManager();
        List<DataRoutine> expectedDataRoutines = new ArrayList<>();

        for (int i = 0; i < 10; ++i) {
            testRoutineManager.addDataRoutine(routines.get(i % 3));
            expectedDataRoutines.add(routines.get(i % 3).newWithId(i));

            List<DataRoutine> dataRoutines = testRoutineManager.getDataRoutines();

            assertEquals(expectedDataRoutines, dataRoutines);
            assertEquals(dataRoutines, testRoutineManager.getDataRoutineSubject().getValue());
        }
    }
}
