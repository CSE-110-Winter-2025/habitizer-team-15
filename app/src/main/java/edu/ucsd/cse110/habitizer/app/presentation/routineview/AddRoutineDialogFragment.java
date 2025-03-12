package edu.ucsd.cse110.habitizer.app.presentation.routineview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddRoutineBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.app.util.SimplifiedTextWatcher;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutineBuilder;
import edu.ucsd.cse110.habitizer.lib.data.DataTask;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class AddRoutineDialogFragment extends DialogFragment {

    private @NonNull FragmentDialogAddRoutineBinding view;
    private MainViewModel model;

    private static FragmentManager manager;

    AddRoutineDialogFragment () {

    }

    public static AddRoutineDialogFragment newInstance() {
        var frag = new AddRoutineDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        //manager = frag.getParentFragmentManager();
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.model = MainViewModel.getSingletonModel(getActivity());
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentDialogAddRoutineBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.routine_dialog_title)
                .setMessage(R.string.routine_dialog_message)
                .setView(view.getRoot())
                .setPositiveButton(getString(R.string.dialog_add), this::onAddClick)
                .setNegativeButton(getString(R.string.dialog_cancel), this::onCancelClick);

        AlertDialog alertDialog = builder.create();

        view.inputRoutineNameEditText.addTextChangedListener(new SimplifiedTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {;
                // We can't use .empty() here since that's on API 35
                boolean enabled = charSequence.length() != 0;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
            }
        });

        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });
        return alertDialog;
    }

    private void onAddClick(DialogInterface dialog, int which) {
        String addedRoutineName = view.inputRoutineNameEditText.getText().toString();
        if (addedRoutineName.isEmpty())
            return;

        DataRoutine routine = new DataRoutineBuilder()
            .setName(addedRoutineName)
            .addTask("Empty")
            .setId(model.getDataRoutines().size())
            .setTotalTime(HabitizerTime.fromMinutes(10))
            .build();

        model.getDataRoutineManager().addDataRoutine(routine);

        dialog.dismiss();
        model.setActiveRoutine(routine);
        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true)).commit();
    }
    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

}
