package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Professor;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ProfessorViewHolder> {

    private Context context;
    private List<Professor> professorList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Professor professor);
    }

    public ProfessorAdapter(Context context, List<Professor> professorList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.professorList = professorList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProfessorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_professor, parent, false);
        return new ProfessorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessorViewHolder holder, int position) {
        Professor professor = professorList.get(position);

        holder.nameTextView.setText(professor.getName());
        holder.cpfTextView.setText(EGradeUtil.formatCpf(professor.getCpf()));
        holder.emailTextView.setText(professor.getEmail());
        holder.phoneTextView.setText(EGradeUtil.formatNumber(professor.getPhoneNumber()));
        holder.birthDateTextView.setText(EGradeUtil.dateToString(professor.getBirthDate()));
        holder.activeTextView.setText(professor.isActive() ? "Ativo" : "Inativo");
        if (professor.getProfilePicture() != null) {
            holder.profileImageView.setImageBitmap(EGradeUtil.convertImageFromByte(professor.getProfilePicture().getBytes()));
        }
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(professor));
    }

    @Override
    public int getItemCount() {
        return professorList.size();
    }

    public static class ProfessorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cpfTextView, emailTextView, phoneTextView, birthDateTextView, activeTextView;
        ImageView profileImageView;
        Button editButton;

        public ProfessorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cpfTextView = itemView.findViewById(R.id.cpfTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            birthDateTextView = itemView.findViewById(R.id.birthDateTextView);
            activeTextView = itemView.findViewById(R.id.activeTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
