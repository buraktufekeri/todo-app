package com.todo.service.impl;

import com.todo.dto.UserDto;
import com.todo.dto.auth.LoginRequest;
import com.todo.dto.auth.LoginResponse;
import com.todo.model.ActionDocument;
import com.todo.model.RoleDocument;
import com.todo.model.enums.ActionEnum;
import com.todo.model.enums.RoleEnum;
import com.todo.security.jwt.JwtTokenProvider;
import com.todo.service.UserService;
import com.todo.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("AuthServiceImplTest Unit Tests")
class AuthServiceImplTest {

    private UserService userService;
    private AuthValidator authValidator;
    private JwtTokenProvider jwtTokenProvider;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        authValidator = mock(AuthValidator.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);

        authService = new AuthServiceImpl(userService, authValidator, jwtTokenProvider);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        final Set<ActionDocument> actionDocuments = Set.of(
                new ActionDocument(ActionEnum.CREATE_TODO),
                new ActionDocument(ActionEnum.READ_OWN_TODO),
                new ActionDocument(ActionEnum.UPDATE_OWN_TODO),
                new ActionDocument(ActionEnum.DELETE_OWN_TODO)
        );
        final LoginRequest loginRequest = new LoginRequest("TestUser", "Password123");
        final UserDto userDto = createUserDto(
                "user::123",
                "TestUser",
                "Password123",
                "testuser@gmail.com",
                RoleEnum.ROLE_USER, actionDocuments);
        final String token = "mocked.jwt.token";

        mockLoginDependencies(loginRequest, userDto, token);

        final LoginResponse response = authService.login(loginRequest);

        verifyLoginResponse(userDto, token, response);

        verifyMocks(loginRequest, userDto);
    }

    @Test
    void shouldAuthenticateAdminSuccessfully() {
        final Set<ActionDocument> actionDocuments = Set.of(
                new ActionDocument(ActionEnum.READ_ALL_TODOS),
                new ActionDocument(ActionEnum.DELETE_ANY_TODO),
                new ActionDocument(ActionEnum.MANAGE_USERS)
        );
        final LoginRequest loginRequest = new LoginRequest("AdminUser", "AdminPass123");
        final UserDto adminDto = createUserDto(
                "admin::001",
                "AdminUser",
                "AdminPass123",
                "admin@example.com",
                RoleEnum.ROLE_ADMIN, actionDocuments
        );
        final String token = "mocked.admin.jwt.token";

        mockLoginDependencies(loginRequest, adminDto, token);

        final LoginResponse response = authService.login(loginRequest);

        verifyLoginResponse(adminDto, token, response);

        verifyMocks(loginRequest, adminDto);
    }

    private UserDto createUserDto(String id, String username, String password, String email, RoleEnum roleEnum, Set<ActionDocument> actions) {
        RoleDocument role = new RoleDocument(roleEnum, actions);
        return new UserDto(id, username, password, email, Set.of(role));
    }

    private void mockLoginDependencies(LoginRequest loginRequest, UserDto userDto, String token) {
        when(userService.getUserDtoByUsername(loginRequest.username())).thenReturn(userDto);
        doNothing().when(authValidator).validatePassword(loginRequest, userDto);
        when(jwtTokenProvider.generateToken(loginRequest.username())).thenReturn(token);
    }

    private void verifyLoginResponse(UserDto userDto, String token, LoginResponse response) {
        assertEquals(userDto.id(), response.id());
        assertEquals(userDto.username(), response.username());
        assertEquals(userDto.email(), response.email());
        assertEquals(userDto.roles(), response.roles());
        assertEquals(token, response.token());
    }

    private void verifyMocks(LoginRequest loginRequest, UserDto userDto) {
        verify(userService).getUserDtoByUsername(loginRequest.username());
        verify(authValidator).validatePassword(loginRequest, userDto);
        verify(jwtTokenProvider).generateToken(loginRequest.username());
    }
}