package com.coigniez.resumebuilder.examples;

import com.coigniez.resumebuilder.model.resume.*;
import com.coigniez.resumebuilder.model.section.*;
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.SectionItemType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeExampleService {
    
    private final ResumeService resumeService;
    private final SectionService sectionService;

    public ResumeDetailResponse createExampleResume(String title) {
        ResumeDetailResponse resume = createBaseResume(title);
        
        addEducationSection(resume.getId());
        addExperienceSection(resume.getId());
        addSummarySection(resume.getId());

        return resumeService.get(resume.getId());
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

    private void addEducationSection(Long resumeId) {
        List<SectionItemRequest> educationItems = new ArrayList<>();
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 2,
            new HashMap<>() {{
                put("degree", "Bachelor of Science in Computer Science");
                put("institution", "Open Universiteit Utrecht");
                put("startDate", LocalDate.of(2019, 9, 1));
                put("endDate", LocalDate.of(2023, 7, 1));
            }}));
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 1,
            new HashMap<>() {{
                put("degree", "Master of Science in Computer Science");
                put("institution", "Open Universiteit Utrecht");
                put("startDate", LocalDate.of(2023, 9, 1));
            }}));
        educationItems.add(new SectionItemRequest(SectionItemType.EDUCATION.name(), 3,
            new HashMap<>() {{
                put("degree", "Science-Mathematics");
                put("institution", "International School of Brussels");
                put("endDate", LocalDate.of(2019, 7, 1));
            }}));
        
        sectionService.create(resumeId, new SectionRequest(null, "Education", educationItems));
    }

    private void addExperienceSection(Long resumeId) {
        List<SectionItemRequest> experienceItems = new ArrayList<>();
        experienceItems.add(new SectionItemRequest(SectionItemType.WORK_EXPERIENCE.name(), 1,
            new HashMap<>() {{
                put("jobTitle", "Software Engineer");
                put("companyName", "MIVB/STIB");
                put("startDate", LocalDate.of(2023, 9, 1));
                put("Description", "Developing software for the public transport company of Brussels");
                put("responsibilities","Developing new features for the website \n Maintaining the existing codebase");
            }}));
            
        sectionService.create(resumeId, new SectionRequest(null, "Experience", experienceItems));
    }

    private void addSummarySection(Long resumeId) {
        List<SectionItemRequest> summaryItems = new ArrayList<>();
        summaryItems.add(new SectionItemRequest(SectionItemType.TEXTBOX.name(), null,
            new HashMap<>() {{
                put("content", """
                    Passionate software developer with a strong foundation in computer science, currently pursuing MSc at Open Universiteit Utrecht. 
                    Specializing in Java development with hands-on experience in Spring Boot and web technologies. 
                    Strong analytical mindset developed through mathematics-focused secondary education.
                    Eager to apply theoretical knowledge in practical software development challenges.
                    """);
            }}));
            
        sectionService.create(resumeId, new SectionRequest(null, "Summary", summaryItems));
    }
}