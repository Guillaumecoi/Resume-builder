package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.LayoutColumn;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionCreateReq;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionUpdateReq;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
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
    
        //TODO: Implement the create method
        throw new UnsupportedOperationException("Not implemented yet");
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
        LayoutColumn column = columnSection.getColumn();

        column.removeSectionMapping(columnSection);
        // Delete the columnSection
        columnSectionRepository.delete(columnSection);
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
//     private void createDefaultLayoutSectionItems(ColumnSection columnSection, List<SectionItem> sectionItems,
//             Map<Class<?>, List<LatexMethodResp>> latexMethodMap) {
//         if (sectionItems.isEmpty()) {
//             return;
//         }

//         // Create the layoutSectionItems
//         for (SectionItem sectionItem : sectionItems) {
//             List<LatexMethodResp> latexMethods = latexMethodMap.get(sectionItem.getItem().getClass());
//             layoutSectionItemService.create(LayoutSectionItemCreateReq.builder()
//                     .columnSectionId(columnSection.getId())
//                     .sectionItemId(sectionItem.getId())
//                     // The first latexMethod will be the default
//                     .latexMethodId(latexMethods.isEmpty() ? null : latexMethods.getFirst().getId())
//                     .itemOrder(columnSection.isDefaultOrder() ? null : sectionItem.getItemOrder())
//                     .build());
//         }
//     }
}
