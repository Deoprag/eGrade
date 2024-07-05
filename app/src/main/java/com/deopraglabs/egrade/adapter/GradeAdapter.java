package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Attendance;
import com.deopraglabs.egrade.model.Grade;
import com.deopraglabs.egrade.model.Student;
import com.deopraglabs.egrade.util.DataHolder;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    Context context;
    private List<Grade> gradeList;

    private Student student;

    public GradeAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        student = DataHolder.getInstance().getStudent();
        if (student != null) {
            float media = (grade.getN1() + grade.getN2()) / 2;
            float faltas = 0f;
            float presenca = 0f;

            if (student.getAttendances() != null && !student.getAttendances().isEmpty()) {
                for (Attendance attendance: student.getAttendances()) {
                    if (attendance.getSubject().equals(grade.getSubject())) {
                        if (attendance.isPresent()) {
                            presenca++;
                        } else {
                            faltas++;
                        }
                    }
                }
            }

            float totalAulas = presenca + faltas;
            float porcentagemPresenca = (totalAulas > 0) ? (presenca / totalAulas) * 100 : 100;

            boolean aprovado = media > 6f && porcentagemPresenca > 75f;

            holder.textViewSubject.setText(grade.getSubject().getName());
            holder.textViewProfessor.setText("Professor: " + grade.getSubject().getProfessor().getName());
            holder.textViewAbsence.setText("Presença: " + String.format("%.1f", porcentagemPresenca) + "%");
            holder.textViewGrade.setText(String.format("Nota Final: %.1f", media));
            holder.textViewGradeN1.setText(String.format("Nota 1: %.1f", grade.getN1()));
            holder.textViewGradeN2.setText(String.format("Nota 2: %.1f", grade.getN2()));
            holder.textViewSituation.setText(aprovado ? "Aprovado" : "Reprovado");
            holder.textViewSituation.setTextColor(aprovado
                    ? ContextCompat.getColor(holder.cardView.getContext(), R.color.holo_green)
                    : ContextCompat.getColor(holder.cardView.getContext(), R.color.holo_red_dark));
        }
    }


    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    public static class GradeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject, textViewProfessor, textViewAbsence, textViewGrade, textViewGradeN1, textViewGradeN2, textViewSituation;
        CardView cardView;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewProfessor = itemView.findViewById(R.id.textViewProfessor);
            textViewAbsence = itemView.findViewById(R.id.textViewAbsence);
            textViewGradeN1 = itemView.findViewById(R.id.textViewGradeN1);
            textViewGradeN2 = itemView.findViewById(R.id.textViewGradeN2);
            textViewGrade = itemView.findViewById(R.id.textViewGrade);
            textViewSituation = itemView.findViewById(R.id.textViewSituation);
        }
    }
}
