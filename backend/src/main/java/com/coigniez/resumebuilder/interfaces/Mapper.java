package com.coigniez.resumebuilder.interfaces;

public interface Mapper<Entity, Request, Response> {

    /**
     * Convert a request to an entity
     * 
     * @param request the request to convert
     * @return the entity
     */
    Entity toEntity(Request request);

    /**
     * Convert an entity to a response
     * 
     * @param entity the entity to convert
     * @return the response
     */
    Response toDto(Entity entity);

    /**
     * Update an entity with a request
     * This method makes sure that only the fields that are allowed to be updated are updated
     * 
     * @param entity the entity to update
     * @param request the request to update with
     */
    void updateEntity(Entity entity, Request request);

}
