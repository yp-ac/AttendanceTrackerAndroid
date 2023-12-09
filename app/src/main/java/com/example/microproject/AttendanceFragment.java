package com.example.microproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<StudentData> recyclerStudentData;
    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.attendanceRV);

        recyclerStudentData = new ArrayList<>();

        recyclerStudentData.add(new StudentData("Yash Pravin Pawar", "png", 2106206));
        recyclerStudentData.add(new StudentData("Mayuresh Tukaram Muluk", "jpg", 2106134));
        recyclerStudentData.add(new StudentData("Anjali Sheshrao Waghmare", "jpg", 2106204));
        recyclerStudentData.add(new StudentData("Kunal Dhanaji Khairnar", "jpg", 2106099));
        recyclerStudentData.add(new StudentData("Yash Satish Thakare", "jpeg", 2106208));
        recyclerStudentData.add(new StudentData("Vaishnavi Sanjiv Patil", "", 2106150));
        recyclerStudentData.add(new StudentData("Vedant Pramod Kale", "jpg", 2106090));
        recyclerStudentData.add(new StudentData("Varun Bipin Jagtap", "jpg", 2106082));

        StudentRecyclerAdapter adapter = new StudentRecyclerAdapter(recyclerStudentData, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}