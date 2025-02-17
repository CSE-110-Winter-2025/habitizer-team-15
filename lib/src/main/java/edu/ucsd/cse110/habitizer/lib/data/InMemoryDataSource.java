package edu.ucsd.cse110.habitizer.lib.data;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;

public class InMemoryDataSource {

    public static final DataRoutine MORNING_ROUTINE =
            new DataRoutine("Morning",
                    List.of(
                            DataTask.createWithoutId("Shower"),
                            DataTask.createWithoutId("Brush teeth"),
                            DataTask.createWithoutId("Dress"),
                            DataTask.createWithoutId("Make coffee"),
                            DataTask.createWithoutId("Make lunch"),
                            DataTask.createWithoutId("Dinner prep"),
                            DataTask.createWithoutId("Pack bag")
                    ), -1);

    public static final DataRoutine EVENING_ROUTINE =
            new DataRoutine("Evening",
                    List.of(
                            DataTask.createWithoutId("Shower"),
                            DataTask.createWithoutId("Brush teeth"),
                            DataTask.createWithoutId("Pajamas"),
                            DataTask.createWithoutId("Dinner")
                    ), -2);
    public static final List<DataTask> EVENING_ROUTINE_TASKS = List.of(
    );

    public List<DataRoutine> routines;

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.routines = new ArrayList<>();
        data.routines.add(MORNING_ROUTINE);
        data.routines.add(EVENING_ROUTINE);
        return data;
    }

    public List<DataRoutine> getAllRoutines() {
        return routines;
    }

    //    public MutableNotifiableSubject<Routine> find(int id) {
//        return dataSource.getRoutineSubject(id);
//    }
//
//    public MutableNotifiableSubject<List<Routine>> findAll() {
//        return dataSource.getAllRoutinesSubject();
//    }
//    public Subject<Flashcard> getFlashcardSubject(int id) {
//        if (!flashcardSubjects.containsKey(id)) {
//            var subject = new Subject<Flashcard>();
//            subject.setValue(getFlashcard(id));
//            flashcardSubjects.put(id, subject);
//        }
//        return flashcardSubjects.get(id);
//    }
//
//    public Subject<List<Flashcard>> getAllFlashcardsSubject() {
//        return allFlashcardsSubject;
//    }


//    public MutableNotifiableSubject<Routine> getRoutineSubject(int id) {
//
//
//    }
}