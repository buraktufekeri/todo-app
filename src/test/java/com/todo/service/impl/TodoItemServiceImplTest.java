package com.todo.service.impl;

import com.todo.dto.todoItem.*;
import com.todo.dto.todoItem.common.TodoItemDto;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.TodoItemMapper;
import com.todo.model.TodoItem;
import com.todo.model.TodoListDocument;
import com.todo.repository.TodoListRepository;
import com.todo.security.util.SecurityUtil;
import com.todo.validator.TodoItemValidator;
import com.todo.validator.TodoListValidator;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TodoItemServiceImpl Unit Tests")
class TodoItemServiceImplTest {

    private TodoListRepository todoListRepository;
    private TodoListValidator todoListValidator;
    private TodoItemValidator todoItemValidator;
    private TodoItemMapper todoItemMapper;
    private TodoItemServiceImpl todoItemService;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    private static final String USER_ID = "user-123";
    private static final String TODO_LIST_ID = "list-1";
    private static final String PREFIXED_TODO_LIST_ID = "todolist::" + TODO_LIST_ID;
    private static final String TODO_ITEM_ID = "item-1";

    @BeforeEach
    void setUp() {
        todoListRepository = mock(TodoListRepository.class);
        todoListValidator = mock(TodoListValidator.class);
        todoItemValidator = mock(TodoItemValidator.class);
        todoItemMapper = mock(TodoItemMapper.class);
        todoItemService = new TodoItemServiceImpl(todoListRepository, todoListValidator, todoItemValidator, todoItemMapper);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserId).thenReturn(USER_ID);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void shouldAddTodoItemSuccessfully() {
        CreateTodoItemRequest request = new CreateTodoItemRequest("title", "desc", Instant.now());
        TodoListDocument document = createTodoListDocumentWithItems(new ArrayList<>());

        when(todoListRepository.findById(PREFIXED_TODO_LIST_ID)).thenReturn(Optional.of(document));
        doNothing().when(todoListValidator).validateUserId(any(), any(), any());
        doNothing().when(todoItemValidator).validateTodoItemTitle(document, request.title());

        TodoItem createdItem = new TodoItem(request.title(), request.description(), request.dueDate());
        document.getItems().add(createdItem);

        TodoItemDto dto = createTodoItemDto(createdItem, false);
        when(todoItemMapper.toTodoItemDto(any())).thenReturn(dto);
        when(todoListRepository.save(document)).thenReturn(document);

        CreateTodoItemResponse response = todoItemService.addItemToTodoList(TODO_LIST_ID, request);

        assertNotNull(response);
        assertEquals(dto, response.todoItemDto());
        verify(todoListRepository).save(document);
    }

    @Test
    void shouldThrowExceptionWhenTodoListNotFound() {
        CreateTodoItemRequest request = new CreateTodoItemRequest("title", "desc", Instant.now());
        when(todoListRepository.findById(PREFIXED_TODO_LIST_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                todoItemService.addItemToTodoList(TODO_LIST_ID, request));
    }

    @Test
    void shouldGetTodoItemByIdSuccessfully() {
        TodoItem item = createTodoItem(TODO_ITEM_ID);
        TodoListDocument document = createTodoListDocumentWithItems(List.of(item));
        TodoItemDto dto = createTodoItemDto(item, false);

        when(todoListRepository.findById(PREFIXED_TODO_LIST_ID)).thenReturn(Optional.of(document));
        doNothing().when(todoListValidator).validateUserId(any(), any(), any());
        when(todoItemMapper.toTodoItemDto(item)).thenReturn(dto);

        GetTodoItemResponse response = todoItemService.getTodoItemById(TODO_LIST_ID, TODO_ITEM_ID);

        assertEquals(dto, response.todoItemDto());
    }

    @Test
    void shouldUpdateTodoItemSuccessfully() {
        TodoItem item = createTodoItem(TODO_ITEM_ID);
        TodoListDocument document = createTodoListDocumentWithItems(List.of(item));

        UpdateTodoItemRequest request = new UpdateTodoItemRequest("new title", "new desc", true, Instant.now());
        TodoItemDto dto = new TodoItemDto(TODO_ITEM_ID, request.title(), request.description(), true, request.dueDate(), Instant.now());

        when(todoListRepository.findById(PREFIXED_TODO_LIST_ID)).thenReturn(Optional.of(document));
        doNothing().when(todoListValidator).validateUserId(any(), any(), any());
        when(todoListRepository.save(document)).thenReturn(document);
        when(todoItemMapper.toTodoItemDto(any())).thenReturn(dto);

        UpdateTodoItemResponse response = todoItemService.updateTodoItem(TODO_LIST_ID, TODO_ITEM_ID, request);

        assertEquals("new title", response.todoItemDto().title());
        assertTrue(response.todoItemDto().completed());
    }

    @Test
    void shouldDeleteTodoItemSuccessfully() {
        TodoItem item = createTodoItem(TODO_ITEM_ID);
        List<TodoItem> items = new ArrayList<>(List.of(item));
        TodoListDocument document = createTodoListDocumentWithItems(items);

        when(todoListRepository.findById(PREFIXED_TODO_LIST_ID)).thenReturn(Optional.of(document));
        doNothing().when(todoListValidator).validateUserId(any(), any(), any());
        doNothing().when(todoItemValidator).validateTodoItemId(document, TODO_ITEM_ID);
        when(todoListRepository.save(document)).thenReturn(document);

        todoItemService.deleteTodoItem(TODO_LIST_ID, TODO_ITEM_ID);

        assertTrue(document.getItems().isEmpty());
        verify(todoListRepository).save(document);
    }

    private TodoListDocument createTodoListDocumentWithItems(List<TodoItem> items) {
        TodoListDocument document = new TodoListDocument();
        document.setId(PREFIXED_TODO_LIST_ID);
        document.setUserId(USER_ID);
        document.setItems(items);
        return document;
    }

    private TodoItem createTodoItem(String id) {
        TodoItem item = new TodoItem("title", "desc", Instant.now());
        item.setId(id);
        return item;
    }

    private TodoItemDto createTodoItemDto(TodoItem item, boolean completed) {
        return new TodoItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                completed,
                item.getDueDate(),
                Instant.now()
        );
    }
}