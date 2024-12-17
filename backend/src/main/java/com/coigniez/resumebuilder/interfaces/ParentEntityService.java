package com.coigniez.resumebuilder.interfaces;

import java.util.List;

/**
 * Interface for services that manage entities that have a parent entity.
 */
public interface ParentEntityService<Request extends ObjectHasID, Response, ID> extends CrudService<Request, Response, ID> {

    /**
     * Get all entities that have a parent entity with the given ID.
     * 
     * @param parentId The ID of the parent entity.
     * @return A list of entities that have the given parent entity.
     */
    List<Response> getAllByParentId(ID parentId);

    /**
     * Remove all entities that have a parent entity with the given ID.
     * 
     * @param parentId The ID of the parent entity.
     */
    void removeAllByParentId(ID parentId);
}
