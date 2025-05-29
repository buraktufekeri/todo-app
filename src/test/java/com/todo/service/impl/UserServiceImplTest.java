package com.todo.service.impl;

import com.todo.dto.user.RegisterRequest;
import com.todo.dto.user.RegisterResponse;
import com.todo.exception.ConflictException;
import com.todo.mapper.UserMapper;
import com.todo.model.RoleDocument;
import com.todo.model.UserDocument;
import com.todo.model.enums.RoleEnum;
import com.todo.repository.UserRepository;
import com.todo.service.RoleService;
import com.todo.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserServiceImplTest Unit Tests")
class UserServiceImplTest {

    private UserRepository userRepository;
    private UserValidator userValidator;
    private RoleService roleService;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;
    private RegisterRequest registerRequest;
    private RoleDocument roleDocument;
    private Set<RoleDocument> roleSet;
    private UserDocument newUserDocument;
    private UserDocument savedUserDocument;
    private RegisterResponse expectedResponse;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userValidator = mock(UserValidator.class);
        roleService = mock(RoleService.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(null, userRepository, userValidator, roleService, userMapper, passwordEncoder);
        registerRequest = new RegisterRequest("newUser", "plainPassword123", "newuser@example.com");
        roleDocument = new RoleDocument(RoleEnum.ROLE_USER, Set.of());
        roleSet = Set.of(roleDocument);
        newUserDocument = new UserDocument();
        savedUserDocument = new UserDocument();
        expectedResponse = new RegisterResponse("id123", "newUser", "newuser@example.com", Set.of());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByUsername(registerRequest.username())).thenReturn(null);
        when(roleService.getRoleDocumentByRoleEnum(RoleEnum.ROLE_USER)).thenReturn(roleDocument);
        doNothing().when(userValidator).validateRoleDocument(roleDocument);
        when(userMapper.toUserDocument(registerRequest, roleSet)).thenReturn(newUserDocument);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(userRepository.save(newUserDocument)).thenReturn(savedUserDocument);
        when(userMapper.toRegisterResponse(savedUserDocument)).thenReturn(expectedResponse);

        RegisterResponse actualResponse = userService.register(registerRequest);

        assertEquals(expectedResponse, actualResponse);

        verify(userRepository).findByUsername(registerRequest.username());
        verify(roleService).getRoleDocumentByRoleEnum(RoleEnum.ROLE_USER);
        verify(userValidator).validateRoleDocument(roleDocument);
        verify(userMapper).toUserDocument(registerRequest, roleSet);
        verify(passwordEncoder).encode(registerRequest.password());
        verify(userRepository).save(newUserDocument);
        verify(userMapper).toRegisterResponse(savedUserDocument);
    }

    @Test
    void shouldThrowConflictExceptionWhenUserAlreadyExists() {
        RegisterRequest conflictRequest = new RegisterRequest("existingUser", "password", "email@example.com");
        UserDocument existingUser = new UserDocument();
        existingUser.setUsername("existingUser");

        when(userRepository.findByUsername(conflictRequest.username())).thenReturn(existingUser);
        doThrow(new ConflictException("Username is already taken, username:existingUser"))
                .when(userValidator).validateUserDocument(existingUser);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            userService.register(conflictRequest);
        });

        assertTrue(exception.getMessage().contains("Username is already taken"));

        verify(userRepository).findByUsername(conflictRequest.username());
        verify(userValidator).validateUserDocument(existingUser);
        verifyNoMoreInteractions(roleService, userMapper, passwordEncoder, userRepository);
    }
}