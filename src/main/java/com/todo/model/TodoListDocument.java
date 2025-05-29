package com.todo.model;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document
public class TodoListDocument extends BaseDocument {

    @Field("userId")
    private String userId;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("items")
    private List<TodoItem> items;

    public TodoListDocument() {
        super("todolist");
        this.items = new ArrayList<>();
    }

    public TodoListDocument(String userId, String title, String description) {
        this();
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<TodoItem> getItems() {
        return items;
    }

    public void setItems(List<TodoItem> items) {
        this.items = items;
    }
}
