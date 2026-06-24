package com.tasktracker.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import java.io.IOException;

public class HttpClient {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient();

    private static String accessToken = null;

    public static void setAccessToken(String token) {
        accessToken = token;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String get(String url) throws IOException {
        Request request = buildRequest(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "[]";
        }
    }

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = buildRequest(url)
                .post(body)
                .addHeader("Prefer", "return=representation")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    public static String patch(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = buildRequest(url)
                .patch(body)
                .addHeader("Prefer", "return=representation")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    public static void delete(String url) throws IOException {
        Request request = buildRequest(url).delete().build();
        try (Response response = client.newCall(request).execute()) {
        }
    }

    public static String postAuth(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "{}";
        }
    }

    private static Request.Builder buildRequest(String url) {
        String bearer = (accessToken != null) ? accessToken : SupabaseConfig.ANON_KEY;
        return new Request.Builder()
                .url(url)
                .addHeader("apikey", SupabaseConfig.ANON_KEY)
                .addHeader("Authorization", "Bearer " + bearer)
                .addHeader("Content-Type", "application/json");
    }
}
