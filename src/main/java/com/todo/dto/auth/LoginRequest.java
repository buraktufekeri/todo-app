package com.todo.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}
