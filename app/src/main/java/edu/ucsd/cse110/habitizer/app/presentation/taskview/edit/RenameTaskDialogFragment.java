package edu.ucsd.cse110.habitizer.app.presentation.taskview.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentDialogRenameTaskBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.util.SimplifiedTextWatcher;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class RenameTaskDialogFragment extends DialogFragment {
    private @NonNull FragmentDialogRenameTaskBinding view;
    private MainViewModel model;
    public Task task;

    RenameTaskDialogFragment() {

    }

    public static RenameTaskDialogFragment newInstance() {
        var frag = new RenameTaskDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static RenameTaskDialogFragment newInstance(Task task) {
        var frag = RenameTaskDialogFragment.newInstance();
        frag.task = task;
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
        this.view = FragmentDialogRenameTaskBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setTitle(R.string.rename_task)
            .setMessage(R.string.enter_new_task_name)
            .setView(view.getRoot())
            .setPositiveButton(R.string.rename, this::onRenameClick)
            .setNegativeButton(R.string.dialog_cancel, this::onCancelClick);

        AlertDialog alertDialog = builder.create();

        view.inputRenameTaskEditText.addTextChangedListener(new SimplifiedTextWatcher() {
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


    private void onRenameClick(DialogInterface dialog, int which) {
        String renamedTaskName = view.inputRenameTaskEditText.getText().toString();
        if (renamedTaskName.isEmpty()) {
            return;
        }
        task.setName(renamedTaskName);
        dialog.dismiss();
    }

    private void onCancelClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}
