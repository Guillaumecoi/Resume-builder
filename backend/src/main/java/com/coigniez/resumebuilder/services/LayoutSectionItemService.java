package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItemMapper;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemCreateReq;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemResp;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.LayoutSectionItemUpdateReq;
import com.coigniez.resumebuilder.domain.layoutsubsection.LayoutSubSection;
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
        ParentEntityService<LayoutSectionItemCreateReq, LayoutSectionItemUpdateReq, LayoutSectionItemResp, Long> {

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
    public Long create(LayoutSectionItemCreateReq request) {
        // Check if the user has access to the ColumnSection and SectionItem
        securityUtils.hasAccessColumnSection(request.getColumnSectionId());
        securityUtils.hasAccessSectionItem(request.getSectionItemId());

        // TODO: Implement the create method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public LayoutSectionItemResp get(Long id) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(id);
        // Get the existing LayoutSectionItem entity
        return layoutSectionItemRepository.findById(id)
                .map(layoutSectionItemMaper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LayoutSectionItem", id));
    }

    @Override
    public void update(LayoutSectionItemUpdateReq request) {
        // Check if the user has access to the SectionItem
        securityUtils.hasAccessLayoutSectionItem(request.getId());

        // Get the existing LayoutSectionItem entity
        LayoutSectionItem layoutSectionItem = layoutSectionItemRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LayoutSectionItem", request.getId()));

        // Shift the order
        orderableRepositoryUtil.updateItemOrder(LayoutSectionItem.class, LayoutSubSection.class,
                layoutSectionItem.getLayoutSubSection().getId(), "itemOrder", request.getItemOrder(),
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

        // delete the LayoutSectionItem
        layoutSectionItemRepository.delete(layoutSectionItem);

        // shift the order of the items
        int maxOrder = orderableRepositoryUtil.findMaxItemOrderByParentId(LayoutSectionItem.class,
                LayoutSubSection.class, layoutSectionItem.getLayoutSubSection(), "itemOrder");
        orderableRepositoryUtil.updateItemOrder(LayoutSectionItem.class, ColumnSection.class,
                layoutSectionItem.getLayoutSubSection(), "itemOrder",
                maxOrder + 1, layoutSectionItem.getItemOrder());

    }

    @Override
    public List<LayoutSectionItemResp> getAllByParentId(Long parentId) {
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
