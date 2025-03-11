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
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
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

        // We can't reference alertDialog until we've actually created it
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });

        alertDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            String routineName = view.inputRoutineNameEditText.getText().toString();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!routineName.isEmpty());
            // We didn't "consume" keyEvent, so we return false
            return false;
        });
        return alertDialog;
    }

    private void onAddClick(DialogInterface dialog, int which) {
        String addedRoutineName = view.inputRoutineNameEditText.getText().toString();
        if (addedRoutineName.isEmpty())
            return;

        List<DataTask> tasks = List.of(DataTask.createEmpty("Empty"));
        DataRoutine routine = new DataRoutine(addedRoutineName, tasks , model.getDataRoutines().size(), HabitizerTime.fromMinutes(10).time());
        model.getDataRoutineManager().addDataRoutine(routine);

        dialog.dismiss();
        model.setActiveRoutine(routine);
        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true)).commit();
    }
    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }

}
