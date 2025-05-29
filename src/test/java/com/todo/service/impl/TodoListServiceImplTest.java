package com.todo.service.impl;

import com.todo.dto.todoList.*;
import com.todo.dto.todoList.common.TodoList;
import com.todo.exception.ResourceNotFoundException;
import com.todo.mapper.TodoListMapper;
import com.todo.model.TodoListDocument;
import com.todo.repository.TodoListRepository;
import com.todo.security.util.SecurityUtil;
import com.todo.validator.TodoListValidator;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TodoListServiceImplTest Unit Tests")
class TodoListServiceImplTest {

    private TodoListRepository todoListRepository;
    private TodoListValidator todoListValidator;
    private TodoListMapper todoListMapper;
    private TodoListServiceImpl todoListService;
    private static final String USER_ID = "user-123";
    private static final String TODO_LIST_ID = "123";
    private static final String PREFIXED_ID = "todolist::123";
    private static final String TITLE = "my list";
    private static final String DESCRIPTION = "desc";
    private static final String NOT_FOUND_MESSAGE = "Todo list not found with id:" + PREFIXED_ID;
    private MockedStatic<SecurityUtil> mockedSecurityUtil;
    private TodoListDocument todoListDocument;
    private TodoList todoListDto;

    @BeforeEach
    void setUp() {
        todoListRepository = mock(TodoListRepository.class);
        todoListValidator = mock(TodoListValidator.class);
        todoListMapper = mock(TodoListMapper.class);
        todoListService = new TodoListServiceImpl(todoListRepository, todoListValidator, todoListMapper);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
        mockedSecurityUtil.when(SecurityUtil::getCurrentUserId).thenReturn(USER_ID);
        todoListDocument = new TodoListDocument(USER_ID, TITLE, DESCRIPTION);
        todoListDocument.setId(PREFIXED_ID);
        todoListDto = new TodoList(PREFIXED_ID, Instant.now(), Instant.now(), USER_ID, TITLE, DESCRIPTION, null);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void shouldGetTodoListByIdSuccessfully() {
        when(todoListRepository.findById(PREFIXED_ID)).thenReturn(Optional.of(todoListDocument));
        doNothing().when(todoListValidator).validateUserId(USER_ID, USER_ID, NOT_FOUND_MESSAGE);
        when(todoListMapper.toTodoList(todoListDocument)).thenReturn(todoListDto);

        GetTodoListResponse response = todoListService.getTodoListById(TODO_LIST_ID);

        assertNotNull(response);
        assertEquals(todoListDto, response.todoList());

        verify(todoListRepository).findById(PREFIXED_ID);
        verify(todoListValidator).validateUserId(USER_ID, USER_ID, NOT_FOUND_MESSAGE);
        verify(todoListMapper).toTodoList(todoListDocument);
    }

    @Test
    void shouldThrowExceptionWhenTodoListNotFound() {
        when(todoListRepository.findById(PREFIXED_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                todoListService.getTodoListById(TODO_LIST_ID)
        );

        assertTrue(exception.getMessage().contains("Todo list not found with todoListId:" + PREFIXED_ID));
    }

    @Test
    void shouldCreateTodoListSuccessfully() {
        CreateTodoListRequest request = new CreateTodoListRequest(TITLE, DESCRIPTION);
        TodoListDocument newDocument = new TodoListDocument();
        TodoListDocument savedDocument = new TodoListDocument();

        when(todoListRepository.findByTitle(TITLE)).thenReturn(null);
        doNothing().when(todoListValidator).validateTodoListDocument(null);
        when(todoListMapper.toTodoListDocument(request, USER_ID)).thenReturn(newDocument);
        when(todoListRepository.save(newDocument)).thenReturn(savedDocument);
        when(todoListMapper.toTodoList(savedDocument)).thenReturn(todoListDto);

        CreateTodoListResponse response = todoListService.createTodoList(request);

        assertNotNull(response);
        assertEquals(todoListDto, response.todoList());

        verify(todoListRepository).findByTitle(TITLE);
        verify(todoListValidator).validateTodoListDocument(null);
        verify(todoListMapper).toTodoListDocument(request, USER_ID);
        verify(todoListRepository).save(newDocument);
        verify(todoListMapper).toTodoList(savedDocument);
    }

    @Test
    void shouldReturnAllTodoListsByUser() {
        when(todoListRepository.findAllByUserId(USER_ID)).thenReturn(List.of(todoListDocument));
        when(todoListMapper.toTodoLists(List.of(todoListDocument))).thenReturn(List.of(todoListDto));

        AllTodoListResponse response = todoListService.getAllTodoListByUser();

        assertNotNull(response.todoLists());
        assertEquals(todoListDto, response.todoLists().get(0));

        verify(todoListRepository).findAllByUserId(USER_ID);
        verify(todoListMapper).toTodoLists(List.of(todoListDocument));
    }
}