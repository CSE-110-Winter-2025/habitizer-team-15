package edu.ucsd.cse110.habitizer.app.presentation.taskview.edit;

import static java.lang.Long.parseLong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogEditGoalTimeBinding;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogRenameTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.HabitizerTime;

public class EditRoutineGoalTimeFragment extends DialogFragment {
    private @NonNull FragmentDialogEditGoalTimeBinding view;
    private MainViewModel model;
    public Routine routine;

    EditRoutineGoalTimeFragment() {

    }

    public static EditRoutineGoalTimeFragment newInstance() {
        var frag = new EditRoutineGoalTimeFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static EditRoutineGoalTimeFragment newInstance(Routine routine) {
        var frag = EditRoutineGoalTimeFragment.newInstance();
        frag.routine = routine;
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
        this.view = FragmentDialogEditGoalTimeBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Edit Goal Time")
                .setMessage("Enter New Goal Time")
                .setView(view.getRoot())
                .setPositiveButton("Apply", this::onApplyClick)
                .setNegativeButton(R.string.dialog_cancel, this::onCancelClick);

        AlertDialog alertDialog = builder.create();


        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });

        // For some reason, this listener works on Android keyboard despite
        // the documentation saying otherwise
        alertDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            String goalTime = view.inputGoalTimeEditText.getText().toString();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(!goalTime.isEmpty());
            // We didn't "consume" keyEvent, so we return false
            return false;
        });
        return alertDialog;
    }


    private void onApplyClick(DialogInterface dialog, int which) {
        String newGoalTime = view.inputGoalTimeEditText.getText().toString();
        if (newGoalTime.isEmpty()) {
            return;
        }
        var timeToSet = HabitizerTime.fromMinutes(parseLong(newGoalTime)).time();
        routine.setTotalTime(timeToSet);
        dialog.dismiss();
    }

    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
