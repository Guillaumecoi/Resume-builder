package com.coigniez.resumebuilder.interfaces;

import java.util.List;

/**
 * Interface for CRUD services that manage entities that have a parent entity.
 * 
 * @param <CreateReq> the request what to create
 * @param <UpdateReq> the request what to update
 * @param <Resp>      the response you get
 * @param <ID>        the id of the object
 */
public interface ParentEntityService<CreateReq extends CreateRequest, UpdateReq extends UpdateRequest, Resp extends Response, ID>
        extends CrudService<CreateReq, UpdateReq, Resp, ID> {

    /**
     * Get all entities that have a parent entity with the given ID.
     * 
     * @param parentId The ID of the parent entity.
     * @return A list of entities that have the given parent entity.
     */
    List<Resp> getAllByParentId(ID parentId);

    /**
     * Remove all entities that have a parent entity with the given ID.
     * 
     * @param parentId The ID of the parent entity.
     */
    void removeAllByParentId(ID parentId);
}
