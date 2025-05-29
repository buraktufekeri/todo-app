package com.todo.dto.user;

import com.todo.model.RoleDocument;

import java.util.Set;

public record RegisterResponse(
        String id,
        String username,
        String email,
        Set<RoleDocument> roles
) {
}
