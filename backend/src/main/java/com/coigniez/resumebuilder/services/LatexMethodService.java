package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRepository;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class LatexMethodService implements CrudService<LatexMethodResponse, LatexMethodRequest> {

    private final LayoutRepository layoutRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final LatexMethodMapper latexMethodMapper;
    private final SecurityUtils securityUtils;

    public Long create(LatexMethodRequest request) {
        hasAccessLayout(request.getLayoutId());

        LatexMethod latexMethod = latexMethodMapper.toEntity(request);
        Layout layout = layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));

        layout.addLatexMethod(latexMethod);

        return latexMethodRepository.save(latexMethod).getId();
    }

    public LatexMethodResponse get(long id) {
        hasAccess(id);

        LatexMethod latexMethod = latexMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        return latexMethodMapper.toDto(latexMethod);
    }

    public void update(LatexMethodRequest request) {
        hasAccess(request.getId());

        if (request.getId() == null) {
            throw new IllegalArgumentException("LatexMethod id is required for update");
        }
        long id = request.getId();

        LatexMethod latexMethod = latexMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        // Update the entity
        latexMethodMapper.updateEntity(latexMethod, request);
    }

    public void delete(long id) {
        hasAccess(id);

        LatexMethod latexMethod = latexMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));

        // Remove the method from the layout
        Layout layout = latexMethod.getLayout();
        layout.removeLatexMethod(latexMethod);
        // Delete the method
        latexMethodRepository.delete(latexMethod);
    }

    private void hasAccess(long id) {
        String owner = latexMethodRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    private void hasAccessLayout(long layoutId) {
        String owner = layoutRepository.findCreatedBy(layoutId)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        securityUtils.hasAccess(List.of(owner));
    }
    
}
