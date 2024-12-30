package com.coigniez.resumebuilder.interfaces;

import org.springframework.validation.annotation.Validated;

import com.coigniez.resumebuilder.validation.HasID;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

/**
 * Interface for CRUD operations
 * 
 * @param <CreateReq> the request what to create
 * @param <UpdateReq> the request what to update
 * @param <Resp>      the response you get
 * @param <ID>        the id of the object
 */
@Validated
@Transactional
public interface CrudService<CreateReq extends CreateRequest, UpdateReq extends UpdateRequest, Resp extends Response, ID> {

    /**
     * Create a new object
     * 
     * @param request the object to create
     * @return the id of the created object
     * @throws AccessDeniedException if the connected user is not permitted to
     *                               create the object
     */
    ID create(@NotNull CreateReq request);

    /**
     * Get an object by its id
     * 
     * @param id the id of the object to get
     * @return the object
     * @throws AccessDeniedException   if the connected user is not permitted to get
     *                                 the object
     * @throws EntityNotFoundException if the object is not found
     */
    Resp get(@NotNull ID id);

    /**
     * Update an object
     * 
     * @param request the object to update
     * @throws AccessDeniedException   if the connected user is not permitted to
     *                                 update the object
     * @throws EntityNotFoundException if the object is not found
     */
    void update(@NotNull @HasID UpdateReq request);

    /**
     * Delete an object by its id
     * 
     * @param id the id of the object to delete
     * @throws AccessDeniedException   if the connected user is not permitted to
     *                                 delete the object
     * @throws EntityNotFoundException if the object is not found
     */
    void delete(@NotNull ID id);
}
