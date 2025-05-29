package com.todo.dto;

import com.todo.model.RoleDocument;

import java.util.Set;

public record UserDto(
        String id,
        String username,
        String password,
        String email,
        Set<RoleDocument> roles
) {
}
