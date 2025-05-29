package com.todo.model;

import com.todo.model.enums.RoleEnum;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Set;

@Document
public class RoleDocument extends BaseDocument {

    @Field("role")
    private RoleEnum role;

    @Field("actions")
    private Set<ActionDocument> actions;

    public RoleDocument() {
        super("role");
    }

    public RoleDocument(RoleEnum role, Set<ActionDocument> actions) {
        this();
        this.role = role;
        this.actions = actions;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Set<ActionDocument> getActions() {
        return actions;
    }

    public void setActions(Set<ActionDocument> actions) {
        this.actions = actions;
    }
}
