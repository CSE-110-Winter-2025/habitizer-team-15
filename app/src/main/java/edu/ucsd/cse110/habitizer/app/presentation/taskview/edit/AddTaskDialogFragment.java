package edu.ucsd.cse110.habitizer.app.presentation.taskview.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class AddTaskDialogFragment extends DialogFragment {

    private @NonNull FragmentDialogAddTaskBinding view;
    private MainViewModel model;

    AddTaskDialogFragment() {

    }
    public static AddTaskDialogFragment newInstance() {
        var frag = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
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
        this.view = FragmentDialogAddTaskBinding.inflate(getLayoutInflater());
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.task_dialog_title)
                .setMessage(R.string.task_dialog_message)
                .setView(view.getRoot())
                .setPositiveButton(getString(R.string.task_dialog_add), this::onAddClick)
                .setNegativeButton(getString(R.string.task_dialog_cancel), this::onCancelClick)
                .create();
    }

    private void onAddClick(DialogInterface dialog, int which) {
        String taskName = view.inputTaskNameEditText.getText().toString();

        model.getRoutine().addTask(new Task(taskName));

        dialog.dismiss();
    }
    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
