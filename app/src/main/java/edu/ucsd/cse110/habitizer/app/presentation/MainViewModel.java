package edu.ucsd.cse110.habitizer.app.presentation;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;


import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.HabitizerApplication;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.time.DebugJavaTimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class MainViewModel extends ViewModel {
    private MutableNotifiableSubject<Routine> activeRoutine;
    private RoutineRepository routineRepository;
    public static final ViewModelInitializer<MainViewModel> initializer =
        new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (HabitizerApplication)creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getRoutineRepository());
            });

    public MainViewModel(@NonNull RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;


        this.activeRoutine = null;
        //activeRoutine.setValue(routine);
    }
//    public MainViewModel(@NonNull Routine routine) {
//        this.activeRoutine = new PlainMutableNotifiableSubject<>();
//        activeRoutine.setValue(routine);
//    }

    public Routine startMorningRoutine() {
        this.activeRoutine = new PlainMutableNotifiableSubject<>();
        this.activeRoutine.setValue(new Routine(routineRepository.getMorningRoutine(),
                new TimeTracker(new DebugJavaTimeManager())));
        this.activeRoutine.getValue().start();
        return this.activeRoutine.getValue();
    }

    public Routine startEveningRoutine() {
        this.activeRoutine = new PlainMutableNotifiableSubject<>();
        this.activeRoutine.setValue(new Routine(routineRepository.getEveningRoutine(),
                new TimeTracker(new DebugJavaTimeManager())));
        this.activeRoutine.getValue().start();
        return this.activeRoutine.getValue();
    }

    public String getRoutineName() {
        return Objects.requireNonNull(activeRoutine.getValue()).getName();
    }

    public HabitizerTime getElapsedTime() {
        return Objects.requireNonNull(activeRoutine.getValue()).getElapsedTime();
    }

    public Routine getRoutine() {
        return activeRoutine.getValue();
    }


}
