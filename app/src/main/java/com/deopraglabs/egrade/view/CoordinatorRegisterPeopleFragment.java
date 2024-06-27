package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Coordinator;

public class CoordinatorRegisterPeopleFragment extends Fragment {

    private Coordinator coordinator;

    public static CoordinatorRegisterPeopleFragment newInstance(Coordinator coordinator) {
        CoordinatorRegisterPeopleFragment fragment = new CoordinatorRegisterPeopleFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", coordinator);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coordinator = (Coordinator) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_coordinator_register_people, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        // Setting click listeners for each card
        CardView cardViewCoordenador = view.findViewById(R.id.cardViewCoordenador);
        cardViewCoordenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), activity_edit_coordinador.class);
//                intent.putExtra("user", coordinator);
//                startActivity(intent);
            }
        });

        CardView cardViewProfessor = view.findViewById(R.id.cardViewProfessor);
        cardViewProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfessorActivity.class);
                intent.putExtra("coordinator", coordinator);
                startActivity(intent);
            }
        });

        CardView cardViewAluno = view.findViewById(R.id.cardViewAluno);
        cardViewAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CoordinatorStudentsActivity.class);
                intent.putExtra("coordinator", coordinator);
                startActivity(intent);
            }
        });

        return view;
    }
}
