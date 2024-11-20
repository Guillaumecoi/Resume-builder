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

}
