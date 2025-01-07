package com.coigniez.resumebuilder.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     * Assigns sequential orders to items, preserving existing valid orders.
     * Duplicate orders are incremented by 1.
     * 
     * @param <T>         Type of items
     * @param items       List of items to order
     * @param orderGetter Function to get order from item
     * @param orderSetter Function to set order on item
     * @return Array of ordered items with their orders set.
     */
    public <T> T[] assignOrders(List<T> items,
            Function<T, Integer> orderGetter,
            BiConsumer<T, Integer> orderSetter,
            IntFunction<T[]> arrayCreator) {
        if (items == null)
            return arrayCreator.apply(0);

        // Create result array and available orders
        T[] result = arrayCreator.apply(items.size());
        List<Integer> availableOrders = IntStream.range(1, items.size() + 1).boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        // Get items with and without order
        List<T> withOrder = items.stream()
                .filter(item -> orderGetter.apply(item) != null &&
                        availableOrders.contains(orderGetter.apply(item)))
                .sorted(Comparator.comparing(orderGetter))
                .collect(Collectors.toList());
        List<T> withoutOrder = items.stream()
                .filter(item -> orderGetter.apply(item) == null ||
                        !availableOrders.contains(orderGetter.apply(item)))
                .collect(Collectors.toList());

        // Assign orders to items
        for (T item : withOrder) {
            // Increment order if duplicate
            if (!availableOrders.contains(orderGetter.apply(item))) {
                orderSetter.accept(item, orderGetter.apply(item) + 1);
            }
            int order = orderGetter.apply(item);
            result[order - 1] = item;
            availableOrders.remove(Integer.valueOf(order));
        }
        for (T item : withoutOrder) {
            int newOrder = availableOrders.getFirst();
            orderSetter.accept(item, newOrder);
            availableOrders.remove(0);
            result[newOrder - 1] = item;
        }

        return result;
    }

    /**
     * Method to find the maximum item order by parent id
     * 
     * @param <ID>        The type of the parent id
     * @param entityClass The entity class
     * @param parentClass The parent class
     * @param parentId    The parent id
     * @return The maximum item order if found, otherwise 0
     */
    public <ID> int findMaxItemOrderByParentId(Class<?> entityClass, Class<?> parentClass, ID parentId,
            String orderName) {
        String query = "SELECT MAX(e.%s) FROM %s e WHERE e.%s.id = :parentId"
                .formatted(orderName, getEntityName(entityClass), getParentName(parentClass));

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
    public <ID> int updateItemOrder(Class<?> entityClass, Class<?> parentClass, ID parentId,
            String orderName, Integer newOrder, Integer oldOrder) {
        // Set the orders if they are null
        int maxOrder = findMaxItemOrderByParentId(entityClass, parentClass, parentId, orderName);
        if (oldOrder == null) {
            oldOrder = maxOrder + 1;
        }
        if (newOrder == null) {
            newOrder = maxOrder + 1;
        }

        // Update the item order of the other items
        if (newOrder == oldOrder) {
            return newOrder;
        } else if (newOrder < oldOrder) {
            incrementItemOrderBetween(entityClass, parentClass, parentId, orderName, newOrder, oldOrder);
        } else {
            decrementItemOrderBetween(entityClass, parentClass, parentId, orderName, newOrder, oldOrder);
        }

        // Refresh the entities to get the updated item order
        refreshEntityItems(parentRepositoryUtil.findAllByParentId(entityClass, parentClass, parentId, orderName));

        return newOrder;
    }

    private <ID> void incrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId,
            String orderName, int newOrder, int oldOrder) {
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder + 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder >= :newOrder
                AND e.itemOrder < :oldOrder
                """.replace("itemOrder", orderName).formatted(getEntityName(entityClass), getParentName(parentClass));

        entityManager.createQuery(query)
                .setParameter("parentId", parentId)
                .setParameter("newOrder", newOrder)
                .setParameter("oldOrder", oldOrder)
                .executeUpdate();
    }

    private <ID> void decrementItemOrderBetween(Class<?> entityClass, Class<?> parentClass, ID parentId,
            String orderName, int newOrder, int oldOrder) {
        String query = """
                UPDATE %s e
                SET e.itemOrder = e.itemOrder - 1
                WHERE e.%s.id = :parentId
                AND e.itemOrder > :oldOrder
                AND e.itemOrder <= :newOrder
                """.replace("itemOrder", orderName).formatted(getEntityName(entityClass), getParentName(parentClass));

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
