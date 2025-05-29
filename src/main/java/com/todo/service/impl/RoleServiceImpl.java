package com.todo.service.impl;

import com.todo.model.*;
import com.todo.model.enums.ActionEnum;
import com.todo.model.enums.RoleActionMapEnum;
import com.todo.model.enums.RoleEnum;
import com.todo.repository.RoleRepository;
import com.todo.service.ActionService;
import com.todo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ActionService actionService;

    public RoleServiceImpl(RoleRepository roleRepository, ActionService actionService) {
        this.roleRepository = roleRepository;
        this.actionService = actionService;
    }

    @Override
    public void saveRoles() {
        if (roleRepository.count() == 0) {
            Map<RoleEnum, List<ActionEnum>> roleActionsMap = getRoleActionsMap();
            Map<ActionEnum, ActionDocument> actionMap = getActionMap();

            saveRoles(roleActionsMap, actionMap);
        }
    }

    private Map<RoleEnum, List<ActionEnum>> getRoleActionsMap() {
        return Map.of(RoleEnum.ROLE_USER, RoleActionMapEnum.ROLE_USER.getAllowedActions(),
                RoleEnum.ROLE_ADMIN, RoleActionMapEnum.ROLE_ADMIN.getAllowedActions());
    }

    private Map<ActionEnum, ActionDocument> getActionMap() {
        List<ActionDocument> actions = actionService.getActions();
        return actions.stream().collect(Collectors.toMap(ActionDocument::getAction, actionDocument -> actionDocument));
    }

    private void saveRoles(Map<RoleEnum, List<ActionEnum>> roleActionsMap, Map<ActionEnum, ActionDocument> actionMap) {
        roleActionsMap.forEach((role, actions) -> {
            Set<ActionDocument> actionDocuments = actions.stream()
                    .map(actionMap::get)
                    .collect(Collectors.toSet());

            RoleDocument roleDocument = new RoleDocument(role, actionDocuments);
            roleRepository.save(roleDocument);
        });
    }

    @Override
    public RoleDocument getRoleDocumentByRoleEnum(RoleEnum roleEnum) {
        return roleRepository.findByRole(roleEnum);
    }
}
