package com.tasktracker.model;

public class TeamMember {
    private int id;
    private String full_name;
    private String role;
    private String email;
    private String created_at;

    public int getId() { return id; }
    public String getFull_name() { return full_name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public void setFull_name(String v) { full_name = v; }
    public void setRole(String v) { role = v; }
    public void setEmail(String v) { email = v; }
}