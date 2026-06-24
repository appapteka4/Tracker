package com.tasktracker.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tasktracker.util.HttpClient;
import com.tasktracker.util.SupabaseConfig;

import java.io.IOException;

public class AuthService {

    private static final String SIGN_IN_URL = SupabaseConfig.AUTH_URL + "/token?grant_type=password";
    private static final String SIGN_UP_URL = SupabaseConfig.AUTH_URL + "/signup";

    public String login(String email, String password) {
        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        try {
            String response = HttpClient.postAuth(SIGN_IN_URL, body);
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            if (json.has("access_token")) {
                HttpClient.setAccessToken(json.get("access_token").getAsString());
                return null;
            }
            if (json.has("error_description")) {
                return json.get("error_description").getAsString();
            }
            return "Неверный email или пароль";
        } catch (IOException e) {
            return "Ошибка соединения: " + e.getMessage();
        }
    }

    public String register(String email, String password) {
        String body = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        try {
            String response = HttpClient.postAuth(SIGN_UP_URL, body);
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            if (json.has("id") || json.has("access_token")) {
                return null;
            }
            if (json.has("msg")) {
                return json.get("msg").getAsString();
            }
            return "Ошибка регистрации";
        } catch (IOException e) {
            return "Ошибка соединения: " + e.getMessage();
        }
    }

    public void logout() {
        HttpClient.setAccessToken(null);
    }
}
