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
    @Autowired
    private ParentRepositoryUtil parentRepositoryUtil;

    /**
     * Method to find the maximum item order by parent id
     * 
     * @param <ID>        The type of the parent id
     * @param entityClass The entity class
     * @param parentClass The parent class
     * @param parentId    The parent id
     * @return The maximum item order if found, otherwise 0
     */
    public <ID> int findMaxItemOrderByParentId(Class<?> entityClass, Class<?> parentClass, ID parentId) {
        String query = "SELECT MAX(e.itemOrder) FROM " + getEntityName(entityClass) + " e WHERE e."
                + getParentName(parentClass) + ".id = :parentId";

        List<Integer> resultList = entityManager.createQuery(query, Integer.class)
                .setParameter("parentId", parentId)
                .getResultList();

        return resultList.get(0) == null ? 0 : resultList.get(0);
    }

    /**
     * Method to update item order
     * 
     * @param <ID>        The type of the parent id
     * @param entityClass The entity class
     * @param parentClass The parent class
     * @param parentId    The parent id
     * @param newOrder    The new order
     * @param oldOrder    The old order
     */
    public <ID> void updateItemOrder(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder,
            int oldOrder) {
        // Update the item order of the other items
        if (newOrder == oldOrder) {
            return;
        } else if (newOrder < oldOrder) {
            incrementItemOrderBetween(entityClass, parentClass, parentId, newOrder, oldOrder);
        } else {
            decrementItemOrderBetween(entityClass, parentClass, parentId, newOrder, oldOrder);
        }

        // Refresh the entities to get the updated item order
        refreshEntityItems(parentRepositoryUtil.findAllByParentId(entityClass, parentClass, parentId));
    }

    private <ID> void incrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder,
            int oldOrder) {
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder + 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder >= :newOrder
                AND e.itemOrder < :oldOrder
                """.formatted(getEntityName(entityClass), getParentName(parentClass));

        entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .setParameter("newOrder", newOrder)
                .setParameter("oldOrder", oldOrder)
                .executeUpdate();
    }

    private <ID> void decrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder,
            int oldOrder) {
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder - 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder > :oldOrder
                AND e.itemOrder <= :newOrder
                """.formatted(getEntityName(entityClass), getParentName(parentClass));

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

    private String getEntityName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    private String getParentName(Class<?> parentClass) {
        return parentClass.getSimpleName().substring(0, 1).toLowerCase()
                + parentClass.getSimpleName().substring(1);
    }
}
