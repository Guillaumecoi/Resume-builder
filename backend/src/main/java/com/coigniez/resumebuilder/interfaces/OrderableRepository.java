package com.coigniez.resumebuilder.interfaces;

import java.util.Optional;

import org.springframework.data.repository.query.Param;

/**
 * OrderableRepository is an interface that provides methods to be able to order
 * items in the table by a parent id.
 */
public interface OrderableRepository<T> {

    /**
     * Find the maximum item order by parent id.
     * 
     * @param parentId the parent id
     * @return the maximum item order or null if none of the children has an order
     */
    Optional<Integer> findMaxItemOrderByParentId(@Param("parentId") Long parentId);

    /**
     * Increment the item order. This method is used when an item is moved down.
     * newOrder should be smaller than oldOrder.
     * 
     * @param parentId the parent id of the item
     * @param newOrder the new order of the item
     * @param oldOrder the old order of the item
     */
    void incrementItemOrderBetween(Long parentId, int newOrder, int oldOrder);

    /**
     * Decrement the item order. This method is used when an item is moved up.
     * newOrder should be greater than oldOrder.
     * 
     * @param parentId the parent id of the item
     * @param newOrder the new order of the item
     * @param oldOrder the old order of the item
     */
    void decrementItemOrderBetween(Long parentId, int newOrder, int oldOrder);

}
