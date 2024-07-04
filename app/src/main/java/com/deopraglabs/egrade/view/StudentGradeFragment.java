package com.deopraglabs.egrade.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.GradeAdapter;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.DataHolder;

public class StudentGradeFragment extends Fragment {

    private Student student;
    private RecyclerView gradeRecyclerView;
    private GradeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        student = DataHolder.getInstance().getStudent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_grade, container, false);

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

        gradeRecyclerView = view.findViewById(R.id.gradeRecyclerView);
        gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GradeAdapter(student.getGrades());
        DataHolder.getInstance().setStudent(student);
        gradeRecyclerView.setAdapter(adapter);

        return view;
    }
}
