package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionParentType;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.MultiParentEntityService;
import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.repository.LatexMethodRepository;
import com.coigniez.resumebuilder.repository.SectionRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.OrderableRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColumnSectionService implements
        MultiParentEntityService<CreateColumnSectionRequest, UpdateColumnSectionRequest, ColumnSectionResponse, Long, ColumnSectionParentType> {

    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnRepository columnRepository;
    private final SectionRepository sectionRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final ColumnSectionMapper columnSectionMapper;
    private final SecurityUtils securityUtils;
    private OrderableRepositoryUtil orderableRepositoryUtil;

    @Override
    public Long create(CreateColumnSectionRequest request) {
        // Check if the user has access to the column, section and latexMethod
        securityUtils.hasAccessColumn(request.getColumnId());
        securityUtils.hasAccessSection(request.getSectionId());
        securityUtils.hasAccessLatexMethod(request.getLatexMethodId());

        // Get the column and section
        Column column = columnRepository.findById(request.getColumnId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", request.getColumnId()));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getSectionId()));
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", request.getLatexMethodId()));

        // Check if the column and section belong to the same resume
        if (!column.getLayout().getId().equals(latexMethod.getLayout().getId())) {
            throw new IllegalArgumentException("The Column and LatexMethod must belong to the same layout");
        }
        if (column.getLayout().getResume().getId() != section.getResume().getId()) {
            throw new IllegalArgumentException("Column and Section must belong to the same resume");
        }

        // Find the maximum sectionOrder in the column
        int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(ColumnSection.class, Column.class,
                column.getId());

        int newOrder = request.getItemOrder() == null ? maxOrder + 1 : request.getItemOrder();

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(ColumnSection.class, Column.class, column.getId(),
                newOrder, maxOrder + 1);

        // Create the entity from the request
        request.setItemOrder(newOrder);
        ColumnSection columnSection = columnSectionMapper.toEntity(request);

        // Add the columnSection to the column and section
        column.addSectionMapping(columnSection);
        section.addColumnSection(columnSection);
        latexMethod.addColumnSection(columnSection);

        // Save the columnSection
        return columnSectionRepository.save(columnSection).getId();
    }

    @Override
    public ColumnSectionResponse get(Long id) {
        // Check if the user has access to this columnSection
        securityUtils.hasAccessColumnSection(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", id));
        return columnSectionMapper.toDto(columnSection);
    }

    @Override
    public void update(UpdateColumnSectionRequest request) {
        // Check if the user has access to this columnSection
        securityUtils.hasAccessColumnSection(request.getId());

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", request.getId()));

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(ColumnSection.class, Column.class, columnSection.getColumn().getId(),
                request.getItemOrder(), columnSection.getItemOrder());

        // Update the latexMethod
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", request.getLatexMethodId()));
        columnSection.setLatexMethod(latexMethod);

        // Update theexistingColumnSection entity
        columnSectionMapper.updateEntity(columnSection, request);

        // Save the updated entity
        columnSectionRepository.save(columnSection);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to this columnSection
        securityUtils.hasAccessColumnSection(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", id));

        // Remove the columnSection from the column and section
        Column column = columnSection.getColumn();
        Section section = columnSection.getSection();

        column.removeSectionMapping(columnSection);
        section.removeColumnSection(columnSection);

        // Delete the columnSection
        columnSectionRepository.delete(columnSection);

        // Shift other columnSections
        int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(ColumnSection.class, Column.class,
                column.getId());
        orderableRepositoryUtil.updateItemOrder(ColumnSection.class, Column.class, column.getId(), 
                maxOrder + 1, columnSection.getItemOrder());
    }

    @Override
    public List<ColumnSectionResponse> getAllByParentId(ColumnSectionParentType parentType, Long parentId) {
        if (parentType == ColumnSectionParentType.COLUMN) {
            // Check if the user has access to the column
            securityUtils.hasAccessColumn(parentId);

            // Get all columnSections in the column
            return columnSectionRepository.findAllByColumnId(parentId).stream()
                    .map(columnSectionMapper::toDto)
                    .toList();

        } else if (parentType == ColumnSectionParentType.SECTION) {
            // Check if the user has access to the section
            securityUtils.hasAccessSection(parentId);

            // Get all columnSections in the section
            return columnSectionRepository.findAllBySectionId(parentId).stream()
                    .map(columnSectionMapper::toDto)
                    .toList();

        } else {
            throw new UnsupportedOperationException(parentType + " is not supported");
        }
    }

    @Override
    public void removeAllByParentId(ColumnSectionParentType parentType, Long parentId) {
        if (parentType == ColumnSectionParentType.COLUMN) {
            // Check if the user has access to the column
            securityUtils.hasAccessColumn(parentId);

            // Remove all columnSections from the column
            columnRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", parentId))
                    .clearSectionMappings();

            // Remove all columnSections in the column
            columnSectionRepository.removeAllByColumnId(parentId);

        } else if (parentType == ColumnSectionParentType.SECTION) {
            // Check if the user has access to the section
            securityUtils.hasAccessSection(parentId);

            // Remove all columnSections from the section
            sectionRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", parentId))
                    .clearColumnSections();

            // Remove all columnSections in the section
            columnSectionRepository.removeAllBySectionId(parentId);
        } else {
            throw new UnsupportedOperationException(parentType + " is not supported");
        }
    }
}
