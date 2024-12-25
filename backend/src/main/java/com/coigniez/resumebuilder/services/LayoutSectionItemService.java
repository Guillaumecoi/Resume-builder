package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LayoutSectionItemService implements
        ParentEntityService<CreateLayoutSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse, Long> {

    @Override
    public Long create(@NotNull CreateLayoutSectionItemRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public SectionItemResponse get(@NotNull Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public void update(@NotNull UpdateSectionItemRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(@NotNull Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<SectionItemResponse> getAllByParentId(Long parentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllByParentId'");
    }

    @Override
    public void removeAllByParentId(Long parentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAllByParentId'");
    }

    private void hasAccess(Long sectionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccessSection'");
    }

    private void hasAccessColumnSection(Long columnSectionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccessColumnSection'");
    }

    private void hasAccessSectionItem(Long sectionItemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccessSectionItem'");
    }

    private void hasAccessLatexMethod(Long latexMethodId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasAccessLatexMethod'");
    }
}
