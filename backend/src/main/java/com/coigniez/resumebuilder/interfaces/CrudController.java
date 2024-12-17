package com.coigniez.resumebuilder.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public interface CrudController <Request, Response> {
   
    /**
     * Create a new entity with the given request
     * 
     * @param request the request to create the entity
     * @param user the authenticated user
     * @return the id of the created entity
     */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody Request request, Authentication user);

    /**
     * Get the entity with the given id
     * 
     * @param id the id of the entity to get
     * @param user the authenticated user
     * @return the entity with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response> get(@PathVariable Long id, Authentication user);

    /**
     * Update the entity with the given id with the given request
     * 
     * @param id the id of the entity to update
     * @param request the request to update the entity
     * @param user the authenticated user
     * @return an empty response
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody Request request, Authentication user);

    /**
     * Delete the entity with the given id
     * 
     * @param id the id of the entity to delete
     * @param user the authenticated user
     * @return an empty response
     */
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication user);
}


