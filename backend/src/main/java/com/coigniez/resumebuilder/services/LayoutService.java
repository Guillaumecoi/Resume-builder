package com.coigniez.resumebuilder.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutMapper;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.latex.generators.LatexDocumentGenerator;
import com.coigniez.resumebuilder.templates.LayoutTemplate;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class LayoutService implements CrudService<LayoutResponse, LayoutRequest> {

    private final LayoutRepository layoutRepository;
    private final ResumeRepository resumeRepository;
    private final LayoutMapper layoutMapper;
    private final ColumnMapper columnMapper;
    private final LatexDocumentGenerator latexDocumentGenerator;

    //TODO: Authentication
    
    public Long create(LayoutRequest request) {
        Layout layout = layoutMapper.toEntity(request);
        Resume resume = resumeRepository.findById(request.getResumeId()).orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        resume.addLayout(layout);

        if(layout.getColumns().isEmpty()) {
            List<ColumnRequest> columnRequests = LayoutTemplate.getDefaultColumns(layout.getNumberOfColumns());
            List<Column> mappedColumns = columnRequests.stream()
                .map(columnMapper::toEntity)
                .collect(Collectors.toList());
            layout.setColumns(mappedColumns);
        }

        for(LatexMethod method : layout.getLatexMethods()) {
            method.setLayout(layout);
        }

        return layoutRepository.save(layout).getId();
    }

    public LayoutResponse get(long id) {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return layoutMapper.toDto(layout);
    }

    public void update(LayoutRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Layout id is required for update");
        }
        long id = request.getId();

        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        // UpexistingLayoutdate the entity
        layoutMapper.updateEntity(layout, request);
        // Save the updated entity
        layoutRepository.save(layout);        
    }

    public void delete(long id) {
        layoutRepository.deleteById(id);
    }

    /**
     * Generate a PDF file from a layout
     * 
     * @param id layout id to generate the PDF from
     * @return the generated PDF file
     */
    public File generateLatexPdf(long id) throws IOException, InterruptedException {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return latexDocumentGenerator.generateFile(layout, layout.getResume().getTitle());
    }

    public Map<String, Long> getLatexMethodsMap(Long id) {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return layout.getLatexMethods().stream()
            .collect(Collectors.toMap(LatexMethod::getName, LatexMethod::getId));
    }
}
