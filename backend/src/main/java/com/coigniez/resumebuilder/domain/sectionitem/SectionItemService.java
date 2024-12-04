package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SectionItemService {

    private final SectionItemRepository sectionitemRepository;
    private final SectionRepository sectionRepository;
    private final SectionItemMapper sectionitemMapper;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    public Long create(Section section, SectionItemRequest request) {   
        SectionItem sectionItem = sectionitemMapper.toEntity(request);
    
        // Find the maximum itemOrder in the section
        Integer maxOrder = sectionitemRepository.findMaxItemOrderBySectionId(section.getId());
        int newOrder = request.getItemOrder() == null ? (maxOrder == null ? 1 : maxOrder + 1) : request.getItemOrder();
    
        if (request.getItemOrder() != null) {
            // Shift other items
            sectionitemRepository.incrementItemOrderForSection(section.getId(), newOrder);
        }
    
        sectionItem.setItemOrder(newOrder);
        section.addSectionItem(sectionItem);
    
        return sectionitemRepository.save(sectionItem).getId();
    }

    public Long createPicture(Long sectionId, MultipartFile file, SectionItemRequest request) {
        Section section = sectionRepository.findById(sectionId)
            .orElseThrow(() -> new EntityNotFoundException("Section not found"));
            
        // Create section item first
        Long itemId = create(section, request);
        
        // Then update with file
        SectionItem item = sectionitemRepository.findById(itemId)
            .orElseThrow(() -> new EntityNotFoundException("SectionItem not found"));
            
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        
        Picture picture = (Picture) item.getData();
        picture.setPath(path);
        
        sectionitemRepository.save(item);
        
        return itemId;
    }

    public List<SectionItem> getAll(Long id) {
        return sectionitemRepository.findAllBySectionId(id);
    }
    

    public void deleteAllBySectionId(Long sectionId) {
        sectionitemRepository.deleteAllBySectionId(sectionId);
    }
}