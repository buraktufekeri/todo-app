package com.todo.service.impl;

import com.todo.dto.todoItem.*;
import com.todo.dto.todoItem.common.TodoItemDto;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.TodoItemMapper;
import com.todo.model.TodoItem;
import com.todo.model.TodoListDocument;
import com.todo.repository.TodoListRepository;
import com.todo.security.util.SecurityUtil;
import com.todo.service.TodoItemService;
import com.todo.validator.TodoItemValidator;
import com.todo.validator.TodoListValidator;
import org.springframework.stereotype.Service;

@Service
public class TodoItemServiceImpl implements TodoItemService {

    private static final String TODO_LIST_DOCUMENT_PREFIX = "todolist::";

    private final TodoListRepository todoListRepository;
    private final TodoListValidator todoListValidator;
    private final TodoItemValidator todoItemValidator;
    private final TodoItemMapper todoItemMapper;

    public TodoItemServiceImpl(TodoListRepository todoListRepository, TodoListValidator todoListValidator, TodoItemValidator todoItemValidator, TodoItemMapper todoItemMapper) {
        this.todoListRepository = todoListRepository;
        this.todoListValidator = todoListValidator;
        this.todoItemValidator = todoItemValidator;
        this.todoItemMapper = todoItemMapper;
    }

    @Override
    public CreateTodoItemResponse addItemToTodoList(String todoListId, CreateTodoItemRequest createTodoItemRequest) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedTodoListId = TODO_LIST_DOCUMENT_PREFIX + todoListId;

        TodoListDocument todoListDocument = getTodoListDocumentByTodoListId(prefixedTodoListId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "You cannot add items to this todo list, todoListId:" + prefixedTodoListId);
        todoItemValidator.validateTodoItemTitle(todoListDocument, createTodoItemRequest.title());

        TodoItem todoItem = new TodoItem(createTodoItemRequest.title(), createTodoItemRequest.description(), createTodoItemRequest.dueDate());
        todoListDocument.getItems().add(todoItem);
        todoListRepository.save(todoListDocument);

        TodoItemDto todoItemDto = todoItemMapper.toTodoItemDto(todoItem);

        return new CreateTodoItemResponse(todoItemDto);
    }

    private TodoListDocument getTodoListDocumentByTodoListId(String prefixedTodoListId) {
        return todoListRepository.findById(prefixedTodoListId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo list not found with todoListId:" + prefixedTodoListId));
    }

    @Override
    public GetTodoItemResponse getTodoItemById(String todoListId, String id) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedTodoListId = TODO_LIST_DOCUMENT_PREFIX + todoListId;

        TodoListDocument todoListDocument = getTodoListDocumentByTodoListId(prefixedTodoListId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "Todo list not found with todoListId:" + prefixedTodoListId);

        TodoItem todoItem = getTodoItemFromTodoListDocument(todoListDocument, id);
        TodoItemDto todoItemDto = todoItemMapper.toTodoItemDto(todoItem);

        return new GetTodoItemResponse(todoItemDto);
    }

    private TodoItem getTodoItemFromTodoListDocument(TodoListDocument todoListDocument, String todoItemId) {
        return todoListDocument.getItems().stream()
                .filter(item -> todoItemId.equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Todo item not found with id:" + todoItemId));
    }

    @Override
    public UpdateTodoItemResponse updateTodoItem(String todoListId, String id, UpdateTodoItemRequest updateTodoItemRequest) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedTodoListId = TODO_LIST_DOCUMENT_PREFIX + todoListId;

        TodoListDocument todoListDocument = getTodoListDocumentByTodoListId(prefixedTodoListId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "You cannot update items in this todo list, todoListId:" + prefixedTodoListId);

        TodoItem todoItem = updateTodoItem(todoListDocument, id, updateTodoItemRequest);
        TodoItemDto todoItemDto = todoItemMapper.toTodoItemDto(todoItem);

        return new UpdateTodoItemResponse(todoItemDto);
    }

    private TodoItem updateTodoItem(TodoListDocument todoListDocument, String todoItemId, UpdateTodoItemRequest updateTodoItemRequest) {
        TodoItem todoItem = getTodoItemFromTodoListDocument(todoListDocument, todoItemId);

        if (updateTodoItemRequest.title() != null && !updateTodoItemRequest.title().isBlank())
            todoItem.setTitle(updateTodoItemRequest.title());
        if (updateTodoItemRequest.description() != null && !updateTodoItemRequest.description().isBlank())
            todoItem.setDescription(updateTodoItemRequest.description());
        if (updateTodoItemRequest.dueDate() != null)
            todoItem.setDueDate(updateTodoItemRequest.dueDate());

        todoItem.setCompleted(updateTodoItemRequest.completed());

        todoListRepository.save(todoListDocument);

        return todoItem;
    }

    @Override
    public void deleteTodoItem(String todoListId, String id) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedTodoListId = TODO_LIST_DOCUMENT_PREFIX + todoListId;

        TodoListDocument todoListDocument = getTodoListDocumentByTodoListId(prefixedTodoListId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "You cannot delete items in this todo list, todoListId:" + prefixedTodoListId);
        todoItemValidator.validateTodoItemId(todoListDocument, id);

        todoListDocument.getItems().removeIf(item -> id.equals(item.getId()));
        todoListRepository.save(todoListDocument);
    }
}
