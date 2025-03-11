package edu.ucsd.cse110.habitizer.app.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Abstract class for overriding extraneous methods with empty methods.
 */
public abstract class SimplifiedTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
