package com.todo.dto.todoItem;

import com.todo.dto.todoItem.common.TodoItemDto;

public record CreateTodoItemResponse(
        TodoItemDto todoItemDto
) {
}
