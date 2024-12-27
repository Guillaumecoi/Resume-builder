package com.coigniez.resumebuilder.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;  // Make it a Spring bean

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Component  // Spring will manage this class
public class OrderableRepositoryUtil {

    @Autowired
    private EntityManager entityManager;  // Instance-level injection (no static)

    // Method to find max item order by parent ID dynamically
    public <ID> Optional<Integer> findMaxItemOrderByParentId(Class<?> entityClass, Class<?> parentClass, ID parentId) {
        String entityName = entityClass.getSimpleName();
        String parentField = parentClass.getSimpleName().toLowerCase();
        String query = "SELECT MAX(e.itemOrder) FROM " + entityName + " e WHERE e." 
                + parentField + ".id = :parentId";
        
        return entityManager.createQuery(query, Integer.class)
                .setParameter("parentId", parentId)
                .getResultList()
                .stream()
                .findFirst();
    }

    // Method to increment item order between two orders dynamically
    @Transactional
    public <ID> void incrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder, int oldOrder) {
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

    // Method to decrement item order between two orders dynamically
    @Transactional
    public <ID> void decrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId, int newOrder, int oldOrder) {
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
}
