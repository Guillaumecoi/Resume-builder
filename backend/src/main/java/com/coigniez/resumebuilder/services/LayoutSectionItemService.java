package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.LayoutSectionItemRepository;
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
    private SecurityUtils securityUtils;

    @Override
    public Long create(@NotNull CreateLayoutSectionItemRequest request) {
        // Check if the user has access to the ColumnSection, SectionItem and LatexMethod
        securityUtils.hasAccessColumnSection(request.getColumnSectionId());
        securityUtils.hasAccessSectionItem(request.getSectionItemId());
        securityUtils.hasAccessLatexMethod(request.getLatexMethodId());

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
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
