package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRepository;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRepository;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionItemService implements CrudService<SectionItemResponse, SectionItemRequest> {

    private final SectionItemRepository sectionitemRepository;
    private final SectionRepository sectionRepository;
    private final LatexMethodRepository latexMethodRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    public Long create(SectionItemRequest request) {   
        Section section = sectionRepository.findById(request.getSectionId())
            .orElseThrow(() -> new EntityNotFoundException("Section not found"));

        SectionItem sectionItem = sectionitemMapper.toEntity(request);
        sectionItem.setId(null);
    
        // Find the maximum itemOrder in the section
        Integer maxOrder = sectionitemRepository.findMaxItemOrderBySectionId(section.getId());
        int newOrder = request.getItemOrder() == null ? (maxOrder == null ? 1 : maxOrder + 1) : request.getItemOrder();
    
        if (request.getItemOrder() != null) {
            // Shift other items
            sectionitemRepository.incrementItemOrderForSection(section.getId(), newOrder);
        }
    
        sectionItem.setItemOrder(newOrder);
        section.addSectionItem(sectionItem);

        LatexMethod latexMethod = latexMethodRepository.findById(request.getLatexMethodId())
            .orElseThrow(() -> new EntityNotFoundException("LatexMethod not found"));
        
        sectionItem.setLatexMethod(latexMethod);
    
        return sectionitemRepository.save(sectionItem).getId();
    }

    public Long createPicture(MultipartFile file, SectionItemRequest request) {
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        Map<String, Object> data = request.getData();
        data.put("path", path);
        request.setData(data);
        
        return create(request);
    }


    @Override
    public SectionItemResponse get(Long id) {
        return sectionitemRepository.findById(id)
            .map(sectionitemMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    }

    @Override
    public void update(Long id, SectionItemRequest request) {
        SectionItem sectionItem = sectionitemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    
        SectionItem updatedSectionItem = sectionitemMapper.toEntity(request);
        updatedSectionItem.setId(sectionItem.getId());
        updatedSectionItem.setSection(sectionItem.getSection());
    
        // Shift other items
        if (!sectionItem.getItemOrder().equals(request.getItemOrder())) {
            sectionitemRepository.incrementItemOrderForSection(sectionItem.getSection().getId(), request.getItemOrder());
            sectionitemRepository.decrementItemOrderForSection(sectionItem.getSection().getId(), sectionItem.getItemOrder());
        }
    
        sectionitemRepository.save(updatedSectionItem);
    }

    @Override
    public void delete(Long id) {
        SectionItem sectionItem = sectionitemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
    
        sectionitemRepository.deleteById(id);
    
        // Shift other items
        sectionitemRepository.decrementItemOrderForSection(sectionItem.getSection().getId(), sectionItem.getItemOrder());
    }

    public List<SectionItem> getAll(Long id) {
        return sectionitemRepository.findAllBySectionId(id);
    }
    
    public void deleteAllBySectionId(Long sectionId) {
        sectionitemRepository.deleteAllBySectionId(sectionId);
    }
}