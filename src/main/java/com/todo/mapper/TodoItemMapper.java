package com.todo.mapper;

import com.todo.dto.todoItem.common.TodoItemDto;
import com.todo.model.TodoItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoItemMapper {

    TodoItemDto toTodoItemDto(TodoItem todoItem);
}
