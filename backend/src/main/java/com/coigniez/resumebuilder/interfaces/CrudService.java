package com.coigniez.resumebuilder.interfaces;

public interface CrudService<Response, Request> {
 
    /**
     * Create a new object
     * 
     * @param parentId the id of the parent object
     * @param request the object to create
     * @return the id of the created object
     */
    Long create(Long parentId, Request request);

    /**
     * Get an object by its id
     * 
     * @param id the id of the object to get
     * @return the object
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    Response get(Long id);

    /**
     * Update an object
     * 
     * @param id the id of the object to update
     * @param request the object to update
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    void update(Long id, Request request);

    /**
     * Delete an object by its id
     * 
     * @param id the id of the object to delete
     * @throws AccessDeniedException if the connected user is not the creator of the resume
     */
    void delete(Long id);
}
