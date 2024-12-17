package com.coigniez.resumebuilder.interfaces;

/**
 * Interface for entities with creator tracking.
 * Defines contract for user creation auditing in domain entities.
 */
public interface Creatable {

    /**
     * Gets the creator of the entity.
     *
     * @return the creator's identifier
     */
    String getCreatedBy();

    /**
     * Sets the creator of the entity.
     *
     * @param createdBy the creator's identifier to set
     */
    void setCreatedBy(String createdBy);
}