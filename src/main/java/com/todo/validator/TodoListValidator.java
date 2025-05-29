package com.todo.validator;

import com.todo.exception.ConflictException;
import com.todo.exception.ForbiddenOperationException;
import com.todo.model.TodoListDocument;
import org.springframework.stereotype.Component;

@Component
public class TodoListValidator {

    public void validateTodoListDocument(TodoListDocument todoListDocument) {
        if (todoListDocument != null)
            throw new ConflictException("TodoListDocument must be unique, title:" + todoListDocument.getTitle());
    }

    public void validateUserId(String currentUserId, String entityUserId, String message) {
        if (!currentUserId.equals(entityUserId))
            throw new ForbiddenOperationException(message);
    }
}
