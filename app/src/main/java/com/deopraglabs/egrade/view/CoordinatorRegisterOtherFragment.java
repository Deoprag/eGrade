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
import com.deopraglabs.egrade.util.DataHolder;

public class CoordinatorRegisterOtherFragment extends Fragment {

    private Coordinator coordinator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        coordinator = DataHolder.getInstance().getCoordinator();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_coordinator_register_other, container, false);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                view.setBackgroundColor(getResources().getColor(R.color.background_dark));
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            default:
                view.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                break;
        }

        CardView cardViewMateria = view.findViewById(R.id.cardViewMateria);
        cardViewMateria.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CoordinatorSubjectsActivity.class);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
        });

        CardView cardViewCurso = view.findViewById(R.id.cardViewCurso);
        cardViewCurso.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CoordinatorCourseActivity.class);
                DataHolder.getInstance().setCoordinator(coordinator);
                startActivity(intent);
        });

        return view;
    }
}
