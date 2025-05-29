package com.todo.dto.auth;

import com.todo.model.RoleDocument;

import java.util.Set;

public record LoginResponse(
        String id,
        String username,
        String email,
        Set<RoleDocument> roles,
        String token
) {
}
