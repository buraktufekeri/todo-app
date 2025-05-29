package com.todo.security;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("customMethodSecurityExpression")
public class CustomMethodSecurityExpressionImpl implements CustomMethodSecurityExpression {

    private Set<String> toSet(String... values) {
        if (ObjectUtils.isEmpty(values))
            return Set.of();

        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private Set<String> getUserActions() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? Set.of() : auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasActions(String... actions) {
        Set<String> userActions = getUserActions();
        return hasAll(userActions, actions);
    }

    @Override
    public boolean hasAnyAction(String... actions) {
        Set<String> userActions = getUserActions();
        return hasAny(userActions, actions);
    }

    private boolean hasAll(Set<String> userActions, String... actions) {
        Set<String> actionsSet = toSet(actions);
        return !actionsSet.isEmpty() && userActions.containsAll(actionsSet);
    }

    private boolean hasAny(Set<String> userActions, String... actions) {
        Set<String> actionsSet = toSet(actions);
        return !actionsSet.isEmpty() && !Collections.disjoint(actionsSet, userActions);
    }
}
