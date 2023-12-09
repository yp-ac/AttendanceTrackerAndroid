package com.example.microproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

// Google ML Kit -- BarCode Scanning
// https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner#java

public class MainActivity extends AppCompatActivity {
    private MaterialButton scanQrButton, loginButton;
    private TextInputEditText usernameField, passwordField;
    private TextView qrStatusView;
    private GmsBarcodeScanner scanner;
    private GmsBarcodeScannerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .allowManualInput()
                .build();

        scanner = GmsBarcodeScanning.getClient(this, options);

        scanQrButton = findViewById(R.id.qrButton);
        qrStatusView = findViewById(R.id.qrStatus);

        loginButton = findViewById(R.id.loginButton);

        scanQrButton.setOnClickListener(view -> {
            scanner.startScan()
                    .addOnSuccessListener(
                            barcode -> {
                                // Task completed successfully
                                Log.i("scanner", barcode.getRawValue());
                                scanQrButton.setIconResource(R.drawable.baseline_check_24);
                                qrStatusView.setText("Scan Successful");
                                System.out.println("secondary = " + R.color.md_theme_dark_secondary + " btn = " + scanQrButton.getSolidColor());
                            })
                    .addOnCanceledListener(
                            () -> {
                                Snackbar.make(
                                            findViewById(R.id.mainActivity),
                                            "Scan Cancelled",
                                            Snackbar.LENGTH_LONG
                                        )
                                        .show();
                                Log.i("scanner", "cancelled");
                            })
                    .addOnFailureListener(
                            e -> {
                                Snackbar.make(
                                            findViewById(R.id.mainActivity),
                                            "Failed to Read QR Code",
                                            Snackbar.LENGTH_LONG
                                        )
                                        .show();
                                Log.e("scanner", "Scan failed");
                            });
        });

        loginButton.setOnClickListener(view -> {
            Intent homePage = new Intent(this, HomePage.class);
            startActivity(homePage);
        });
    }
}