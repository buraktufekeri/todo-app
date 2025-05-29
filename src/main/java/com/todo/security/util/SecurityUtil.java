package com.todo.security.util;

import com.todo.exception.UnauthorizedException;
import com.todo.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private static final String UNAUTHORIZED_EXCEPTION_MESSAGE = "User identity verification failed";

    private SecurityUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getCurrentUsername() {
        Authentication authentication = getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)
            return customUserDetails.getUsername();

        throw new UnauthorizedException(UNAUTHORIZED_EXCEPTION_MESSAGE);
    }

    public static String getCurrentUserId() {
        Authentication authentication = getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)
            return customUserDetails.getId();

        throw new UnauthorizedException(UNAUTHORIZED_EXCEPTION_MESSAGE);
    }
}
