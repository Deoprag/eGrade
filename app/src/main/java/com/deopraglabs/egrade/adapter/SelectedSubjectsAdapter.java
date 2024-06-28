package com.deopraglabs.egrade.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Subject;

import java.util.List;

public class SelectedSubjectsAdapter extends RecyclerView.Adapter<SelectedSubjectsAdapter.SubjectViewHolder> {

    private final List<Subject> subjects;
    private final OnSubjectRemoveListener onSubjectRemoveListener;

    public SelectedSubjectsAdapter(List<Subject> subjects, OnSubjectRemoveListener onSubjectRemoveListener) {
        this.subjects = subjects;
        this.onSubjectRemoveListener = onSubjectRemoveListener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjects.get(position);
        holder.subjectNameTextView.setText(subject.getName());
        holder.removeButton.setOnClickListener(v -> onSubjectRemoveListener.onSubjectRemove(subject));
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;
        Button removeButton;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public interface OnSubjectRemoveListener {
        void onSubjectRemove(Subject subject);
    }
}