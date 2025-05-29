package com.todo.security;

public interface CustomMethodSecurityExpression {

    boolean hasActions(String... actions);

    boolean hasAnyAction(String... actions);
}
