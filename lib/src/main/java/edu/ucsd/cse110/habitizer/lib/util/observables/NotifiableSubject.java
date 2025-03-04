package edu.ucsd.cse110.habitizer.lib.util.observables;

import edu.ucsd.cse110.observables.Subject;

public interface NotifiableSubject<T> extends Subject<T> {
    void updateObservers();
}
