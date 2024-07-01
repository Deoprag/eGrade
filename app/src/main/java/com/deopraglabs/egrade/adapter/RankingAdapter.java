package com.deopraglabs.egrade.adapter;// RankingAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Grade;

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
        holder.positionTextView.setText(String.valueOf(position + 1));
        holder.nameTextView.setText(grade.getStudent().getName());
        holder.gradeTextView.setText(String.valueOf(grade.getN1()));
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
