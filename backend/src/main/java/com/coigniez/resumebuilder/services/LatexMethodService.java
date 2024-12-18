package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRepository;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LatexMethodService implements ParentEntityService<LatexMethodRequest, LatexMethodResponse, Long> {

    private final LayoutRepository layoutRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final LatexMethodMapper latexMethodMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(LatexMethodRequest request) {
        // Check if the user has access to the layout
        hasAccessLayout(request.getLayoutId());

        // Create the entity
        request.setId(null);
        LatexMethod latexMethod = latexMethodMapper.toEntity(request);
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"))
                .addLatexMethod(latexMethod);

        // Save the entity
        return latexMethodRepository.save(latexMethod).getId();
    }

    @Override
    public LatexMethodResponse get(Long id) {
        // Check if the user has access to the method
        hasAccess(id);

        // Get the entity
        return latexMethodRepository.findById(id)
                .map(latexMethodMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
    }

    @Override
    public void update(LatexMethodRequest request) {
        // Check if the user has access to the method
        hasAccess(request.getId());

        // Update the entity
        LatexMethod latexMethod = latexMethodRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));

        latexMethodMapper.updateEntity(latexMethod, request);

        // Save the entity
        latexMethodRepository.save(latexMethod);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the method
        hasAccess(id);

        // Remove the method from the layout
        LatexMethod latexMethod = latexMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        Optional.ofNullable(latexMethod.getLayout()).ifPresent(layout -> layout.removeLatexMethod(latexMethod));

        // Delete the method
        latexMethodRepository.deleteById(id);
    }

    @Override
    public List<LatexMethodResponse> getAllByParentId(Long layoutId) {
        // Check if the user has access to the layout
        hasAccessLayout(layoutId);

        // Get all methods for the layout
        return latexMethodRepository.findAllByLayoutId(layoutId).stream()
                .map(latexMethodMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long layoutId) {
        // Check if the user has access to the layout
        hasAccessLayout(layoutId);

        // Remove all methods from the layout
        layoutRepository.findById(layoutId)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"))
                .clearLatexMethods();
        
        // Delete all methods
        latexMethodRepository.deleteAllByLayoutId(layoutId);
    }

    /**
     * Check if the user has access to the LatexMethod
     * 
     * @param id The id of the LatexMethod
     */
    private void hasAccess(long id) {
        String owner = latexMethodRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the user has access to the Layout
     * 
     * @param layoutId The id of the Layout
     */
    private void hasAccessLayout(long layoutId) {
        String owner = layoutRepository.findCreatedBy(layoutId)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        securityUtils.hasAccess(List.of(owner));
    }
    
}
