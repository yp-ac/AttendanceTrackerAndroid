package com.example.microproject;

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
    private GmsBarcodeScannerOptions options;
    MaterialButton syncButton, logOutButton, scanQrButton;
    TextView userName, userDesignation, serverUrl;
    ImageView profile;
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

        scanQrButton = view.findViewById(R.id.qrSheetButton);
        profile = view.findViewById(R.id.modalProfile);
        String profileUrl = "https://online.gppune.ac.in/gpp_s20/student/photo/2106206.png";

        options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .allowManualInput()
                .build();

        scanner = GmsBarcodeScanning.getClient(view.getContext(), options);

        Glide.with(view)
                .load(profileUrl)
                .placeholder(R.drawable.profile_loading)
                .error(R.drawable.blue)
                .fallback(R.drawable.blue)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(20)))
                .into(profile);

        scanQrButton.setOnClickListener(v -> {
            scanner.startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                // Task completed successfully
                                Log.i("scanner", barcode.getRawValue());
                            })
                    .addOnCanceledListener(
                            () -> {
                                Snackbar.make(
                                                v,
                                                "Scan Cancelled",
                                                Snackbar.LENGTH_LONG
                                        )
                                        .show();
                                Log.i("scanner", "cancelled");
                            })
                    .addOnFailureListener(
                            e -> {
                                Snackbar.make(
                                                v,
                                                "Failed to Read QR Code",
                                                Snackbar.LENGTH_LONG
                                        )
                                        .show();
                                Log.e("scanner", "Scan failed");
                            });
        });
    }
}