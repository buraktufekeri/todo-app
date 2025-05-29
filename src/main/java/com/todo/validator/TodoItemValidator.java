package com.todo.validator;

import com.todo.exception.ConflictException;
import com.todo.exception.ResourceNotFoundException;
import com.todo.model.TodoListDocument;
import org.springframework.stereotype.Component;

@Component
public class TodoItemValidator {

    public void validateTodoItemTitle(TodoListDocument todoListDocument, String title) {
        boolean exists = todoListDocument.getItems().stream()
                .anyMatch(item -> title.equalsIgnoreCase(item.getTitle()));
        if (exists)
            throw new ConflictException("An item with the same title already exists in this todo list, title:" + title);
    }

    public void validateTodoItemId(TodoListDocument todoListDocument, String id) {
        boolean exists = todoListDocument.getItems().stream()
                .anyMatch(item -> id.equals(item.getId()));
        if (!exists)
            throw new ResourceNotFoundException("Todo item not found with id:" + id);
    }
}
