package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionCreateReq;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionUpdateReq;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemCreateReq;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.MultiParentEntityService;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
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
        ParentEntityService<ColumnSectionCreateReq, ColumnSectionUpdateReq, ColumnSectionResp, Long> {

    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnRepository columnRepository;
    private final SectionRepository sectionRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final LayoutSectionItemService layoutSectionItemService;
    private final LatexMethodService latexMethodService;
    private final ColumnSectionMapper columnSectionMapper;
    private final SecurityUtils securityUtils;
    private OrderableRepositoryUtil orderableRepositoryUtil;

    @Override
    public Long create(ColumnSectionCreateReq request) {
        // Check if the user has access to the column and section
        securityUtils.hasAccessColumn(request.getColumnId());
        securityUtils.hasAccessSection(request.getSectionId());
    
        // Get the column and section and layoutMethodsMap
        Column column = columnRepository.findById(request.getColumnId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", request.getColumnId()));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Section", request.getSectionId()));
        Map<Class<?>, List<LatexMethodResponse>> latexMethodMap = latexMethodService
                .getLatexMethodsMap(column.getLayout().getId());

        if (request.getLatexMethodId() == null) {
            request.setLatexMethodId(latexMethodMap.get(ColumnSection.class).getFirst().getId());
        } 

        // Get the latexMethod
        securityUtils.hasAccessLatexMethod(request.getLatexMethodId());
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", request.getLatexMethodId()));    

        // Check if the column and latexMethod belong to the same layout
        if (!column.getLayout().getId().equals(latexMethod.getLayout().getId())) {
            throw new IllegalArgumentException("The Column and LatexMethod must belong to the same layout");
        }
    
        // Check if the column and section belong to the same resume
        if (column.getLayout().getResume().getId() != section.getResume().getId()) {
            throw new IllegalArgumentException("Column and Section must belong to the same resume");
        }
    
        // Find the maximum sectionOrder in the column
        int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(ColumnSection.class, Column.class,
                column.getId());
    
        int newOrder = request.getSectionOrder() == null ? maxOrder + 1 : request.getSectionOrder();
    
        // Shift the order
        orderableRepositoryUtil.updateItemOrder(ColumnSection.class, Column.class, column.getId(),
                newOrder, maxOrder + 1);
    
        // Create the entity from the request
        request.setSectionOrder(newOrder);
        ColumnSection columnSection = columnSectionMapper.toEntity(request);
    
        // Add the columnSection to the column and section
        column.addSectionMapping(columnSection);
        section.addColumnSection(columnSection);
        if (latexMethod != null) {
            latexMethod.addColumnSection(columnSection);
        }
    
        // Save the columnSection
        long id = columnSectionRepository.save(columnSection).getId();
    
        // Create the default layoutSectionItems
        columnSection.setId(id);
        createDefaultLayoutSectionItems(columnSection, section.getItems(), latexMethodMap);
    
        return id;
    }

    @Override
    public ColumnSectionResp get(Long id) {
        // Check if the user has access to this columnSection
        securityUtils.hasAccessColumnSection(id);
        // Get the existing columnSection entity
        return columnSectionRepository.findById(id)
                .map(columnSectionMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", id));
    }

    @Override
    public void update(ColumnSectionUpdateReq request) {
        // Check if the user has access to this columnSection
        securityUtils.hasAccessColumnSection(request.getId());

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", request.getId()));

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(ColumnSection.class, Column.class, columnSection.getColumn().getId(),
                request.getSectionOrder(), columnSection.getSectionOrder());

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
                maxOrder + 1, columnSection.getSectionOrder());
    }

    @Override
    public List<ColumnSectionResp> getAllByParentId(Long parentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllByParentId'");
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAllByParentId'");
    }

    /*
     * Create the default layoutSectionItems for all the sectionItems in the section
     */
    private void createDefaultLayoutSectionItems(ColumnSection columnSection, List<SectionItem> sectionItems,
            Map<Class<?>, List<LatexMethodResponse>> latexMethodMap) {
        if (sectionItems.isEmpty()) {
            return;
        }

        // Create the layoutSectionItems
        for (SectionItem sectionItem : sectionItems) {
            List<LatexMethodResponse> latexMethods = latexMethodMap.get(sectionItem.getItem().getClass());
            layoutSectionItemService.create(LayoutSectionItemCreateReq.builder()
                    .columnSectionId(columnSection.getId())
                    .sectionItemId(sectionItem.getId())
                    // The first latexMethod will be the default
                    .latexMethodId(latexMethods.isEmpty() ? null : latexMethods.getFirst().getId())
                    .itemOrder(columnSection.isDefaultOrder() ? null : sectionItem.getItemOrder())
                    .build());
        }
    }
}
