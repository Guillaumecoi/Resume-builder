package com.coigniez.resumebuilder.examples;

import com.coigniez.resumebuilder.model.layout.LayoutDTO;
import com.coigniez.resumebuilder.model.layout.LayoutService;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionDTO;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionService;
import com.coigniez.resumebuilder.model.resume.*;
import com.coigniez.resumebuilder.model.section.*;
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.SectionItemType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ResumeExampleService {
    
    private final ResumeService resumeService;
    private final SectionService sectionService;
    private final LayoutService layoutService;
    private final ColumnSectionService columnSectionService;

    public List<Object> createExampleResume(String title) {
        ResumeDetailResponse resume = createBaseResume(title);
        Long layoutId = addLayout(resume.getId());
        LayoutDTO layout = layoutService.get(layoutId);
        
        addEducationSection(resume.getId(), layout.getColumns().get(0).getId(), 1);
        addSummarySection(resume.getId(), layout.getColumns().get(1).getId(), 1);
        addExperienceSection(resume.getId(),layout.getColumns().get(1).getId(), 2);

        return List.of(resumeService.get(resume.getId()), layoutService.get(layoutId));
    }

    private ResumeDetailResponse createBaseResume(String title) {
        ResumeRequest resumeRequest = new ResumeRequest(
            title,
            "John",
            "Doe",
            null
        );
        
        Long resumeId = resumeService.create(null, resumeRequest);
        return resumeService.get(resumeId);
    }

    private void addEducationSection(Long resumeId, Long columnId, int position) {
        List<SectionItemRequest> educationItems = new ArrayList<>();
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 2,
            new HashMap<>() {{
                put("degree", "Bachelor of Science in Computer Science");
                put("institution", "Open Universiteit Utrecht");
                put("period", "2019-2023");
            }}));
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 1,
            new HashMap<>() {{
                put("degree", "Master of Science in Computer Science");
                put("institution", "Open Universiteit Utrecht");
                put("period", "2023-");
            }}));
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 3,
            new HashMap<>() {{
                put("degree", "Science-Mathematics");
                put("institution", "International School of Brussels");
                put("period", "2016");
            }}));
        
        Long educationId = sectionService.create(resumeId, new SectionRequest(null, "Education", educationItems));
        columnSectionService.create(columnId, ColumnSectionDTO.builder()
                .columnId(columnId)
                .sectionId(educationId)
                .position(position)
                .build());

    }

    private void addExperienceSection(Long resumeId, Long columnId, int position) {
        List<SectionItemRequest> experienceItems = new ArrayList<>();
        experienceItems.add(new SectionItemRequest(SectionItemType.WORK_EXPERIENCE.name(), 1,
            new HashMap<>() {{
                put("jobTitle", "Software Engineer");
                put("companyName", "MIVB/STIB");
                put("period", "2021-2022");
                put("description", "Developing software for the public transport company of Brussels");
                put("responsibilities","Developing new features for the website\nMaintaining the existing codebase");
            }}));
            
        Long experienceId = sectionService.create(resumeId, new SectionRequest(null, "Experience", experienceItems));
        columnSectionService.create(columnId, ColumnSectionDTO.builder()
                .columnId(columnId)
                .sectionId(experienceId)
                .position(position)
                .build());
    }

    private void addSummarySection(Long resumeId, Long columnId, int position) {
        List<SectionItemRequest> summaryItems = new ArrayList<>();
        summaryItems.add(new SectionItemRequest(SectionItemType.TEXTBOX.name(), null,
            new HashMap<>() {{
                put("content", """
                    Passionate software developer with a strong foundation in computer science, currently pursuing MSc at Open Universiteit Utrecht. 
                    Specializing in Java development with hands-on experience in Spring Boot and web technologies. 
                    Strong analytical mindset developed through mathematics-focused secondary education.
                    Eager to apply theoretical knowledge in practical software development challenges.""");
            }}));
            
        Long summaryId = sectionService.create(resumeId, new SectionRequest(null, "Summary", summaryItems));
        columnSectionService.create(columnId, ColumnSectionDTO.builder()
                .columnId(columnId)
                .sectionId(summaryId)
                .position(position)
                .build());
    }

    private Long addLayout(Long resumeId) {
        LayoutDTO layoutDTO = LayoutDTO.builder().numberOfColumns(2).build();
        return layoutService.create(resumeId, layoutDTO);
    }
}