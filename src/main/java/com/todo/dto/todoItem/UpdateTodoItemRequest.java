package com.todo.dto.todoItem;

import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateTodoItemRequest(
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,
        @Size(max = 300, message = "Description must be at most 300 characters")
        String description,
        boolean completed,
        Instant dueDate
) {
}
