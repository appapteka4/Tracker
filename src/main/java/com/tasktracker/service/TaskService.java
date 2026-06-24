package com.tasktracker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tasktracker.model.Task;
import com.tasktracker.util.HttpClient;
import com.tasktracker.util.SupabaseConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class TaskService {

    private static final String TABLE_URL = SupabaseConfig.REST_URL + "/tasks";
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    public List<Task> getAllTasks() {
        try {
            String json = HttpClient.get(TABLE_URL + "?order=id.desc");
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> tasks = gson.fromJson(json, listType);
            return tasks != null ? tasks : Collections.emptyList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Task> getTasksByStatus(String status) {
        try {
            String url = TABLE_URL + "?status=eq." + status + "&order=id.desc";
            String json = HttpClient.get(url);
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> tasks = gson.fromJson(json, listType);
            return tasks != null ? tasks : Collections.emptyList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean createTask(Task task) {
        try {
            String body = gson.toJson(new TaskPayload(
                    task.getTitle(), task.getDescription(),
                    task.getStatus(), task.getPriority(), task.getAssignee()
            ));
            String response = HttpClient.post(TABLE_URL, body);
            return !response.contains("error");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int taskId, String newStatus) {
        try {
            String url = TABLE_URL + "?id=eq." + taskId;
            String body = gson.toJson(new StatusPayload(newStatus));
            String response = HttpClient.patch(url, body);
            return !response.contains("error");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTask(int taskId) {
        try {
            String url = TABLE_URL + "?id=eq." + taskId;
            HttpClient.delete(url);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static class TaskPayload {
        String title, description, status, priority, assignee;
        TaskPayload(String t, String d, String s, String p, String a) {
            title = t; description = d; status = s; priority = p; assignee = a;
        }
    }

    private static class StatusPayload {
        String status;
        StatusPayload(String s) { status = s; }
    }
}
