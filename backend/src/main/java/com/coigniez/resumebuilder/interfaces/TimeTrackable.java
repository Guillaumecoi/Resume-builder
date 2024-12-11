package com.coigniez.resumebuilder.interfaces;

import java.time.LocalDateTime;

/**
 * Interface for entities with creation and modification timestamps.
 * Defines contract for temporal tracking in domain entities.
 */
public interface TimeTrackable {

    /**
     * Gets the creation date of the entity.
     *
     * @return the creation timestamp
     */
    LocalDateTime getCreatedDate();

    /**
     * Sets the creation date of the entity.
     *
     * @param createdDate the creation timestamp to set
     */
    void setCreatedDate(LocalDateTime createdDate);

    /**
     * Gets the last modification date of the entity.
     *
     * @return the last modification timestamp
     */
    LocalDateTime getLastModifiedDate();

    /**
     * Sets the last modification date of the entity.
     *
     * @param lastModifiedDate the last modification timestamp to set
     */
    void setLastModifiedDate(LocalDateTime lastModifiedDate);
}