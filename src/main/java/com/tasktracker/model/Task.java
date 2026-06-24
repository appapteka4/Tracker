package com.tasktracker.model;

public class Task {

    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignee;
    private String created_at;

    public Task() {}

    public Task(String title, String description, String status,
                String priority, String assignee) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignee = assignee;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusLabel() {
        return switch (status) {
            case "in_progress" -> "В работе";
            case "done"        -> "Готово";
            default            -> "Новая";
        };
    }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getPriorityLabel() {
        return switch (priority) {
            case "medium" -> "Средний";
            case "high"   -> "Высокий";
            default       -> "Низкий";
        };
    }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    @Override
    public String toString() { return title; }
}
