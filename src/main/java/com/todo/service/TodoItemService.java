package com.todo.service;

import com.todo.dto.todoItem.*;

public interface TodoItemService {

    CreateTodoItemResponse addItemToTodoList(String todoListId, CreateTodoItemRequest createTodoItemRequest);

    GetTodoItemResponse getTodoItemById(String todoListId, String id);

    UpdateTodoItemResponse updateTodoItem(String todoListId, String id, UpdateTodoItemRequest updateTodoItemRequest);

    void deleteTodoItem(String todoListId, String id);
}
