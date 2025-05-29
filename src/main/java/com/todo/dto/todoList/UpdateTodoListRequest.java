package com.todo.dto.todoList;

import jakarta.validation.constraints.Size;

public record UpdateTodoListRequest(
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,
        @Size(max = 300, message = "Description must be at most 300 characters")
        String description
) {
}
