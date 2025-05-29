package com.todo.service.impl;

import com.todo.dto.auth.LoginRequest;
import com.todo.dto.auth.LoginResponse;
import com.todo.dto.UserDto;
import com.todo.security.jwt.JwtTokenProvider;
import com.todo.service.AuthService;
import com.todo.service.UserService;
import com.todo.validator.AuthValidator;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthValidator authValidator;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserService userService, AuthValidator authValidator, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authValidator = authValidator;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserDto userDto = userService.getUserDtoByUsername(loginRequest.username());
        authValidator.validatePassword(loginRequest, userDto);

        return generateLoginResponse(userDto);
    }

    private LoginResponse generateLoginResponse(UserDto userDto) {
        String token = jwtTokenProvider.generateToken(userDto.username());
        return new LoginResponse(userDto.id(), userDto.username(), userDto.email(), userDto.roles(), token);
    }
}