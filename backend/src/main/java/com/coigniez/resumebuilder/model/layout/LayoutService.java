package com.coigniez.resumebuilder.model.layout;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.layout.templates.LayoutTemplate;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.resume.ResumeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutService implements CrudService<LayoutDTO, LayoutDTO> {

    private final LayoutRepository layoutRepository;
    private final ResumeRepository resumeRepository;
    private final LayoutMapper layoutMapper;
    
    public Long create(Long parentId, LayoutDTO request) {
        Layout layout = layoutMapper.toEntity(request);
        Resume resume = resumeRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        layout.setResume(resume);

        if(layout.getColumns().isEmpty()) {
            layout.setColumns(LayoutTemplate.getDefaultColumns(layout.getNumberOfColumns()));
        }
        layout.getColumns().forEach(column -> column.setLayout(layout));
        return layoutRepository.save(layout).getId();
    }

    public Long createDefault(Long parentId, Long numberOfColumns) {
        // Get the default layout based on the number of columns
        Layout layout;
        if (numberOfColumns == 1) {
            layout = LayoutTemplate.getDefaultSingleColumn();
        } else if (numberOfColumns == 2) {
            layout = LayoutTemplate.getDefaultTwoColumn();
        } else {
            throw new IllegalArgumentException("Invalid number of columns");
        }

        Resume resume = resumeRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        layout.setResume(resume);
        return layoutRepository.save(layout).getId();
    }

    public LayoutDTO get(Long id) {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return layoutMapper.toDto(layout);
    }

    public void update(Long id, LayoutDTO request) {
        Layout existingLayout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        Layout updatedLayout = layoutMapper.toEntity(request);

        BeanUtils.copyProperties(updatedLayout, existingLayout, "id", "resume");

        layoutRepository.save(existingLayout);        
    }

    public void delete(Long id) {
        layoutRepository.deleteById(id);
    }

}
