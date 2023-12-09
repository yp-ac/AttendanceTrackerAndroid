package com.example.microproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalTime;

// Tabs
// https://youtu.be/ziJ6-AT3ymg?si=06JBFqyTqVrN8ta8

public class AttendanceMarker extends AppCompatActivity {
    MaterialToolbar topAppBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    MenuItem doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_marker);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Intent intent = getIntent();

        topAppBar = findViewById(R.id.topAppBar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        doneButton = topAppBar.getMenu().findItem(R.id.attendanceDone);

        String title = intent.getStringExtra(SectionRecyclerAdapter.EXTRA_SECTION) + " " + intent.getStringExtra(SectionRecyclerAdapter.EXTRA_SEM);
        topAppBar.setTitle(title);

        topAppBar.setNavigationOnClickListener(view -> {
            finish();
        });

        AttendanceVPAdapter avpAdapter = new AttendanceVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        avpAdapter.addFragment(new ConfigurationFragment(), "Configuration");
        avpAdapter.addFragment(new AttendanceFragment(), "Attendance");

        viewPager.setAdapter(avpAdapter);

        doneButton.setOnMenuItemClickListener(menuItem -> {
            StringBuilder sb = new StringBuilder("Do you want to commit attendance of ");
            String batch = sharedPreferences.getString("BATCH", "Z");
            sb.append(intent.getStringExtra(SectionRecyclerAdapter.EXTRA_SECTION))
                    .append(" ")
                    .append(intent.getStringExtra(SectionRecyclerAdapter.EXTRA_SEM))
                    .append((batch.equals("")) ? "" : (" " + batch + " batch"))
                    .append(" on ")
                    .append(sharedPreferences.getString("DATE", ""))
                    .append(" from ")
                    .append(ConfigurationFragment.timeFormatter.format(LocalTime.of(
                            sharedPreferences.getInt("HOUR", 0),
                            sharedPreferences.getInt("MINUTE", 0))))
                    .append(" to ")
                    .append(ConfigurationFragment.timeFormatter.format(LocalTime.of(
                            sharedPreferences.getInt("HOUR", 0) + sharedPreferences.getInt("DURATION", 0),
                            sharedPreferences.getInt("MINUTE", 0))));

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Mark Attendance")
                    .setMessage(sb.toString())
                    .setNeutralButton("wait", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("mark", (dialog, which) -> {

                    })
                    .show();
            return false;
        });
    }
}