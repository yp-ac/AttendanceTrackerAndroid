package com.example.microproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

// Google ML Kit -- BarCode Scanning
// https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner#java

// Internet Connection Check
// https://blog.devgenius.io/how-to-check-the-internet-connection-in-an-android-project-4371aafa59ca

// Clear Activity
// https://riptutorial.com/android/example/2736/clearing-an-activity-stack

// Loading dialog
// https://www.youtube.com/watch?v=tccoRIrMyhU

public class MainActivity extends AppCompatActivity {
    private MaterialButton scanQrButton, loginButton;
    private TextInputEditText usernameField, passwordField;
    private TextView qrStatusView;
    private QRCodeScanner qrCodeScanner;
    SharedPreferences sharedPreferences;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUIElements();

        qrCodeScanner = new QRCodeScanner(this);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (!sharedPreferences.getString("USERNAME", "").equals("")) {
            startHomeActivity();
        }

        scanQrButton.setOnClickListener(view -> {
            qrCodeScanner.startScan(findViewById(R.id.mainActivity), rawValue -> {
                scanQrButton.setIconResource(R.drawable.baseline_check_24);
                qrStatusView.setText("Scan Successful");
            });
        });

        loginButton.setOnClickListener(view -> {
            if (!isNetworkAvailable()) {
                dialogOK("Not Connected To Internet", "Internet connection is required while logging in the application, please connect to internet");
                return;
            }

            loadingDialog = new MaterialAlertDialogBuilder(this)
                    .setMessage("Loading...")
                    .setCancelable(false)
                    .create();
            loadingDialog.show();

            login(usernameField.getText().toString(), passwordField.getText().toString());
        });
    }
    boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    void getUIElements() {
        scanQrButton = findViewById(R.id.qrButton);
        qrStatusView = findViewById(R.id.qrStatus);

        usernameField = findViewById(R.id.usernameFieldInput);
        passwordField = findViewById(R.id.passwordFieldInput);

        loginButton = findViewById(R.id.loginButton);
    }
    void startHomeActivity() {
        Intent homePage = new Intent(this, HomePage.class);
        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homePage);
    }
    void login(String username, String password) {
        username = username.trim();
        password = password.trim();

        if (username.equals("") || password.equals("")) {
            dialogOK("Fill Data", "Please fill the username and the password");
            return;
        }

        Request request = new Request.Builder()
                .url(Server.BASE_URL + "/users/" + username + "/" + password)
                .build();

        Server.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> {
                    loadingDialog.dismiss();

                    switch(response.code()) {
                        case 404:
                            dialogOK("Invalid Username", "Please Enter a valid username");
                            return;
                        case 401:
                            dialogOK("Invalid Password", "Please enter a valid password");
                            return;
                        case 200:
                            updateSharedPreferences(getUserData(response));
                            startHomeActivity();
                            return;
                        default:
                            dialogOK("Something Went Wrong", "Try again, response code: " + response.code());
                    }
                });
            }
        });
    }
    UserResponse getUserData(Response response) {
        try {
            return Server.gson.fromJson(response.body().string(), UserResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void updateSharedPreferences(UserResponse userData) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putString("USERNAME", userData.username);
        sharedPreferencesEditor.putString("NAME", userData.name);
        sharedPreferencesEditor.putString("PROFILE_URL", userData.imgUrl);
        sharedPreferencesEditor.putString("DESIGNATION", userData.designation);
        sharedPreferencesEditor.apply();
    }

    void dialogOK(String title, String message) {
        new MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("ok", (dialog, which) -> {})
            .show();
    }
}