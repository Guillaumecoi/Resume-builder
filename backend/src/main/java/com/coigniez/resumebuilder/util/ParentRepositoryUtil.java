package com.coigniez.resumebuilder.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Transactional
@Component
public class ParentRepositoryUtil {

    @Autowired
    private EntityManager entityManager;

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
    public <T, ID> List<T> findAllByParentId(Class<T> entityClass, Class<?> parentClass, ID parentId, String orderBy) {
        String query = "SELECT e FROM " + getEntityName(entityClass) + " e WHERE e."
                + getParentName(parentClass) + ".id = :parentId";
                
        query += (orderBy != null ? " ORDER BY e." + orderBy : "");
    
        return entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .getResultList();
    }

    private String getEntityName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    private String getParentName(Class<?> parentClass) {
        return parentClass.getSimpleName().substring(0, 1).toLowerCase()
                + parentClass.getSimpleName().substring(1);
    }
}
