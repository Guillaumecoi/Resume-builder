package com.coigniez.resumebuilder.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodCreateReq;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodUpdateReq;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.LatexMethodRepository;
import com.coigniez.resumebuilder.repository.LayoutRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LatexMethodService implements ParentEntityService<LatexMethodCreateReq, LatexMethodUpdateReq, LatexMethodResp, Long> {

    private final LayoutRepository layoutRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final LatexMethodMapper latexMethodMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(LatexMethodCreateReq request) {
        // Check if the user has access to the layout
        securityUtils.hasAccessLayout(request.getLayoutId());

        // Create the entity
        LatexMethod latexMethod = latexMethodMapper.toEntity(request);
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", request.getLayoutId()))
                .addLatexMethod(latexMethod);

        // Save the entity
        return latexMethodRepository.save(latexMethod).getId();
    }

    @Override
    public LatexMethodResp get(Long id) {
        // Check if the user has access to the method
        securityUtils.hasAccessLatexMethod(id);

        // Get the entity
        return latexMethodRepository.findById(id)
                .map(latexMethodMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", id));
    }

    @Override
    public void update(LatexMethodUpdateReq request) {
        // Check if the user has access to the method
        securityUtils.hasAccessLatexMethod(request.getId());

        // Update the entity
        LatexMethod latexMethod = latexMethodRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", request.getId()));

        latexMethodMapper.updateEntity(latexMethod, request);

        // Save the entity
        latexMethodRepository.save(latexMethod);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the method
        securityUtils.hasAccessLatexMethod(id);

        // Remove the method from the layout
        LatexMethod latexMethod = latexMethodRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("LatexMethod", id));
        Optional.ofNullable(latexMethod.getLayout()).ifPresent(layout -> layout.removeLatexMethod(latexMethod));

        // Delete the method
        latexMethodRepository.deleteById(id);
    }

    @Override
    public List<LatexMethodResp> getAllByParentId(Long layoutId) {
        // Check if the user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        // Get all methods for the layout
        return latexMethodRepository.findAllByLayoutId(layoutId).stream()
                .map(latexMethodMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long layoutId) {
        // Check if the user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        // Remove all methods from the layout
        layoutRepository.findById(layoutId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", layoutId))
                .clearLatexMethods();
        
        // Delete all methods
        latexMethodRepository.deleteAllByLayoutId(layoutId);
    }

    /**
     * Get all latex methods for a layout
     * 
     * @param id layout id to get the latex methods from
     * @return a map of latex methods grouped by class
     */
    public Map<Class<?>, List<LatexMethodResp>> getLatexMethodsMap(Long id) {
        return latexMethodRepository.findAllByLayoutId(id).stream()
                .map(latexMethodMapper::toDto)
                .filter(latexMethod -> latexMethod.getType() != null && latexMethod.getType().getDataType() != null) // TODO: Handle Section?
                .collect(Collectors.groupingBy(
                    latexMethod -> latexMethod.getType().getDataType(),
                    HashMap::new,
                    Collectors.toList()
                ));
    }
}
