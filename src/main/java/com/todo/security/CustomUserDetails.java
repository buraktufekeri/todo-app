package com.todo.security;

import com.todo.model.UserDocument;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final UserDocument userDocument;

    public CustomUserDetails(UserDocument userDocument) {
        this.userDocument = userDocument;
    }

    public String getId() {
        return userDocument.getId();
    }

    public static CustomUserDetails fromUserEntity(UserDocument userDocument) {
        return new CustomUserDetails(userDocument);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDocument.getRoles().stream()
                .flatMap(role -> role.getActions().stream())
                .map(actionDocument -> new SimpleGrantedAuthority(actionDocument.getAction().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return userDocument.getPassword();
    }

    @Override
    public String getUsername() {
        return userDocument.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
