package com.todo.service.impl;

import com.todo.config.AdminProperties;
import com.todo.dto.UserDto;
import com.todo.dto.user.RegisterRequest;
import com.todo.dto.user.RegisterResponse;
import com.todo.mapper.UserMapper;
import com.todo.model.RoleDocument;
import com.todo.model.UserDocument;
import com.todo.model.enums.RoleEnum;
import com.todo.repository.UserRepository;
import com.todo.service.RoleService;
import com.todo.service.UserService;
import com.todo.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final AdminProperties adminProperties;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(AdminProperties adminProperties, UserRepository userRepository, UserValidator userValidator,
                           RoleService roleService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.adminProperties = adminProperties;
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveAdmin() {
        String username = adminProperties.getUsername();
        String password = adminProperties.getPassword();

        if (userRepository.findByUsername(username) == null) {
            RoleDocument roleDocument = roleService.getRoleDocumentByRoleEnum(RoleEnum.ROLE_ADMIN);
            userValidator.validateRoleDocument(roleDocument);

            UserDocument adminDocument = generateAdminDocument(username, password, roleDocument);
            userRepository.save(adminDocument);
        }
    }

    private UserDocument generateAdminDocument(String username, String password, RoleDocument roleDocument) {
        UserDocument adminDocument = new UserDocument();
        adminDocument.setUsername(username);
        String encodedPassword = passwordEncoder.encode(password);
        adminDocument.setPassword(encodedPassword);
        adminDocument.setEmail("adminemail@gmail.com");
        adminDocument.setRoles(Set.of(roleDocument));

        return adminDocument;
    }

    @Override
    public UserDocument getUserDocumentByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        UserDocument existingUserDocument = userRepository.findByUsername(registerRequest.username());
        userValidator.validateUserDocument(existingUserDocument);

        RoleDocument roleDocument = roleService.getRoleDocumentByRoleEnum(RoleEnum.ROLE_USER);
        userValidator.validateRoleDocument(roleDocument);

        UserDocument userDocument = generateUserDocument(registerRequest, roleDocument);
        UserDocument registeredUserDocument = userRepository.save(userDocument);

        return userMapper.toRegisterResponse(registeredUserDocument);
    }

    private UserDocument generateUserDocument(RegisterRequest registerRequest, RoleDocument roleDocument) {
        UserDocument userDocument = userMapper.toUserDocument(registerRequest, Set.of(roleDocument));
        String encodedPassword = passwordEncoder.encode(registerRequest.password());
        userDocument.setPassword(encodedPassword);

        return userDocument;
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {
        UserDocument userDocument = userRepository.findByUsername(username);
        return userMapper.toUserDto(userDocument);
    }
}
