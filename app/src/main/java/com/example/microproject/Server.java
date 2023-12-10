package com.example.microproject;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

// Google GSON
// https://github.com/google/gson
// https://www.baeldung.com/okhttp-json-response

// OKHTTP3
// https://www.baeldung.com/guide-to-okhttp

public class Server {
    static String BASE_URL = "https://e195-144-48-177-162.ngrok-free.app";
    final static OkHttpClient client = new OkHttpClient();
    final static Gson gson = new Gson();
}
