package com.coigniez.resumebuilder.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;

public class ExceptionUtils {

    public static EntityNotFoundException entityNotFound(String entityName, Long id) {
        return new EntityNotFoundException(entityName + " with id " + id + " not found");
    }

    public static AccessDeniedException accessDenied(String user, String entityName, Long id) {
        return new AccessDeniedException("User " + user + " does not have access to " + entityName + " with id " + id);
    }
}