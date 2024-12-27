package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItemMapper;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemResponse;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.UpdateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.repository.LatexMethodRepository;
import com.coigniez.resumebuilder.repository.LayoutSectionItemRepository;
import com.coigniez.resumebuilder.repository.SectionItemRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.OrderableRepositoryUtil;
import com.coigniez.resumebuilder.util.ParentRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LayoutSectionItemService implements
        ParentEntityService<CreateLayoutSectionItemRequest, UpdateLayoutSectionItemRequest, LayoutSectionItemResponse, Long> {

    @Autowired
    private LayoutSectionItemRepository layoutSectionItemRepository;
    @Autowired
    private ColumnSectionRepository columnSectionRepository;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private LatexMethodRepository latexMethodRepository;
    @Autowired
    private LayoutSectionItemMapper layoutSectionItemMaper;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private OrderableRepositoryUtil orderableRepositoryUtil;
    @Autowired
    private ParentRepositoryUtil parentRepositoryUtil;

    @Override
    public Long create(CreateLayoutSectionItemRequest request) {
        // Check if the user has access to the ColumnSection, SectionItem and
        // LatexMethod
        securityUtils.hasAccessColumnSection(request.getColumnSectionId());
        securityUtils.hasAccessSectionItem(request.getSectionItemId());
        securityUtils.hasAccessLatexMethod(request.getLatexMethodId());

        // Get the ColumnSection, SectionItem and LatexMethod
        ColumnSection columnSection = columnSectionRepository.findById(request.getColumnSectionId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection",
                        request.getColumnSectionId()));
        SectionItem sectionItem = sectionItemRepository.findById(request.getSectionItemId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("SectionItem",
                        request.getSectionItemId()));
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod",
                        request.getLatexMethodId()));

        // Check if the columnSection, sectionItem and latexMethod belong to the same
        // layout/resume
        if (!columnSection.getColumn().getLayout().getId().equals(latexMethod.getLayout().getId())) {
            throw new IllegalArgumentException(
                    "The ColumnSection and LatexMethod must belong to the same layout");
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
            int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(LayoutSectionItem.class,
                    ColumnSection.class, request.getColumnSectionId());
            order = request.getItemOrder() == null ? maxOrder + 1 : request.getItemOrder();

            // shift the order of the items
            orderableRepositoryUtil.updateItemOrder(LayoutSectionItem.class, ColumnSection.class,
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
    public LayoutSectionItemResponse get(Long id) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(id);
        // Get the existing LayoutSectionItem entity
        return layoutSectionItemRepository.findById(id)
                .map(layoutSectionItemMaper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LayoutSectionItem", id));
    }

    @Override
    public void update(UpdateLayoutSectionItemRequest request) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(request.getId());

        // Get the existing LayoutSectionItem entity
        LayoutSectionItem layoutSectionItem = layoutSectionItemRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LayoutSectionItem", request.getId()));

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(LayoutSectionItem.class, ColumnSection.class,
                layoutSectionItem.getColumnSection().getId(), request.getItemOrder(),
                layoutSectionItem.getItemOrder());

        // Update the latexMethod
        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod",
                        request.getLatexMethodId()));
        layoutSectionItem.setLatexMethod(latexMethod);

        // Update the LayoutSectionItem
        layoutSectionItemMaper.updateEntity(layoutSectionItem, request);

        // Save the updated entity
        layoutSectionItemRepository.save(layoutSectionItem);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(id);

        // Get the existing LayoutSectionItem entity
        LayoutSectionItem layoutSectionItem = layoutSectionItemRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LayoutSectionItem", id));

        // Remove the LayoutSectionItem from the ColumnSection, SectionItem and
        // LatexMethod
        ColumnSection columnSection = layoutSectionItem.getColumnSection();
        columnSection.removeLayoutSectionItem(layoutSectionItem);

        // delete the LayoutSectionItem
        layoutSectionItemRepository.delete(layoutSectionItem);

        // shift the order of the items
        int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(LayoutSectionItem.class,
                ColumnSection.class,
                columnSection.getId());
        orderableRepositoryUtil.updateItemOrder(LayoutSectionItem.class, ColumnSection.class,
                columnSection.getId(),
                maxOrder + 1, layoutSectionItem.getItemOrder());

    }

    @Override
    public List<LayoutSectionItemResponse> getAllByParentId(Long parentId) {
        // Check if the user has access to the ColumnSection
        securityUtils.hasAccessColumnSection(parentId);
        // Get all LayoutSectionItems from the ColumnSection
        return parentRepositoryUtil.findAllByParentId(LayoutSectionItem.class, ColumnSection.class, parentId)
                .stream()
                .map(layoutSectionItemMaper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // Check if the user has access to the ColumnSection
        securityUtils.hasAccessColumnSection(parentId);
        // Remove all LayoutSectionItems from the ColumnSection
        parentRepositoryUtil.removeAllByParentId(LayoutSectionItem.class, ColumnSection.class, parentId);
    }
}
