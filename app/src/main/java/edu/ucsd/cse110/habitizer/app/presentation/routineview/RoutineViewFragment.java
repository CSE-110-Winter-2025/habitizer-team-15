package edu.ucsd.cse110.habitizer.app.presentation.routineview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineViewFragment extends Fragment {

    private FragmentRoutineViewBinding view;
    public static RoutineViewFragment newInstance() {
        RoutineViewFragment fragment = new RoutineViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentRoutineViewBinding.inflate(inflater, container, false);
        setupMvpHooks();
        return view.getRoot();
    }

    private void setupMvpHooks() {
        view.startMorning.setOnClickListener(view1 -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance())
                    .commit();
        });
        view.editMorning.setOnClickListener(view1 -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true))
                    .commit();
        });
    }
}