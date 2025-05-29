package com.todo.validator;

import com.todo.dto.UserDto;
import com.todo.dto.auth.LoginRequest;
import com.todo.exception.UnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {

    private final PasswordEncoder passwordEncoder;

    public AuthValidator(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void validatePassword(LoginRequest loginRequest, UserDto userDto) {
        boolean invalid =
                loginRequest == null || loginRequest.password() == null
                || userDto == null || userDto.password() == null
                || !passwordEncoder.matches(loginRequest.password(), userDto.password());

        if (invalid)
            throw new UnauthorizedException("Invalid username or password");
    }
}
