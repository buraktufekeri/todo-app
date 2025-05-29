package com.todo.service;

import com.todo.model.RoleDocument;
import com.todo.model.enums.RoleEnum;

public interface RoleService {

    void saveRoles();

    RoleDocument getRoleDocumentByRoleEnum(RoleEnum roleEnum);
}
