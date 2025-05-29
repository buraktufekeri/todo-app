package com.todo.controller;

import com.todo.dto.todoItem.*;
import com.todo.service.TodoItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/todo-item")
@Tag(name = "TodoItemController", description = "User todo-item processes")
public class TodoItemController {

    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping("/add-item/{todoListId}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('CREATE_TODO')")
    public ResponseEntity<CreateTodoItemResponse> addItemToTodoList(@PathVariable @NotBlank String todoListId,
                                                                    @RequestBody @Valid CreateTodoItemRequest createTodoItemRequest) {
        return ResponseEntity.ok(todoItemService.addItemToTodoList(todoListId, createTodoItemRequest));
    }

    @GetMapping("/get-item/{todoListId}/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('READ_OWN_TODO')")
    public ResponseEntity<GetTodoItemResponse> getTodoItemById(@PathVariable @NotBlank String todoListId,
                                                               @PathVariable @NotBlank String id) {
        return ResponseEntity.ok(todoItemService.getTodoItemById(todoListId, id));
    }

    @PutMapping("/update-item/{todoListId}/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('UPDATE_OWN_TODO')")
    public ResponseEntity<UpdateTodoItemResponse> updateTodoItem(@PathVariable @NotBlank String todoListId,
                                                                 @PathVariable @NotBlank String id,
                                                                 @RequestBody @Valid UpdateTodoItemRequest updateTodoItemRequest) {
        return ResponseEntity.ok(todoItemService.updateTodoItem(todoListId, id, updateTodoItemRequest));
    }

    @DeleteMapping("/delete-item/{todoListId}/{id}")
    @PreAuthorize("@customMethodSecurityExpression.hasActions('DELETE_OWN_TODO')")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable @NotBlank String todoListId, @PathVariable @NotBlank String id) {
        todoItemService.deleteTodoItem(todoListId, id);
        return ResponseEntity.noContent().build();
    }
}
