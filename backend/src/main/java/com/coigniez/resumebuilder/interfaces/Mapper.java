package com.coigniez.resumebuilder.interfaces;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;

/**
 * Interface for mapping between entities and request/response objects.
 * 
 * @param <E>  the entity type
 * @param <CR> the create request type
 * @param <UR> the update request type
 * @param <R>  the response type
 */
@Validated
public interface Mapper<E, CR extends CreateRequest, UR extends UpdateRequest, R extends Response> {

    /**
     * Convert a create request object to an entity.
     * 
     * @param request the create request object
     * @return the entity
     */
    E toEntity(@Valid CR request);

    /**
     * Convert an entity to a response object.
     * 
     * @param entity the entity
     * @return the response object
     */
    R toDto(E entity);

    /**
     * Update an entity with values from an update request object.
     * 
     * @param entity  the entity to update
     * @param request the update request object with updated values
     */
    void updateEntity(E entity, @Valid UR request);
}
