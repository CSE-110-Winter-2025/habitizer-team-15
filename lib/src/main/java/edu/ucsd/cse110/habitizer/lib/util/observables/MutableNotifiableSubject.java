package edu.ucsd.cse110.habitizer.lib.util.observables;

import edu.ucsd.cse110.observables.MutableSubject;

public interface MutableNotifiableSubject<T> extends MutableSubject<T>, NotifiableSubject<T> {
}
