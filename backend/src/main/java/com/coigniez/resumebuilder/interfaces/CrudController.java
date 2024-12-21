package com.coigniez.resumebuilder.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

/**
 * Interface for CRUD operations
 * 
 * @param <CreateReq> the request what to create
 * @param <UpdateReq> the request what to update
 * @param <Resp>      the response you get
 * @param <ID>        the id of the object
 */
@Validated
public interface CrudController<CreateReq extends CreateRequest, UpdateReq extends UpdateRequest, Resp extends Response, ID> {
   
    /**
     * Create a new entity with the given request
     * 
     * @param request the request to create the entity
     * @param user the authenticated user
     * @return the id of the created entity
     */
    @PostMapping
    public ResponseEntity<ID> create(@Valid @RequestBody CreateReq request, Authentication user);

    /**
     * Get the entity with the given id
     * 
     * @param id the id of the entity to get
     * @param user the authenticated user
     * @return the entity with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resp> get(@PathVariable ID id, Authentication user);

    /**
     * Update the entity with the given id with the given request
     * 
     * @param id the id of the entity to update
     * @param request the request to update the entity
     * @param user the authenticated user
     * @return an empty response
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable ID id, @RequestBody UpdateReq request, Authentication user);

    /**
     * Delete the entity with the given id
     * 
     * @param id the id of the entity to delete
     * @param user the authenticated user
     * @return an empty response
     */
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable ID id, Authentication user);
}


