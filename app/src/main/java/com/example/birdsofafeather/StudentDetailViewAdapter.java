package com.example.birdsofafeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.Course;

import java.util.List;
import java.util.function.Consumer;

public class StudentDetailViewAdapter extends RecyclerView.Adapter<StudentDetailViewAdapter.ViewHolder>{
    private final List<Course> courses;

    public StudentDetailViewAdapter(List<Course> courses) {
        super();
        this.courses = courses;
    }

    @NonNull
    @Override
    public StudentDetailViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_detail_row, parent, false);
        return new StudentDetailViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDetailViewAdapter.ViewHolder holder, int position) {
        holder.setCourse(courses.get(position));
    }

    @Override
    public int getItemCount() { return this.courses.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameView;
        private Course course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseNameView = itemView.findViewById(R.id.student_detail_row_text);
        }

        public void setCourse(Course course) {
            this.course = course;
            this.courseNameView.setText(course.getCourseInfoText());
        }
    }
}
