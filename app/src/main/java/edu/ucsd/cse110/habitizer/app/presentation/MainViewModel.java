package edu.ucsd.cse110.habitizer.app.presentation;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;


import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.HabitizerApplication;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class MainViewModel extends ViewModel {
    private MutableNotifiableSubject<Routine> activeRoutine;
    private ITimeManager activeTimeManager;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication)creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getActiveRoutine(), app.getActiveTimeManager());
                    });

    public static final MainViewModel getSingletonModel(ViewModelStoreOwner modelOwner) {
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        return modelProvider.get(MainViewModel.class);
    }

    public MainViewModel(@NonNull Routine routine, ITimeManager activeTimeManager) {
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
    public MutableNotifiableSubject<Routine> getRoutineSubject() {
        return activeRoutine;
    }

    public ITimeManager getActiveTimeManager() {
        return activeTimeManager;
    }
}