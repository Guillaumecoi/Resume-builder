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
    public <T, ID> List<T> findAllByParentId(Class<T> entityClass, Class<?> parentClass, ID parentId) {
        String query = "SELECT e FROM " + getEntityName(entityClass) + " e WHERE e." 
                + getParentName(parentClass) + ".id = :parentId ORDER BY e.itemOrder";
        
        return entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .getResultList();
    }

    /**
     * Method to remove all entities by parent id
     * 
     * @param <ID>         The type of the parent id
     * @param entityClass  The entity class
     * @param parentClass  The parent class
     * @param parentId     The parent id
     */
    public <ID> void removeAllByParentId(Class<?> entityClass, Class<?> parentClass, ID parentId) {
        String query = "DELETE FROM " + getEntityName(entityClass) + " e WHERE e." 
                + getParentName(parentClass) + ".id = :parentId";
        
        entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .executeUpdate();
    }


    private String getEntityName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    private String getParentName(Class<?> parentClass) {
        return parentClass.getSimpleName().substring(0, 1).toLowerCase()
                + parentClass.getSimpleName().substring(1);
    }
}
