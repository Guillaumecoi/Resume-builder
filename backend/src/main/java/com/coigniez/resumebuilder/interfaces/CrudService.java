package com.coigniez.resumebuilder.interfaces;

import org.springframework.security.core.Authentication;

public interface CrudService<Response, Request> {
 
    /**
     * Create a new object
     * 
     * @param request the object to create
     * @param user the connected user
     * @return the id of the created object
     */
    Long create(Request request, Authentication user);

    /**
     * Get an object by its id
     * 
     * @param id the id of the object to get
     * @param connectedUser the connected user
     * @return the object
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    Response get(Long id, Authentication user);

    /**
     * Update an object
     * 
     * @param request the object to update
     * @param user the connected user
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    void update(Request request, Authentication user);

    /**
     * Delete an object by its id
     * 
     * @param id the id of the object to delete
     * @param user the connected user
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    void delete(Long id, Authentication user);
}
