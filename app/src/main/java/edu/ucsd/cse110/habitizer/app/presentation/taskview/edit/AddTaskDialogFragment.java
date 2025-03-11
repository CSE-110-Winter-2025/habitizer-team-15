package edu.ucsd.cse110.habitizer.app.presentation.taskview.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogAddTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.util.SimplifiedTextWatcher;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.task_dialog_title)
                .setMessage(R.string.task_dialog_message)
                .setView(view.getRoot())
                .setPositiveButton(getString(R.string.task_dialog_add), this::onAddClick)
                .setNegativeButton(getString(R.string.dialog_cancel), this::onCancelClick);

        AlertDialog alertDialog = builder.create();

        // We can't reference alertDialog until we've actually created it
        view.inputTaskNameEditText.addTextChangedListener(new SimplifiedTextWatcher() {
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
        String addedTaskName = view.inputTaskNameEditText.getText().toString();
        if (addedTaskName.isEmpty())
            return;
        Task task = new Task(addedTaskName);
        if (view.taskBottomBtn.isChecked())
            model.getActiveRoutine().addTask(task);
        else
            model.getActiveRoutine().addTask(0, task);
        dialog.dismiss();
    }
    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
