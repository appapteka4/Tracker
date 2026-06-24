package com.tasktracker.model;

public class Comment {
    private int id;
    private int task_id;
    private String author;
    private String body;
    private String created_at;

    public int getId() { return id; }
    public int getTask_id() { return task_id; }
    public String getAuthor() { return author; }
    public String getBody() { return body; }
    public String getCreated_at() { return created_at; }
    public void setTask_id(int v) { task_id = v; }
    public void setAuthor(String v) { author = v; }
    public void setBody(String v) { body = v; }
}