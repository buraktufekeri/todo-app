package com.todo.dto.todoItem.common;

import java.time.Instant;

public record TodoItemDto(
        String id,
        String title,
        String description,
        boolean completed,
        Instant dueDate,
        Instant createdAt
) {
}
