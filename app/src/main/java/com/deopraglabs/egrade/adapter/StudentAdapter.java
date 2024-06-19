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
import com.bumptech.glide.Glide;
import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    public StudentAdapter(Context context, List<Student> studentList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.studentList = studentList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.nameTextView.setText(student.getName());
        holder.cpfTextView.setText(EGradeUtil.formatCpf(student.getCpf()));
        holder.emailTextView.setText(student.getEmail());
        holder.phoneTextView.setText(EGradeUtil.formatNumber(student.getPhoneNumber()));
        holder.birthDateTextView.setText(EGradeUtil.dateToString(student.getBirthDate()));

        holder.courseTextView.setText(student.getCourse().getName());

        holder.activeTextView.setText(student.isActive() ? "Ativo" : "Inativo");

        Glide.with(context)
                .load(student.getProfilePicture())
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(holder.profileImageView);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(student));
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, cpfTextView, emailTextView, phoneTextView, birthDateTextView, courseTextView, activeTextView;
        ImageView profileImageView;
        Button editButton;

        public StudentViewHolder(@NonNull View itemView) {
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
