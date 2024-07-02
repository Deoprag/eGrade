package com.deopraglabs.egrade.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.adapter.CoordinatorAdapter;
import com.deopraglabs.egrade.model.Coordinator;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.util.DataHolder;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorCoordinatorsActivity extends AppCompatActivity {

    private Coordinator coordinator;
    private RecyclerView recyclerView;
    private CoordinatorAdapter adapter;
    private List<Coordinator> coordinatorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_coordinators);

        coordinator = DataHolder.getInstance().getCoordinator();
        coordinatorList = new ArrayList<>();

        adapter = new CoordinatorAdapter(this, coordinatorList, coordinatorEdit -> {
            Intent intent = new Intent(CoordinatorCoordinatorsActivity.this, EditCoordinatorActivity.class);
            DataHolder.getInstance().setCoordinatorEdit(coordinatorEdit);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewCoordinators);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button addCoordinatorButton = findViewById(R.id.addCoordinatorButton);
        addCoordinatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorCoordinatorsActivity.this, EditCoordinatorActivity.class);
            DataHolder.getInstance().setCoordinatorEdit(null);
            DataHolder.getInstance().setCoordinator(coordinator);
            startActivity(intent);
        });

        loadCoordinators();
    }

    private void loadCoordinators() {
        final String url = EGradeUtil.URL + "/api/v1/coordinator/findAll";

        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Type coordinatorListType = new TypeToken<List<Coordinator>>() {}.getType();
                List<Coordinator> coordinators = gson.fromJson(response, coordinatorListType);

                runOnUiThread(() -> {
                    if (coordinators != null) {
                        coordinatorList.clear();
                        coordinatorList.addAll(coordinators);
                        coordinatorList.remove(coordinator);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Erro", "Lista de coordenadores retornada é nula");
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("Erro", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadCoordinators();
        }
    }
}
