package com.todo.model;

import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Set;

@Document
public class UserDocument extends BaseDocument {

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("email")
    private String email;

    @Field("roles")
    private Set<RoleDocument> roles;

    public UserDocument() {
        super("user");
    }

    public UserDocument(String username, String password, String email, Set<RoleDocument> roles) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDocument> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDocument> roles) {
        this.roles = roles;
    }
}
