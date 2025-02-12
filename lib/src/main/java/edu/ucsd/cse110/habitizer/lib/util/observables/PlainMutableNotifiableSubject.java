package edu.ucsd.cse110.habitizer.lib.util.observables;

import edu.ucsd.cse110.observables.PlainMutableSubject;

/**
 * Like PlainMutableSubject, except its observers can be notified from the outside
 * in case notifications are needed when modifying (rather than setting) the encapsulated value.
 * @param <T>
 */
public class PlainMutableNotifiableSubject<T> extends PlainMutableSubject<T> implements MutableNotifiableSubject<T> {
    public void updateObservers() {
        setValue(getValue());
    }
}
