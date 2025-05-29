package com.todo.security;

import com.todo.model.UserDocument;
import com.todo.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDocument userDocument = Optional.ofNullable(userService.getUserDocumentByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username:" + username));
        return CustomUserDetails.fromUserEntity(userDocument);
    }
}
