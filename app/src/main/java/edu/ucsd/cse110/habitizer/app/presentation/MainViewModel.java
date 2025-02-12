package edu.ucsd.cse110.habitizer.app.presentation;

import androidx.lifecycle.ViewModel;


import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableNotifiableSubject;
import edu.ucsd.cse110.observables.Subject;

public class MainViewModel extends ViewModel {
    private MutableNotifiableSubject<Routine> activeRoutine;

    public MainViewModel(Routine routine) {
        this.activeRoutine = new PlainMutableNotifiableSubject<>();
        activeRoutine.setValue(routine);



    }
}
