package com.todo.dto.todoItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateTodoItemRequest(
        @NotNull(message = "Title cannot be null")
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,
        @Size(max = 300, message = "Description must be at most 300 characters")
        String description,
        @NotNull(message = "dueDate cannot be null")
        Instant dueDate
) {
}
