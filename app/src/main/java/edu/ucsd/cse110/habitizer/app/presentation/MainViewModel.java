package edu.ucsd.cse110.habitizer.app.presentation;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import static edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource.NULL_ROUTINE;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.ViewModelInitializer;


import java.util.List;

import edu.ucsd.cse110.habitizer.app.HabitizerApplication;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.time.ITimeManager;
import edu.ucsd.cse110.habitizer.lib.domain.time.TimeTracker;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;

public class MainViewModel extends ViewModel {
    private MutableNotifiableSubject<Routine> activeRoutine;
    private ITimeManager activeTimeManager;
    private final InMemoryDataSource inMemoryDataSource;
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication)creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getActiveTimeManager(), app.getInMemoryDataSource());
                    });

    public static final MainViewModel getSingletonModel(ViewModelStoreOwner modelOwner) {
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        return modelProvider.get(MainViewModel.class);
    }

    public MainViewModel(ITimeManager activeTimeManager, InMemoryDataSource inMemoryDataSource) {
        this.inMemoryDataSource = inMemoryDataSource;
        this.activeRoutine = new PlainMutableNotifiableSubject<>();
        this.activeRoutine.setValue(new Routine(NULL_ROUTINE, new TimeTracker(activeTimeManager)));;

        this.activeTimeManager = activeTimeManager;
    }

    public String getActiveRoutineName() {
        return activeRoutine.getValue().getName();
    }

    public HabitizerTime getActiveRoutineElapsedTime() {
        return activeRoutine.getValue().getElapsedTime();
    }

    public Routine getActiveRoutine() {
        return activeRoutine.getValue();
    }
    public MutableNotifiableSubject<Routine> getActiveRoutineSubject() {
        return activeRoutine;
    }

    public List<DataRoutine> getDataRoutines() {
        return inMemoryDataSource.getDataRoutineManager().getDataRoutines();
    }

    public void setActiveRoutine(DataRoutine data) {
        Routine newRoutine = new Routine(data, new TimeTracker(activeTimeManager));
        setActiveRoutine(newRoutine);
    }
    public void setActiveRoutine(Routine routine) {
        activeRoutine.setValue(routine);
        routine.getOnFlushSubject().observe(o -> {
            inMemoryDataSource.getDataRoutineManager()
                    .setDataRoutine(routine.getId(), routine.getData());
        });
    }



    public ITimeManager getActiveTimeManager() {
        return activeTimeManager;
    }
    public void setActiveTimeManager(ITimeManager timeManager) {
        activeTimeManager = timeManager;
    }
}