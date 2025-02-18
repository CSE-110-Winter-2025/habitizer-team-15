package edu.ucsd.cse110.habitizer.app.presentation;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;


import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.HabitizerApplication;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeManager;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class MainViewModel extends ViewModel {
    private MutableNotifiableSubject<Routine> activeRoutine;
    private TimeManager activeTimeManager;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication)creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getActiveRoutine(), app.getActiveTimeManager());
                    });

    public MainViewModel(@NonNull Routine routine, TimeManager activeTimeManager) {
        this.activeRoutine = new PlainMutableNotifiableSubject<>();
        activeRoutine.setValue(routine);

        this.activeTimeManager = activeTimeManager;
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

    public TimeManager getActiveTimeManager() {
        return activeTimeManager;
    }

    public void checkOff(int id) {
        getRoutine().checkOffById(id);
    }
}