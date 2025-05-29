package com.todo.controller;

import com.todo.dto.todoList.*;
import com.todo.service.TodoListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/todo-list")
@Tag(name = "TodoListController", description = "User todo-list processes")
public class TodoListController {

    private final TodoListService todoListService;

    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/create")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('CREATE_TODO')")
    public ResponseEntity<CreateTodoListResponse> createTodoList(@RequestBody @Valid CreateTodoListRequest createTodoListRequest) {
        return ResponseEntity.ok(todoListService.createTodoList(createTodoListRequest));
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('READ_OWN_TODO')")
    public ResponseEntity<GetTodoListResponse> getTodoListById(@PathVariable @NotBlank String id) {
        return ResponseEntity.ok(todoListService.getTodoListById(id));
    }

    @GetMapping("/all/by-user")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('READ_OWN_TODO')")
    public ResponseEntity<AllTodoListResponse> getAllTodoListByUser() {
        return ResponseEntity.ok(todoListService.getAllTodoListByUser());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('UPDATE_OWN_TODO')")
    public ResponseEntity<UpdateTodoListResponse> updateTodoList(@PathVariable @NotBlank String id,
                                                                 @RequestBody @Valid UpdateTodoListRequest updateTodoListRequest) {
        return ResponseEntity.ok(todoListService.updateTodoList(id, updateTodoListRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('DELETE_OWN_TODO')")
    public ResponseEntity<Void> deleteTodoList(@PathVariable @NotBlank String id) {
        todoListService.deleteTodoList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('READ_ALL_TODOS')")
    public ResponseEntity<AllTodoListResponse> getAllTodoList() {
        return ResponseEntity.ok(todoListService.getAllTodoList());
    }
}
