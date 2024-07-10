package com.deopraglabs.egrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deopraglabs.egrade.R;
import com.deopraglabs.egrade.model.Attendance;
import com.deopraglabs.egrade.util.EGradeUtil;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private Context context;
    private List<Attendance> attendanceList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Attendance attendance);
    }

    public AttendanceAdapter(Context context, List<Attendance> attendanceList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.attendanceList = attendanceList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);

        holder.dateTextView.setText(EGradeUtil.dateToString(attendance.getDate()));
        holder.courseTextView.setText(attendance.getSubject().getName());
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(attendance));
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView, courseTextView;
        Button editButton;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            courseTextView = itemView.findViewById(R.id.courseTextView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}