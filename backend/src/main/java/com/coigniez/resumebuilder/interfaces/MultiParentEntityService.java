package com.coigniez.resumebuilder.interfaces;

import java.util.List;

public interface MultiParentEntityService<Request extends ObjectHasID, Response, ID, ParentType> extends CrudService<Request, Response, ID> {

    /**
     * Get all entities that have a parent entity with the given ID.
     * 
     * @param parentType The type of the parent entity.
     * @param parentId The ID of the parent entity.
     * @return A list of entities that have the given parent entity.
     */
    List<Response> getAllByParentId(ParentType parentType, ID parentId);

    /**
     * Remove all entities that have a parent entity with the given ID.
     * 
     * @param parentType The type of the parent entity.
     * @param parentId The ID of the parent entity.
     */
    void removeAllByParentId(ParentType parentType, ID parentId);
    
}
