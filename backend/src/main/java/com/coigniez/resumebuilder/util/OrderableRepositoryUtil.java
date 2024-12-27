package com.coigniez.resumebuilder.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;  

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional
@Component  
public class OrderableRepositoryUtil {

    @Autowired
    private EntityManager entityManager; 

    /**
     * Method to find the maximum item order by parent id
     * 
     * @param <ID>         The type of the parent id
     * @param entityClass  The entity class
     * @param parentClass  The parent class
     * @param parentId     The parent id
     * @return The maximum item order if found, otherwise 0
     */
    public <ID> int findMaxItemOrderByParentId(Class<?> entityClass, Class<?> parentClass, ID parentId) {
        String entityName = entityClass.getSimpleName();
        String parentField = parentClass.getSimpleName().toLowerCase();
        String query = "SELECT MAX(e.itemOrder) FROM " + entityName + " e WHERE e." 
                + parentField + ".id = :parentId";
        
        List<Integer> resultList = entityManager.createQuery(query, Integer.class)
                .setParameter("parentId", parentId)
                .getResultList();
        
        return resultList.get(0) == null ? 0 : resultList.get(0);
    }

    /**
     * Method to find all entities by parent id
     * 
     * @param <T>         The type of the entity
     * @param <ID>        The type of the parent id
     * @param entityClass The entity class
     * @param parentClass The parent class
     * @param parentId    The parent id
     * @return The list of entities ordered by item order
     */
    @SuppressWarnings("unchecked")
    public <T, ID> List<T> findAllByParentId(Class<T> entityClass, Class<?> parentClass, ID parentId) {
        String entityName = entityClass.getSimpleName();
        String parentField = parentClass.getSimpleName().toLowerCase();
        String query = "SELECT e FROM " + entityName + " e WHERE e." 
                + parentField + ".id = :parentId ORDER BY e.itemOrder";
        
        return entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .getResultList();
    }

    /**
     * Method to update item order
     * 
     * @param <ID>      The type of the parent id
     * @param entityClass The entity class
     * @param parentClass The parent class
     * @param parentId    The parent id
     * @param newOrder    The new order
     * @param oldOrder    The old order
     */
    public <ID> void updateItemOrder(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder, int oldOrder) {
        // Update the item order of the other items
        if (newOrder == oldOrder) {
            return;
        } else if (newOrder < oldOrder) {
            incrementItemOrderBetween(entityClass, parentClass, parentId, newOrder, oldOrder);
        } else {
            decrementItemOrderBetween(entityClass, parentClass, parentId, newOrder, oldOrder);
        }

        // Refresh the entities to get the updated item order
        refreshEntityItems(findAllByParentId(entityClass, parentClass, parentId));
    }

    private <ID> void incrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder, int oldOrder) {
        String entityName = entityClass.getSimpleName();
        String parentField = parentClass.getSimpleName().toLowerCase();
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder + 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder >= :newOrder
                AND e.itemOrder < :oldOrder
                """.formatted(entityName, parentField);
        
        entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .setParameter("newOrder", newOrder)
                .setParameter("oldOrder", oldOrder)
                .executeUpdate();
    }

    private <ID> void decrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder, int oldOrder) {
        String entityName = entityClass.getSimpleName();
        String parentField = parentClass.getSimpleName().toLowerCase();
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder - 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder > :oldOrder
                AND e.itemOrder <= :newOrder
                """.formatted(entityName, parentField);
        
        entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .setParameter("newOrder", newOrder)
                .setParameter("oldOrder", oldOrder)
                .executeUpdate();
    }

    private void refreshEntityItems(List<?> items) {
        for (Object item : items) {
            entityManager.refresh(item);
        }
    }
}
