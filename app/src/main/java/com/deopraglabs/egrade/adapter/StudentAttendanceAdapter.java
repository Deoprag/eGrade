package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Student;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private Set<Student> selectedStudents;

    public StudentAttendanceAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.selectedStudents = new HashSet<>();
    }

    public Set<Student> getSelectedStudents() {
        return selectedStudents;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_attendance, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.nameTextView.setText(student.getName());
        holder.attendanceCheckBox.setChecked(selectedStudents.contains(student));
        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedStudents.add(student);
            } else {
                selectedStudents.remove(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        CheckBox attendanceCheckBox;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            attendanceCheckBox = itemView.findViewById(R.id.attendanceCheckBox);
        }
    }
}
