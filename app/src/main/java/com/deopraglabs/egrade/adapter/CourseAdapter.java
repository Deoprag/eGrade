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
import com.deopraglabs.egrade.model.Course;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Course> courseList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }

    public CourseAdapter(Context context, List<Course> courseList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.courseList = courseList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);

        holder.nameTextView.setText(course.getName());
        holder.descriptionTextView.setText(course.getDescription());
        holder.coordinatorTextView.setText(course.getCoordinator().getName());
        holder.editButton.setOnClickListener(v -> onItemClickListener.onItemClick(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, descriptionTextView, coordinatorTextView;
        Button editButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            coordinatorTextView = itemView.findViewById(R.id.coordinatorTextView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}