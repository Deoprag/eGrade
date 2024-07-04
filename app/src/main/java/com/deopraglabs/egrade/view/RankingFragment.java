package com.deopraglabs.egrade.view;// StudentHomeFragment.java

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.RankingAdapter;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Subject;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RankingFragment extends Fragment {

    private RecyclerView rankingRecyclerView;
    private RankingAdapter rankingAdapter;
    private List<Grade> gradesList = new ArrayList<>();
    private List<Subject> subjectsList = new ArrayList<>();
    private Spinner subjectSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);

        rankingRecyclerView = view.findViewById(R.id.rankingRecyclerView);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);

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

        populateSubjects();

        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = (Subject) parent.getItemAtPosition(position);
                updateRanking(subject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rankingAdapter = new RankingAdapter(new ArrayList<>());
        rankingRecyclerView.setAdapter(rankingAdapter);

        updateRanking(null);

        return view;
    }

    private void populateSubjects() {
        final String url = EGradeUtil.URL + "/api/v1/subject/findAll/";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta Matéria", response);
                Gson gson = new Gson();
                Type subjectListType = new TypeToken<List<Subject>>() {}.getType();
                synchronized (RankingFragment.this) {
                    List<Subject> subjects = gson.fromJson(response, subjectListType);
                    if (subjects != null) {
                        subjectsList.clear();
                        subjectsList.addAll(subjects);
                    } else {
                        Log.e("Erro", "Lista de matérias retornada é nula");
                    }
                    RankingFragment.this.notifyAll();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    private void updateRanking(@Nullable Subject subject) {
        List<Grade> filteredGrades = filterGradesBySubject(subject);
        rankingAdapter.updateGrades(filteredGrades);
    }

    private List<Grade> filterGradesBySubject(@Nullable Subject subject) {
        List<Grade> allGrades = getAllGrades();

        if (subject == null || subject.getName().equals("Todas")) {
            return allGrades.stream()
                    .sorted((g1, g2) -> Float.compare(g2.getN1(), g1.getN1())) // Ordena por nota, por exemplo
                    .limit(5)
                    .collect(Collectors.toList());
        }

        return allGrades.stream()
                .filter(grade -> grade.getSubject().getName().equals(subject.getName()))
                .sorted((g1, g2) -> Float.compare(g2.getN1(), g1.getN1()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<Grade> getAllGrades() {
        final String url = EGradeUtil.URL + "/api/v1/grade/findAll";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Type gradeListType = new TypeToken<List<Grade>>() {}.getType();
                synchronized (RankingFragment.this) {
                    gradesList = gson.fromJson(response, gradeListType);
                    RankingFragment.this.notifyAll();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
        return gradesList;
    }
}
