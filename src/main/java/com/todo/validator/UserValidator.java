package com.todo.validator;

import com.todo.exception.ConflictException;
import com.todo.exception.ResourceNotFoundException;
import com.todo.model.RoleDocument;
import com.todo.model.UserDocument;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUserDocument(UserDocument userDocument) {
        if (userDocument != null && userDocument.getUsername() != null)
            throw new ConflictException("Username is already taken, username:" + userDocument.getUsername());
    }

    public void validateRoleDocument(RoleDocument roleDocument) {
        if (roleDocument == null || roleDocument.getRole() == null)
            throw new ResourceNotFoundException("Role not found or invalid");
    }
}
