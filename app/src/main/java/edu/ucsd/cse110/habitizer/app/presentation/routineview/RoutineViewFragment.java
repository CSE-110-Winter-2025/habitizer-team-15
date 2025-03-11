package edu.ucsd.cse110.habitizer.app.presentation.routineview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.util.Log;

import edu.ucsd.cse110.habitizer.app.R;
import edu.ucsd.cse110.habitizer.app.databinding.FragmentRoutineViewBinding;
import edu.ucsd.cse110.habitizer.app.presentation.MainViewModel;
import edu.ucsd.cse110.habitizer.app.presentation.ui.RoutineViewAdapter;
import edu.ucsd.cse110.habitizer.lib.data.DataRoutine;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableNotifiableSubject;
import edu.ucsd.cse110.observables.Observer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineViewFragment extends Fragment {

    private FragmentRoutineViewBinding view;
    private MainViewModel model;
    private RoutineViewAdapter adapter;
    private MutableNotifiableSubject<List<DataRoutine>> allDataRoutines;
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

        //this.allDataRoutines = model.getDataRoutines(); // DUPLICATING HAPPENS HERE

        this.allDataRoutines = model.getDataRoutineManager().getDataRoutineSubject();

        //Log.i("ROUTINE 1", allDataRoutines.getValue().get(0).toString());
        //Log.i("ROUTINE 2", allDataRoutines.getValue().get(1).toString());

        this.adapter = new RoutineViewAdapter(requireContext(), getParentFragmentManager(), model, allDataRoutines.getValue());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = FragmentRoutineViewBinding.inflate(inflater, container, false);
        view.routineListView.setAdapter(this.adapter);
        setupMvpHooks();
        return view.getRoot();
    }

    private void setupMvpHooks() {

        allDataRoutines.observe(newRoutines -> {
            if (newRoutines == null) return;
            adapter.notifyDataSetChanged();
        });

        view.addRoutines.setOnClickListener(v -> {
            var frag = AddRoutineDialogFragment.newInstance();
            frag.show(getParentFragmentManager(), "AddRoutineDialogFragment");
        });
    }


}