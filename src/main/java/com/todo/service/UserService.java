package com.todo.service;

import com.todo.dto.UserDto;
import com.todo.dto.user.RegisterRequest;
import com.todo.dto.user.RegisterResponse;
import com.todo.model.UserDocument;

public interface UserService {

    void saveAdmin();

    UserDocument getUserDocumentByUsername(String username);

    RegisterResponse register(RegisterRequest registerRequest);

    UserDto getUserDtoByUsername(String username);
}
