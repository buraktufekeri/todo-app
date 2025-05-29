package com.todo.model.enums;

import java.util.List;

public enum RoleActionMapEnum {

    ROLE_USER(List.of(ActionEnum.CREATE_TODO,
            ActionEnum.READ_OWN_TODO,
            ActionEnum.UPDATE_OWN_TODO,
            ActionEnum.DELETE_OWN_TODO
    )),
    ROLE_ADMIN(List.of(
            ActionEnum.READ_ALL_TODOS,
            ActionEnum.DELETE_ANY_TODO,
            ActionEnum.MANAGE_USERS
    ));

    private final List<ActionEnum> allowedActions;

    RoleActionMapEnum(List<ActionEnum> allowedActions) {
        this.allowedActions = allowedActions;
    }

    public List<ActionEnum> getAllowedActions() {
        return allowedActions;
    }
}
