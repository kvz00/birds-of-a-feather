package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.model.db.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder>{
    private final List<Student> students;

    public StudentViewAdapter(List<Student> students) {
        super();
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setStudent(students.get(position));
    }

    @Override
    public int getItemCount() {
        return this.students.size();
    }

    public void addStudent(Student student) {
        this.students.add(student);
        this.notifyItemInserted(this.students.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView studentNameView;
        private Student student;
        private final ImageView studentPictureView;
        private final ImageView wavePictureView;
        private final ImageView favoritePictureView;

        ViewHolder(View itemView) {
            super(itemView);
            this.studentNameView = itemView.findViewById(R.id.student_row_name);
            this.studentPictureView = itemView.findViewById(R.id.profile_picture);
            this.wavePictureView = itemView.findViewById(R.id.wave_picture);
            this.favoritePictureView = itemView.findViewById(R.id.favorite_picture);
            itemView.setOnClickListener(this);
        }

        public void setStudent(Student student) {
            this.student = student;
            this.studentNameView.setText(student.getStudentName());
            if (student.isWaved() == 0) {
                this.studentPictureView.setImageResource(R.drawable.a416016766695jxj6ttwhn);
            } else {
                this.studentPictureView.setImageResource(R.drawable.a416016766695jxj6ttwhnyellow);
            }
            if (student.isFavorited()) {
                this.favoritePictureView.setImageResource(R.drawable.downloadyellow);
            } else {
                this.favoritePictureView.setImageResource(R.drawable.download);
            }
            if (student.getStudentImage() != null) {
                Picasso.get().load(student.getStudentImage()).into(this.studentPictureView);
            } else {
                this.studentPictureView.setImageResource(R.drawable.test);
            }
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra("student_id", this.student.getStudentId());
            context.startActivity(intent);
        }

        public void favorite() {

        }
    }
}
