package com.todo.service;

import com.todo.dto.auth.LoginRequest;
import com.todo.dto.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
}
