package com.coigniez.resumebuilder.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.repository.LatexMethodRepository;
import com.coigniez.resumebuilder.repository.LayoutRepository;
import com.coigniez.resumebuilder.repository.LayoutSectionItemRepository;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;

/**
 * Utility class for security related operations
 */
@Component
public class SecurityUtils {

    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private LatexMethodRepository latexMethodRepository;
    @Autowired
    private ColumnRepository columnRepository;
    @Autowired
    private ColumnSectionRepository columnSectionRepository;
    @Autowired
    private LayoutSectionItemRepository layoutSectionItemRepository;

    /**
     * Get the current authentication object
     * 
     * @return the current authentication object
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get the username of the current user
     * 
     * @return the username of the current user
     */
    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Check if the current user has access to the resource
     * 
     * @param usernames List of usernames that have access to the resource
     * @return true if the current user has access to the resource, false otherwise
     */
    public boolean hasAccess(@NonNull List<String> usernames) {
        String currentUsername = getUserName();
        return usernames.contains(currentUsername);
    }

    /**
     * Check if the connected user has access to the resume
     * 
     * @param id            the id of the resume
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the resume
     * @throws EntityNotFoundException if the resume does not exist
     */
    public void hasAccessResume(Long resumeId) {
        String owner = resumeRepository.findCreatedBy(resumeId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", resumeId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "resume", resumeId);
        }
    }

    /**
     * Check if the connected user has access to the section
     * 
     * @param id            the id of the section
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the section
     * @throws EntityNotFoundException if the section does not exist
     */
    public void hasAccessSection(Long sectionId) {
        String owner = sectionRepository.findCreatedBy(sectionId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", sectionId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "section", sectionId);
        }
    }

    /**
     * Check if the connected user has access to the section item
     * 
     * @param id            the id of the section item
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the section item
     * @throws EntityNotFoundException if the section item does not exist
     */
    public void hasAccessSectionItem(Long sectionItemId) {
        String owner = sectionItemRepository.findCreatedBy(sectionItemId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section item", sectionItemId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "section item", sectionItemId);
        }
    }

    /**
     * Check if the connected user has access to the layout
     * 
     * @param id            the id of the layout
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the layout
     * @throws EntityNotFoundException if the layout does not exist
     */
    public void hasAccessLayout(Long layoutId) {
        String owner = layoutRepository.findCreatedBy(layoutId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", layoutId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "layout", layoutId);
        }
    }

    /**
     * Check if the connected user has access to the latex method
     * 
     * @param id            the id of the latex method
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the latex method
     * @throws EntityNotFoundException if the latex method does not exist
     */
    public void hasAccessLatexMethod(Long latexMethodId) {
        String owner = latexMethodRepository.findCreatedBy(latexMethodId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Latex method", latexMethodId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "latex method", latexMethodId);
        }
    }

    /**
     * Check if the connected user has access to the column
     * 
     * @param id            the id of the column
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the column
     * @throws EntityNotFoundException if the column does not exist
     */
    public void hasAccessColumn(Long columnId) {
        String owner = columnRepository.findCreatedBy(columnId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", columnId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "column", columnId);
        }
    }

    /**
     * Check if the connected user has access to the column section
     * 
     * @param id            the id of the column section
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the column section
     * @throws EntityNotFoundException if the column section does not exist
     */
    public void hasAccessColumnSection(Long columnSectionId) {
        String owner = columnSectionRepository.findCreatedBy(columnSectionId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column section", columnSectionId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "column section", columnSectionId);
        }
    }

    /**
     * Check if the connected user has access to the layout section item
     * 
     * @param id            the id of the layout section item
     * @param connectedUser the connected user
     * @throws AccessDeniedException   if the connected user does not have access to
     *                                 the layout section item
     * @throws EntityNotFoundException if the layout section item does not exist
     */
    public void hasAccessLayoutSectionItem(Long layoutSectionItemId) {
        String owner = layoutSectionItemRepository.findCreatedBy(layoutSectionItemId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout section item", layoutSectionItemId));
        if (!hasAccess(List.of(owner))) {
            throw ExceptionUtils.accessDenied(owner, "layout section item", layoutSectionItemId);
        }
    }

}