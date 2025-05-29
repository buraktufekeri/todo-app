package com.todo.dto.todoList.common;

import com.todo.model.TodoItem;

import java.time.Instant;
import java.util.List;

public record TodoList(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String userId,
        String title,
        String description,
        List<TodoItem> items
) {
}
