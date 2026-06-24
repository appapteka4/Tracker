package com.tasktracker.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tasktracker.model.Comment;
import com.tasktracker.util.HttpClient;
import com.tasktracker.util.SupabaseConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class CommentService {

    private static final String URL  = SupabaseConfig.REST_URL + "/comments";
    private static final Gson   gson = new Gson();

    public List<Comment> getByTask(int taskId) {
        try {
            String json = HttpClient.get(URL + "?task_id=eq." + taskId + "&order=id.asc");
            Type t = new TypeToken<List<Comment>>() {}.getType();
            List<Comment> list = gson.fromJson(json, t);
            return list != null ? list : Collections.emptyList();
        } catch (IOException e) { e.printStackTrace(); return Collections.emptyList(); }
    }

    public boolean add(int taskId, String author, String body) {
        try {
            String json = String.format(
                    "{\"task_id\":%d,\"author\":\"%s\",\"body\":\"%s\"}",
                    taskId, author, body.replace("\"", "\\\""));
            String resp = HttpClient.post(URL, json);
            return !resp.contains("error");
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
}