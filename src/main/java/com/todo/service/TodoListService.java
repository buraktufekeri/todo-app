package com.todo.service;

import com.todo.dto.todoList.*;

public interface TodoListService {

    CreateTodoListResponse createTodoList(CreateTodoListRequest createTodoListRequest);

    GetTodoListResponse getTodoListById(String id);

    AllTodoListResponse getAllTodoListByUser();

    UpdateTodoListResponse updateTodoList(String id, UpdateTodoListRequest updateTodoListRequest);

    void deleteTodoList(String id);

    AllTodoListResponse getAllTodoList();
}
