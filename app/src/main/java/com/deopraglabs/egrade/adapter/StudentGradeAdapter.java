package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.util.List;

public class StudentGradeAdapter extends RecyclerView.Adapter<StudentGradeAdapter.StudentGradeViewHolder> {

    private Context context;
    private List<Student> studentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    public StudentGradeAdapter(Context context, List<Student> studentList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.studentList = studentList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StudentGradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_grade, parent, false);
        return new StudentGradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentGradeViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.nameTextView.setText(student.getName());
        holder.cpfTextView.setText(EGradeUtil.formatCpf(student.getCpf()));
        holder.emailTextView.setText(student.getEmail());
        holder.phoneTextView.setText(EGradeUtil.formatNumber(student.getPhoneNumber()));
        holder.birthDateTextView.setText(EGradeUtil.dateToString(student.getBirthDate()));
        holder.courseTextView.setText(student.getCourse().getName());
        holder.activeTextView.setText(student.isActive() ? "Ativo" : "Inativo");
        if (student.getProfilePicture() != null) {
            holder.profileImageView.setImageBitmap(EGradeUtil.base64ToBitmap(student.getProfilePicture()));
        } else {
            holder.profileImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile_placeholder));
        }
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentGradeViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cpfTextView, emailTextView, phoneTextView, birthDateTextView, courseTextView, activeTextView;
        ImageView profileImageView;
        ImageButton editButton;

        public StudentGradeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cpfTextView = itemView.findViewById(R.id.cpfTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            birthDateTextView = itemView.findViewById(R.id.birthDateTextView);
            courseTextView = itemView.findViewById(R.id.courseTextView);
            activeTextView = itemView.findViewById(R.id.activeTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
