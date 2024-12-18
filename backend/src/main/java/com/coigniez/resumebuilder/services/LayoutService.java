package com.coigniez.resumebuilder.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutMapper;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.resume.ResumeRepository;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.latex.generators.LatexDocumentGenerator;
import com.coigniez.resumebuilder.templates.LayoutTemplate;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutService implements ParentEntityService<LayoutRequest, LayoutResponse, Long> {

    private final LayoutRepository layoutRepository;
    private final ResumeRepository resumeRepository;
    private final LayoutMapper layoutMapper;
    private final ColumnMapper columnMapper;
    private final LatexDocumentGenerator latexDocumentGenerator;
    private final SecurityUtils securityUtils;
    
    @Override
    public Long create(LayoutRequest request) {
        // Check if the connected user has access to the resume
        hasAccessResume(request.getResumeId());

        // Create the entity
        Layout layout = layoutMapper.toEntity(request);
        // Add the layout to the resume
        resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new EntityNotFoundException("Resume not found"))
                .addLayout(layout);

        // Add default columns if none are provided
        if(layout.getColumns().isEmpty()) {
            List<ColumnRequest> columnRequests = LayoutTemplate.getDefaultColumns(layout.getNumberOfColumns());
            columnRequests.stream()
                .map(columnMapper::toEntity)
                .forEach(layout::addColumn);
        }

        // Save the entity
        return layoutRepository.save(layout).getId();
    }

    @Override
    public LayoutResponse get(Long id) {
        // Check if the connected user has access to the layout
        hasAccess(id);
        // Get the layout
        return layoutRepository.findById(id)
                .map(layoutMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
    }

    @Override
    public void update(LayoutRequest request) {
        // Check if the connected user has access to the layout
        hasAccess(request.getId());

        // UpexistingLayoutdate the entity
        Layout layout = layoutRepository.findById(request.getId())
            .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        layoutMapper.updateEntity(layout, request);
        // Save the updated entity
        layoutRepository.save(layout);        
    }

    @Override
    public void delete(Long id) {
        // Check if the connected user has access to the layout
        hasAccess(id);

        // Remove the layout from the resume
        Layout layout = layoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        layout.getResume().removeLayout(layout);

        // Delete the layout
        layoutRepository.deleteById(id);
    }


    @Override
    public List<LayoutResponse> getAllByParentId(Long resumetId) {
        // Check if the connected user has access to the resume
        hasAccessResume(resumetId);

        // Get all layouts for the resume
        return layoutRepository.findAllByResumeId(resumetId).stream()
            .map(layoutMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public void removeAllByParentId(Long resumetId) {
        // Check if the connected user has access to the resume
        hasAccessResume(resumetId);
        
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
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return latexDocumentGenerator.generateFile(layout, layout.getResume().getTitle());
    }

    /**
     * Get the latex methods of a layout
     * 
     * @param id the layout id
     * @return a map of latex methods with their name as key and id as value
     */
    public Map<String, Long> getLatexMethodsMap(Long id) {
        return layoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"))
                .getLatexMethods().stream()
                .collect(Collectors.toMap(LatexMethod::getName, LatexMethod::getId));
    }

    /**
     * Check if the connected user has access to the layout
     * 
     * @param layoutId the layout id
     */
    private void hasAccess(Long layoutId) {
        String owner = layoutRepository.findCreatedBy(layoutId)
            .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the connected user has access to the resume
     * 
     * @param resumeId the resume id
     */
    private void hasAccessResume(Long resumeId) {
        String owner = resumeRepository.findCreatedBy(resumeId)
            .orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        securityUtils.hasAccess(List.of(owner));
    }

}
