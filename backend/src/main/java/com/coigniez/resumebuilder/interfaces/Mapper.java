package com.coigniez.resumebuilder.interfaces;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

/**
 * Interface for mapping between entities and request/response objects.
 * 
 * @param <Entity>   the entity type
 * @param <CreateReq> the create request type
 * @param <UpdateReq> the update request type
 * @param <Resp>      the response type
 */
@Validated
public interface Mapper<Entity, CreateReq extends CreateRequest, UpdateReq extends UpdateRequest, Resp extends Response> {

    /**
     * Convert a create request object to an entity.
     * 
     * @param request the create request object
     * @return the entity
     */
    Entity toEntity(@Valid CreateReq request);

    /**
     * Convert an entity to a response object.
     * 
     * @param entity the entity
     * @return the response object
     */
    Resp toDto(Entity entity);

    /**
     * Update an entity with values from an update request object.
     * 
     * @param entity the entity to update
     * @param request the update request object with updated values
     */
    void updateEntity(Entity entity, @Valid UpdateReq request);
}
