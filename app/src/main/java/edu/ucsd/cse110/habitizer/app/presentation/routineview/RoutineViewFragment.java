package edu.ucsd.cse110.habitizer.app.presentation.routineview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import android.util.Log;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.taskview.TaskViewFragment;
import edu.ucsd.cse110.habitizer.app.presentation.ui.RoutineViewAdapter;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataRoutineManager;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineViewFragment extends Fragment {

    private FragmentRoutineViewBinding view;
    private MainViewModel model;
    private RoutineViewAdapter adapter;
    private List<DataRoutine> allDataRoutines;
    public static RoutineViewFragment newInstance() {
        RoutineViewFragment fragment = new RoutineViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.model = MainViewModel.getSingletonModel(getActivity());

        this.allDataRoutines = model.getDataRoutines(); // DUPLICATING HAPPENS HERE

        Log.i("ROUTINE 1", allDataRoutines.get(0).toString());
        Log.i("ROUTINE 2", allDataRoutines.get(1).toString());

        this.adapter = new RoutineViewAdapter(requireContext(), getParentFragmentManager(), this.allDataRoutines,
                integer -> model.setActiveRoutine(allDataRoutines.get(integer)));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentRoutineViewBinding.inflate(inflater, container, false);
        view.routineListView.setAdapter(this.adapter);
//        setupMvpHooks();
        return view.getRoot();
    }

//    private void setupMvpHooks() {
//        view.startMorning.setOnClickListener(view1 -> {
//            // TODO: Call this depending on what the user picks from
//            //  the list given by DataRoutineManager!
//
//            model.setActiveRoutine(InMemoryDataRoutineManager.DATA_MORNING_ROUTINE);
//            getParentFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance())
//                    .commit();
//        });
//        view.editMorning.setOnClickListener(view1 -> {
//            model.setActiveRoutine(InMemoryDataRoutineManager.DATA_MORNING_ROUTINE);
//            getParentFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_activity_fragment_container, TaskViewFragment.newInstance(true))
//                    .commit();
//        });
//    }
}