package com.example.microproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class ProfileBottomSheetFragment extends BottomSheetDialogFragment {
    private GmsBarcodeScanner scanner;
    private QRCodeScanner qrCodeScanner;
    MaterialButton syncButton, logOutButton, scanQrButton;
    TextView userName, userDesignation, serverUrl;
    ImageView profile;
    SharedPreferences sharedPreferences;
    public ProfileBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = view.getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        scanQrButton = view.findViewById(R.id.qrSheetButton);
        logOutButton = view.findViewById(R.id.logoutButton);
        syncButton = view.findViewById(R.id.syncButton);

        userName = view.findViewById(R.id.userName);
        userDesignation = view.findViewById(R.id.userDesignation);
        serverUrl = view.findViewById(R.id.serverUrl);

        profile = view.findViewById(R.id.modalProfile);
        String profileUrl = sharedPreferences.getString("PROFILE_URL", "");

        userName.setText(sharedPreferences.getString("NAME", ""));
        userDesignation.setText(sharedPreferences.getString("DESIGNATION", ""));

        qrCodeScanner = new QRCodeScanner(view.getContext());

        Glide.with(view)
            .load(profileUrl)
            .placeholder(R.drawable.profile_loading)
            .error(R.drawable.blue)
            .fallback(R.drawable.blue)
            .transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
            .into(profile);

        scanQrButton.setOnClickListener(v -> {
            qrCodeScanner.startScan(v, rawData -> {});
        });

        logOutButton.setOnClickListener(v -> {
            logout();
            getActivity().finish();
        });
    }

    void logout() {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("USERNAME", "");
        sharedPreferencesEditor.putString("PROFILE_URL", "");
        sharedPreferencesEditor.putString("DESIGNATION", "");
        sharedPreferencesEditor.apply();
        // TODO: Clear App Data
    }
}