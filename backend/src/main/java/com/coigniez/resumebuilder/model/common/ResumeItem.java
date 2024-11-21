package com.coigniez.resumebuilder.model.common;

import com.coigniez.resumebuilder.model.resume.resume.Resume;

/**
 * Interface for resume section items.
 * Defines contract for items that belong to a resume.
 */
public interface ResumeItem {

    /**
     * Gets the resume this item belongs to.
     *
     * @return the parent resume
     */
    Resume getResume();

    /**
     * Sets the resume this item belongs to.
     *
     * @param resume the parent resume to set
     */
    void setResume(Resume resume);
}