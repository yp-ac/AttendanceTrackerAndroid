package com.example.microproject;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;

// intent inside recycler view
// https://stackoverflow.com/questions/48001762/passing-intent-with-onclick-of-recyclerview

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.StudentViewHolder> {
    private ArrayList<StudentData> studentDataArrayList;
    private Context mcontext;

    public StudentRecyclerAdapter(ArrayList<StudentData> recyclerDataArrayList, Context mcontext) {
        this.studentDataArrayList = recyclerDataArrayList;
        studentDataArrayList.sort((studentData, t1) -> {
            if (studentData.getEnrollmentNo() > t1.getEnrollmentNo())
                return 1;
            else if (studentData.getEnrollmentNo() == t1.getEnrollmentNo())
                return 0;
            else
                return -1;
        });

        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        // Set the data to textview and imageview.
        StudentData recyclerData = studentDataArrayList.get(position);
        holder.enrollmentNoLabel.setText(String.valueOf(recyclerData.getEnrollmentNo()));
        holder.nameLabel.setText(recyclerData.getName());
        holder.attendanceButton.setChecked(true);
        holder.attendanceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                recyclerData.setPresent(isChecked);
            }
        });
        holder.attendanceButton.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
            recyclerData.setPresent(isChecked);
            Log.i("StudentData", recyclerData.getEnrollmentNo() + " " + recyclerData.isPresent());
        }));

        Glide.with(mcontext)
                .load(recyclerData.getProfileUrl())
                .placeholder(R.drawable.profile_loading)
                .error(R.drawable.blue)
                .fallback(R.drawable.blue)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return studentDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView enrollmentNoLabel, nameLabel;
        private MaterialSwitch attendanceButton;
        private ImageView profilePic;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            enrollmentNoLabel = itemView.findViewById(R.id.enrollmentNo);
            nameLabel = itemView.findViewById(R.id.name);
            attendanceButton = itemView.findViewById(R.id.attendanceBtn);
            profilePic = itemView.findViewById(R.id.studentPic);
        }
    }
}
