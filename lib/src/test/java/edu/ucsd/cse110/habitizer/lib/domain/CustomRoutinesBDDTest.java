package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.time.MockITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class CustomRoutinesBDDTest {

    // Scenario 1: Set up Evening Weekend Routine
    //  Given
    //      I have an evening Routines page set up
    //  When
    //      I press ‘Add’ on the Routines page
    //      And type in “Weekend Evenings” in the textbox
    //      And press ‘create’
    //  Then
    //      I should be able to see a blank new routine that I can populate with more relevant tasks
    @Test
    public void testScenario1() {
        InMemoryDataRoutineManager routineManager = new InMemoryDataRoutineManager();
        DataRoutine routine = new DataRoutineBuilder()
                .setName("Weekend Evenings")
                .addTask("Empty")
                .setId(routineManager.size())
                .setTotalTime(HabitizerTime.fromMinutes(10))
                .build();

        routineManager.addDataRoutine(routine);

        assertEquals(routineManager.getDataRoutines().get(0), routine);
        assertEquals(1, routineManager.size());
    }

}
