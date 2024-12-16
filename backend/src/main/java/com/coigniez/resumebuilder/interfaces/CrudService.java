package com.coigniez.resumebuilder.interfaces;

/**
 * Interface for CRUD operations
 * 
 * @param <Response> the response object type
 * @param <Request> the request object type
 */
public interface CrudService<Response, Request> {
 
    /**
     * Create a new object
     * 
     * @param request the object to create
     * @return the id of the created object
     * @throws AccessDeniedException if the connected user is not permitted to create the object
     */
    Long create(Request request);

    /**
     * Get an object by its id
     * 
     * @param id the id of the object to get
     * @return the object
     * @throws AccessDeniedException if the connected user is not permitted to get the object
     * @throws EntityNotFoundException if the object is not found
     */
    Response get(long id);

    /**
     * Update an object
     * 
     * @param id the id of the object to update
     * @param request the object to update
     * @throws AccessDeniedException if the connected user is not permitted to update the object
     * @throws EntityNotFoundException if the object is not found
     */
    void update(Request request);

    /**
     * Delete an object by its id
     * 
     * @param id the id of the object to delete
     * @throws AccessDeniedException if the connected user is not permitted to delete the object
     * @throws EntityNotFoundException if the object is not found
     */
    void delete(long id);
}
