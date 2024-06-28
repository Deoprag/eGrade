package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context context;
    private List<Subject> subjectList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }

    public SubjectAdapter(Context context, List<Subject> subjectList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.subjectList = subjectList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

        holder.nameTextView.setText(subject.getName());
        holder.professorTextView.setText(subject.getProfessor().getName());
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(subject));
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, professorTextView;
        Button editButton;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            professorTextView = itemView.findViewById(R.id.professorTextView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}

