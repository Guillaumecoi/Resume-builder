package com.coigniez.resumebuilder.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutMapper;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutUpdateReq;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.latex.generators.LatexDocumentGenerator;
import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.repository.LayoutRepository;
import com.coigniez.resumebuilder.repository.ResumeRepository;
import com.coigniez.resumebuilder.templates.LayoutTemplates;
import com.coigniez.resumebuilder.templates.layouts.LayoutTemplate;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutService implements ParentEntityService<LayoutCreateReq, LayoutUpdateReq, LayoutResp, Long> {

    private final LayoutRepository layoutRepository;
    private final ResumeRepository resumeRepository;
    private final ColumnRepository columnRepository;
    private final ColumnService columnService;
    private final LatexMethodService latexService;
    private final LayoutMapper layoutMapper;
    private final LatexDocumentGenerator latexDocumentGenerator;
    private final SecurityUtils securityUtils;
    private final LayoutTemplates layoutTemplates;
    
    @Override
    public Long create(LayoutCreateReq request) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(request.getResumeId());

        // Create the entity
        Layout layout = layoutMapper.toEntity(request);
        // Add the layout to the resume
        resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Resume", request.getResumeId()))
                .addLayout(layout);

        // Save the entity
        return layoutRepository.save(layout).getId();
    }

    @Override
    public LayoutResp get(Long id) {
        // Check if the connected user has access to the layout
        securityUtils.hasAccessLayout(id);
        // Get the layout
        return layoutRepository.findById(id)
                .map(layoutMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", id));
    }

    @Override
    public void update(LayoutUpdateReq request) {
        // Check if the connected user has access to the layout
        securityUtils.hasAccessLayout(request.getId());

        // UpexistingLayoutdate the entity
        Layout layout = layoutRepository.findById(request.getId())
            .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", request.getId()));
        layoutMapper.updateEntity(layout, request);
        
        // Save the updated entity
        layoutRepository.save(layout);        
    }

    @Override
    public void delete(Long id) {
        // Check if the connected user has access to the layout
        securityUtils.hasAccessLayout(id);

        // Remove the layout from the resume
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", id));
        layout.getResume().removeLayout(layout);

        // Delete the layout
        layoutRepository.deleteById(id);
    }

    @Override
    public List<LayoutResp> getAllByParentId(Long resumetId) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(resumetId);

        // Get all layouts for the resume
        return layoutRepository.findAllByResumeId(resumetId).stream()
            .map(layoutMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void removeAllByParentId(Long resumetId) {
        // Check if the connected user has access to the resume
        securityUtils.hasAccessResume(resumetId);
        
        // Delete all layouts for the resume
        layoutRepository.deleteAll(layoutRepository.findAllByResumeId(resumetId));
    }

    /**
     * Generate a PDF file from a layout
     * 
     * @param id layout id to generate the PDF from
     * @return the generated PDF file
     */
    public byte[] generateLatexPdf(long id) throws IOException, InterruptedException {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", id));
        return latexDocumentGenerator.generateFile(layout, layout.getResume().getTitle());
    }
    
    /**
     * Get all latex methods for a layout
     * 
     * @param id layout id to get the latex methods from
     * @return a map of latex methods grouped by class
     */
    public Map<Class<?>, List<LatexMethodResp>> getLatexMethodsMap(Long id) {
        return latexService.getLatexMethodsMap(id);
    }


    /**
     * Get all available layout templates
     * 
     * @return a list of layout templates
     */
    public List<LayoutTemplate> getTemplates() {
        return layoutTemplates.getAllLayoutTemplates();
    }
}
