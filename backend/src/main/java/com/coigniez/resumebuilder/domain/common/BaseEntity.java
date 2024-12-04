package com.coigniez.resumebuilder.domain.common;

/**
 * Base interface for entities with Long identifier.
 * Defines contract for ID management in domain entities.
 */
public interface BaseEntity {

    /**
     * Gets the unique identifier of the entity.
     *
     * @return the entity identifier
     */
    Long getId();

    /**
     * Sets the unique identifier of the entity.
     *
     * @param id the entity identifier to set
     */
    void setId(Long id);
}
