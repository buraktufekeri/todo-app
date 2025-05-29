package com.todo.service.impl;

import com.todo.dto.todoList.*;
import com.todo.dto.todoList.common.TodoList;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.TodoListMapper;
import com.todo.model.TodoListDocument;
import com.todo.repository.TodoListRepository;
import com.todo.security.util.SecurityUtil;
import com.todo.service.TodoListService;
import com.todo.validator.TodoListValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListServiceImpl implements TodoListService {

    private static final String TODO_LIST_DOCUMENT_PREFIX = "todolist::";

    private final TodoListRepository todoListRepository;
    private final TodoListValidator todoListValidator;
    private final TodoListMapper todoListMapper;

    public TodoListServiceImpl(TodoListRepository todoListRepository, TodoListValidator todoListValidator, TodoListMapper todoListMapper) {
        this.todoListRepository = todoListRepository;
        this.todoListValidator = todoListValidator;
        this.todoListMapper = todoListMapper;
    }

    @Override
    public CreateTodoListResponse createTodoList(CreateTodoListRequest createTodoListRequest) {
        String currentUserId = SecurityUtil.getCurrentUserId();

        TodoListDocument existingTodoListDocument = todoListRepository.findByTitle(createTodoListRequest.title());
        todoListValidator.validateTodoListDocument(existingTodoListDocument);

        TodoListDocument todoListDocument = todoListMapper.toTodoListDocument(createTodoListRequest, currentUserId);
        TodoListDocument savedTodoListDocument = todoListRepository.save(todoListDocument);

        TodoList todoList = todoListMapper.toTodoList(savedTodoListDocument);

        return new CreateTodoListResponse(todoList);
    }

    @Override
    public GetTodoListResponse getTodoListById(String id) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedId = TODO_LIST_DOCUMENT_PREFIX + id;

        TodoListDocument todoListDocument = getTodoListDocumentById(prefixedId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "Todo list not found with id:" + prefixedId);

        TodoList todoList = todoListMapper.toTodoList(todoListDocument);

        return new GetTodoListResponse(todoList);
    }

    private TodoListDocument getTodoListDocumentById(String prefixedId) {
        return todoListRepository.findById(prefixedId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo list not found with todoListId:" + prefixedId));
    }

    @Override
    public AllTodoListResponse getAllTodoListByUser() {
        String currentUserId = SecurityUtil.getCurrentUserId();

        List<TodoListDocument> todoListDocuments = todoListRepository.findAllByUserId(currentUserId);
        List<TodoList> todoLists = todoListMapper.toTodoLists(todoListDocuments);

        return new AllTodoListResponse(todoLists);
    }

    @Override
    public UpdateTodoListResponse updateTodoList(String id, UpdateTodoListRequest updateTodoListRequest) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedId = TODO_LIST_DOCUMENT_PREFIX + id;

        TodoListDocument todoListDocument = getTodoListDocumentById(prefixedId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "You cannot update this todo list, id:" + prefixedId);

        boolean updated = updateTodoListDocument(todoListDocument, updateTodoListRequest);
        if (updated) {
            TodoListDocument updatedTodoListDocument = todoListRepository.save(todoListDocument);
            TodoList todoList = todoListMapper.toTodoList(updatedTodoListDocument);
            return new UpdateTodoListResponse(todoList);
        }

        TodoList todoList = todoListMapper.toTodoList(todoListDocument);

        return new UpdateTodoListResponse(todoList);
    }

    private boolean updateTodoListDocument(TodoListDocument todoListDocument, UpdateTodoListRequest updateTodoListRequest) {
        boolean updated = false;

        if (updateTodoListRequest.title() != null && !updateTodoListRequest.title().isBlank()) {
            todoListDocument.setTitle(updateTodoListRequest.title());
            updated = true;
        }
        if (updateTodoListRequest.description() != null && !updateTodoListRequest.description().isBlank()) {
            todoListDocument.setDescription(updateTodoListRequest.description());
            updated = true;
        }

        return updated;
    }

    @Override
    public void deleteTodoList(String id) {
        String currentUserId = SecurityUtil.getCurrentUserId();
        String prefixedId = TODO_LIST_DOCUMENT_PREFIX + id;

        TodoListDocument todoListDocument = getTodoListDocumentById(prefixedId);
        todoListValidator.validateUserId(currentUserId, todoListDocument.getUserId(), "You cannot delete this todo list, id:" + prefixedId);

        todoListRepository.deleteById(prefixedId);
    }

    @Override
    public AllTodoListResponse getAllTodoList() {
        List<TodoListDocument> todoListDocuments = todoListRepository.findAll();
        List<TodoList> todoLists = todoListMapper.toTodoLists(todoListDocuments);

        return new AllTodoListResponse(todoLists);
    }
}
