package com.todo.dto.todoList;

import com.todo.dto.todoList.common.TodoList;

import java.util.List;

public record AllTodoListResponse(
        List<TodoList> todoLists
) {
}
