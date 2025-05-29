package com.todo.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class TodoItem implements Serializable {

    @Serial
    private static final long serialVersionUID = -307447781126133120L;

    private String id;
    private String title;
    private String description;
    private boolean completed;
    private Instant dueDate;
    private Instant createdAt;

    public TodoItem() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }

    public TodoItem(String title, String description, Instant dueDate) {
        this();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}