package com.todo.mapper;

import com.todo.dto.todoList.CreateTodoListRequest;
import com.todo.dto.todoList.common.TodoList;
import com.todo.model.TodoListDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoListMapper {

    @Mapping(target = "userId", source = "userId")
    TodoListDocument toTodoListDocument(CreateTodoListRequest createTodoListRequest, String userId);

    TodoList toTodoList(TodoListDocument todoListDocument);

    List<TodoList> toTodoLists(List<TodoListDocument> todoListDocuments);
}
