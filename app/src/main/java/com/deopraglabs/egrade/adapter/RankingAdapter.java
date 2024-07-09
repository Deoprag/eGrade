package com.deopraglabs.egrade.adapter;// RankingAdapter.java

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Method;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;
import com.deopraglabs.egrade.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private List<Grade> gradesList;

    public RankingAdapter(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Grade grade = gradesList.get(position);
        final String url = EGradeUtil.URL + "/api/v1/student/findByGrade/" + grade.getId();
        HttpUtil.sendRequest(url, Method.GET, "", new HttpUtil.HttpRequestListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Resposta", response);
                Gson gson = new Gson();
                Student student = gson.fromJson(response, Student.class);

                holder.itemView.post(() -> {
                    holder.positionTextView.setText(String.valueOf(holder.getAdapterPosition() + 1));
                    holder.nameTextView.setText(student.getName());
                    holder.gradeTextView.setText(String.format("%.1f",(grade.getN1() + grade.getN2()) / 2));

                    switch (holder.getAdapterPosition()) {
                        case 0:
                            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.gold));
                            break;
                        case 1:
                            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.silver));
                            break;
                        case 2:
                            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.bronze));
                            break;
                        default:
                            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
                            break;
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
    public int getItemCount() {
        return gradesList.size();
    }

    public void updateGrades(List<Grade> newGradesList) {
        gradesList = newGradesList;
        notifyDataSetChanged();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView positionTextView;
        TextView nameTextView;
        TextView gradeTextView;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            gradeTextView = itemView.findViewById(R.id.gradeTextView);
        }
    }
}
