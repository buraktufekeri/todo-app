package com.todo.config;

import com.todo.service.ActionService;
import com.todo.service.RoleService;
import com.todo.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final ActionService actionService;
    private final RoleService roleService;
    private final UserService userService;

    public DataLoader(ActionService actionService, RoleService roleService, UserService userService) {
        this.actionService = actionService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        actionService.saveActions();
        roleService.saveRoles();
        userService.saveAdmin();
    }
}
