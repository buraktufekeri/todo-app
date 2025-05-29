package com.todo.mapper;

import com.todo.dto.UserDto;
import com.todo.dto.user.RegisterRequest;
import com.todo.dto.user.RegisterResponse;
import com.todo.model.RoleDocument;
import com.todo.model.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roleDocuments")
    UserDocument toUserDocument(RegisterRequest registerRequest, Set<RoleDocument> roleDocuments);

    RegisterResponse toRegisterResponse(UserDocument userDocument);

    UserDto toUserDto(UserDocument userDocument);
}
