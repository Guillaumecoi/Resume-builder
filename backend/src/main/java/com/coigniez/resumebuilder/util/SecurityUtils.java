package com.coigniez.resumebuilder.util;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.NonNull;

@Component
public class SecurityUtils {

    /**
     * Get the current authentication object
     * 
     * @return the current authentication object
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get the username of the current user
     * 
     * @return the username of the current user
     */
    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Check if the current user has access to the resource
     * 
     * @param usernames List of usernames that have access to the resource
     * @throws AccessDeniedException if the current user does not have access
     */
    public void hasAccess(@NonNull List<String> usernames) {
        String currentUsername = getUserName();
        if (!usernames.contains(currentUsername)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}