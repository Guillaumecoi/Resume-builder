package com.coigniez.resumebuilder.examples;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionService;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutService;
import com.coigniez.resumebuilder.domain.resume.*;
import com.coigniez.resumebuilder.domain.section.*;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;

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
        LayoutResponse layout = layoutService.get(layoutId);
        
        addEducationSection(resume.getId(), layout.getColumns().get(0).getId(), 1);
        addSkillsSection(resume.getId(), layout.getColumns().get(0).getId(), 2);
        addSummarySection(resume.getId(), layout.getColumns().get(1).getId(), 1);
        addExperienceSection(resume.getId(),layout.getColumns().get(1).getId(), 2);

        return List.of(resumeService.get(resume.getId()), layoutService.get(layoutId));
    }

    private ResumeDetailResponse createBaseResume(String title) {
        ResumeRequest resumeRequest = ResumeRequest.builder().title(title).build();
        
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
        
        Long educationId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Education")
                .sectionItems(educationItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
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
            
        Long experienceId = sectionService.create(resumeId, SectionRequest.builder()
            .title("Experience")
            .sectionItems(experienceItems)
            .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
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
            
        Long summaryId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Summary")
                .sectionItems(summaryItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(summaryId)
                .position(position)
                .build());
    }

    private void addSkillsSection(Long resumeId, Long columnId, int position) {
        List<SectionItemRequest> skillsItems = new ArrayList<>();
        skillsItems.add(new SectionItemRequest(SectionItemType.SKILL_BOXES.name(), null,
            new HashMap<>() {{
                put("skills", """
                    Java
                    Spring Boot
                    HTML
                    CSS
                    JavaScript
                    SQL
                    Python
                    C
                    C++
                    """);
            }}));
        skillsItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), null,
            new HashMap<>() {{
                put("name", "Dutch");
                put("type", Skill.SkillType.SIMPLE.name());
            }}));
        skillsItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), null,
            new HashMap<>() {{
                put("name", "French");
                put("description", "Intermediate");
                put("type", Skill.SkillType.TEXT.name());
            }}));
        skillsItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), null,
            new HashMap<>() {{
                put("name", "English");
                put("proficiency", 9);
                put("type", Skill.SkillType.BAR.name());
            }}));
        skillsItems.add(new SectionItemRequest(SectionItemType.SKILL.name(), null,
            new HashMap<>() {{
                put("name", "Japanese");
                put("proficiency", 6);
                put("type", Skill.SkillType.BULLETS.name());
            }}));
            
        Long skillsId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Skills")
                .sectionItems(skillsItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(skillsId)
                .position(position)
                .build());
    }

    private Long addLayout(Long resumeId) {
        LayoutRequest layoutDTO = LayoutRequest.builder().numberOfColumns(2).build();
        return layoutService.create(resumeId, layoutDTO);
    }
}