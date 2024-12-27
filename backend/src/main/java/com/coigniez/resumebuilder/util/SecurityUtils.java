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
     * @throws AccessDeniedException if the current user does not have access
     */
    public void hasAccess(@NonNull List<String> usernames) {
        String currentUsername = getUserName();
        if (!usernames.contains(currentUsername)) {
            throw new AccessDeniedException("Access denied");
        }
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
                .orElseThrow(() -> new EntityNotFoundException("Resume with id " + resumeId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Section with id " + sectionId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Section item with id " + sectionItemId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Layout with id " + layoutId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Latex method with id " + latexMethodId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Column with id " + columnId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Column section with id " + columnSectionId + " not found"));
        hasAccess(List.of(owner));
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
                .orElseThrow(() -> new EntityNotFoundException("Layout section item with id " + layoutSectionItemId + " not found"));
        hasAccess(List.of(owner));
    }

}