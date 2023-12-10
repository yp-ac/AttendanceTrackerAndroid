package com.example.microproject;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class QRCodeScanner {
    GmsBarcodeScannerOptions options;
    GmsBarcodeScanner scanner;

    QRCodeScanner(Context context) {
         options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .allowManualInput()
                .build();

         scanner = GmsBarcodeScanning.getClient(context, options);
    }

    void startScan(View view, Callback callback) {
        Log.i("scanner", view.toString());
        scanner.startScan()
            .addOnSuccessListener(barcode -> {
                // Task completed successfully
                Log.i("scanner", barcode.getRawValue());
                callback.callbackFunction(barcode.getRawValue());
            })
            .addOnCanceledListener(() -> {
                Snackbar.make(view, "Scan Cancelled", Snackbar.LENGTH_LONG)
                        .show();
                Log.i("scanner", "cancelled");
            })
            .addOnFailureListener(e -> {
                Snackbar.make(view, "Failed to Read QR Code", Snackbar.LENGTH_LONG)
                        .show();
                Log.e("scanner", "Scan failed");
            });
    }

    interface Callback {
        void callbackFunction(String rawValue);
    }
}
