package com.example.microproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

// intent inside recycler view
// https://stackoverflow.com/questions/48001762/passing-intent-with-onclick-of-recyclerview

public class SectionRecyclerAdapter extends RecyclerView.Adapter<SectionRecyclerAdapter.SectionViewHolder> {
    private ArrayList<SectionData> courseDataArrayList;
    private Context mcontext;
    public static final String EXTRA_SECTION = "com.example.microproject.sectionrecycleradapter.section";
    public static final String EXTRA_SEM = "com.example.microproject.sectionrecycleradapter.sem";

    public SectionRecyclerAdapter(ArrayList<SectionData> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_card_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        // Set the data to textview and imageview.
        SectionData recyclerData = courseDataArrayList.get(position);
        holder.sectionLabel.setText(recyclerData.getSectionName());
        holder.semLabel.setText(recyclerData.getSectionSem());
        holder.markButton.setOnClickListener(view -> {
            Intent intent = new Intent(mcontext, AttendanceMarker.class);
            intent.putExtra(EXTRA_SECTION, recyclerData.getSectionName());
            intent.putExtra(EXTRA_SEM, recyclerData.getSectionSem());

            mcontext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class SectionViewHolder extends RecyclerView.ViewHolder {

        private TextView sectionLabel, semLabel;
        private MaterialButton markButton;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionLabel = itemView.findViewById(R.id.sectionLabel);
            semLabel = itemView.findViewById(R.id.semLabel);
            markButton = itemView.findViewById(R.id.markButton);
        }
    }
}
