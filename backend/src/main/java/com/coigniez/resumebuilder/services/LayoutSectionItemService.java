package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItemMaper;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.repository.LatexMethodRepository;
import com.coigniez.resumebuilder.repository.LayoutSectionItemRepository;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.OrderableRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LayoutSectionItemService implements
        ParentEntityService<CreateLayoutSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse, Long> {

    @Autowired
    private LayoutSectionItemRepository layoutSectionItemRepository;
    @Autowired
    private ColumnSectionRepository columnSectionRepository;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private LatexMethodRepository latexMethodRepository;
    @Autowired
    private LayoutSectionItemMaper layoutSectionItemMaper;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private OrderableRepositoryUtil orderableRepositoryUtil;

    @Override
    public Long create(@NotNull CreateLayoutSectionItemRequest request) {
        // Check if the user has access to the ColumnSection, SectionItem and
        // LatexMethod
        securityUtils.hasAccessColumnSection(request.getColumnSectionId());
        securityUtils.hasAccessSectionItem(request.getSectionItemId());
        securityUtils.hasAccessLatexMethod(request.getLatexMethodId());

        // Get the ColumnSection, SectionItem and LatexMethod
        ColumnSection columnSection = columnSectionRepository.findById(request.getColumnSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", request.getColumnSectionId()));
        SectionItem sectionItem = sectionItemRepository.findById(request.getSectionItemId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem", request.getSectionItemId()));
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", request.getLatexMethodId()));

        // Check if the columnSection, sectionItem and latexMethod belong to the same
        // layout/resume
        if (!columnSection.getColumn().getLayout().getId().equals(latexMethod.getLayout().getId())) {
            throw new IllegalArgumentException("The ColumnSection and LatexMethod must belong to the same layout");
        }
        if (!latexMethod.getLayout().getResume().getId().equals(sectionItem.getSection().getResume().getId())) {
            throw new IllegalArgumentException(
                    "The SectionItem must belong to the same resume as the LatexMethod and ColumnSection");
        }

        Integer order;
        // set the order
        if (columnSection.isDefaultOrder()) {
            order = null;
        } else {
            int maxOrder = orderableRepositoryUtil
                    .findMaxItemOrderByParentId(LayoutSectionItem.class, ColumnSection.class,
                            request.getColumnSectionId())
                    .orElse(0);
            order = request.getItemOrder() == null ? maxOrder + 1 : request.getItemOrder();

            // shift the order of the items
            orderableRepositoryUtil.incrementItemOrderBetween(LayoutSectionItem.class, ColumnSection.class,
                    request.getColumnSectionId(), order, maxOrder + 1);
        }
        // Create the LayoutSectionItem
        request.setItemOrder(order);
        LayoutSectionItem layoutSectionItem = layoutSectionItemMaper.toEntity(request);
        layoutSectionItem.setColumnSection(columnSection);
        layoutSectionItem.setSectionItem(sectionItem);
        layoutSectionItem.setLatexMethod(latexMethod);
        // Save the LayoutSectionItem
        return layoutSectionItemRepository.save(layoutSectionItem).getId();
    }

    @Override
    public SectionItemResponse get(@NotNull Long id) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(id);

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public void update(@NotNull UpdateSectionItemRequest request) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(request.getId());
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(@NotNull Long id) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(id);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<SectionItemResponse> getAllByParentId(Long parentId) {
        // Check if the user has access to the ColumnSection
        securityUtils.hasAccessColumnSection(parentId);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllByParentId'");
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // Check if the user has access to the ColumnSection
        securityUtils.hasAccessColumnSection(parentId);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAllByParentId'");
    }
}
