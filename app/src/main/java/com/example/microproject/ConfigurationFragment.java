package com.example.microproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.text.MessageFormat;


// Get Parent fragment manager
// https://stackoverflow.com/questions/20237531/how-can-i-access-getsupportfragmentmanager-in-a-fragment

public class ConfigurationFragment extends Fragment {
    MaterialButtonToggleGroup durationToggleGroup;
    ChipGroup batchToggleGroup;
    TextInputEditText dateField, timeField, commentsField;
    TextInputLayout timeLayout;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
    private int durationOfLecture = 1, hour, minute = 0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        batchToggleGroup = view.findViewById(R.id.batchChipGroup);

        durationToggleGroup = view.findViewById(R.id.hourGroup);
        durationToggleGroup.check(R.id.oneHourLecture);

        dateField = view.findViewById(R.id.dateField);
        timeField = view.findViewById(R.id.timeField);
        timeLayout = view.findViewById(R.id.timeLayout);

        commentsField = view.findViewById(R.id.commentsFieldInput);

        configureBatchToggleGroup(view);
        configureDurationToggleGroup();
        configureTimeField();
        configureDateField();
        configureCommentField();

        hour = LocalTime.now().getHour();

        timeField.setText(LocalTime.of(hour, 0).format(timeFormatter));
        timeLayout.setHelperText(MessageFormat.format("Lecture Ends at {0}",
                LocalTime.of(hour + durationOfLecture, minute).format(timeFormatter)));

        dateField.setText(dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));

        sharedPreferencesEditor.putString("COMMENT", "");
        sharedPreferencesEditor.putString("BATCH", "");
        sharedPreferencesEditor.putString("DATE", dateFormat.format(MaterialDatePicker.todayInUtcMilliseconds()));
        sharedPreferencesEditor.putInt("HOUR", hour);
        sharedPreferencesEditor.putInt("MINUTE", minute);
        sharedPreferencesEditor.putInt("DURATION", durationOfLecture);
        sharedPreferencesEditor.apply();
    }

    void configureCommentField() {
        commentsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sharedPreferencesEditor.putString("COMMENT", charSequence.toString());
                sharedPreferencesEditor.apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    void configureDateField() {
        dateField.setOnClickListener(v -> {
            MaterialDatePicker picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .build();

            picker.addOnPositiveButtonClickListener(v2 -> {
                dateField.setText(dateFormat.format(picker.getSelection()));
                sharedPreferencesEditor.putString("DATE", dateFormat.format(picker.getSelection()));
                sharedPreferencesEditor.apply();
            });

            picker.show(getParentFragmentManager(), "DatePicker");
        });
    }
    void configureTimeField() {
        timeField.setOnClickListener(v -> {
            MaterialTimePicker timePicker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setMinute(0)
                            .setHour(LocalTime.now().getHour())
                            .setTitleText("Select Lecture Time")
                            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                            .build();

            timePicker.addOnPositiveButtonClickListener(v2 -> {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                LocalTime t = LocalTime.of(hour, minute);
                timeField.setText(t.format(timeFormatter));

                timeLayout.setHelperText(MessageFormat.format("Lecture Ends at {0}",
                        LocalTime.of(hour + durationOfLecture, minute).format(timeFormatter)));

                sharedPreferencesEditor.putInt("HOUR", hour);
                sharedPreferencesEditor.putInt("MINUTE", minute);
                sharedPreferencesEditor.apply();
            });

            timePicker.show(getParentFragmentManager(), "TimePicker");
        });
    }
    void configureDurationToggleGroup() {
        durationToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.oneHourLecture) {
                    durationOfLecture = 1;
                } else {
                    durationOfLecture = 2;
                }

                sharedPreferencesEditor.putInt("DURATION", durationOfLecture);
                sharedPreferencesEditor.apply();
                timeLayout.setHelperText(MessageFormat.format("Lecture Ends at {0}",
                        LocalTime.of(hour + durationOfLecture, minute).format(timeFormatter)));
            }
        });
    }
    void configureBatchToggleGroup(View view) {
        batchToggleGroup.setOnCheckedStateChangeListener((chipGroup, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                sharedPreferencesEditor.putString("BATCH", "");
                sharedPreferencesEditor.apply();
            } else {
                Chip chip = view.findViewById(checkedIds.get(0));

                durationToggleGroup.check(R.id.twoHourLecture);

                sharedPreferencesEditor.putString("BATCH", chip.getText().toString());
                sharedPreferencesEditor.apply();
            }
        });
    }
}