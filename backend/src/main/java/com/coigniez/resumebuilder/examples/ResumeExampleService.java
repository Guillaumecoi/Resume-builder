package com.coigniez.resumebuilder.examples;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.resume.*;
import com.coigniez.resumebuilder.domain.section.*;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.services.ColumnSectionService;
import com.coigniez.resumebuilder.services.LayoutService;
import com.coigniez.resumebuilder.services.ResumeService;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.coigniez.resumebuilder.services.SectionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class ResumeExampleService {
    
    private final ResumeService resumeService;
    private final SectionService sectionService;
    private final SectionItemService sectionItemService;
    private final LayoutService layoutService;
    private final ColumnSectionService columnSectionService;

    public List<Object> createExampleResume(String title) throws IOException {
        ResumeDetailResponse resume = createBaseResume(title);
        Long layoutId = addLayout(resume.getId());
        LayoutResponse layout = layoutService.get(layoutId);
        Map<String, Long> methodIds = layoutService.getLatexMethodsMap(layoutId);
        
        addPictureSection(resume.getId(), layout.getColumns().get(0).getId(), 1, methodIds);
        addContactSection(resume.getId(), layout.getColumns().get(0).getId(), 2, methodIds);
        addEducationSection(resume.getId(), layout.getColumns().get(0).getId(), 3, methodIds);
        addSkillsSection(resume.getId(), layout.getColumns().get(0).getId(), 4, methodIds);
        addTitleSection(resume.getId(), layout.getColumns().get(1).getId(), 1, methodIds);
        addSummarySection(resume.getId(), layout.getColumns().get(1).getId(), 2, methodIds);
        addExperienceSection(resume.getId(),layout.getColumns().get(1).getId(), 3, methodIds);

        return List.of(resumeService.get(resume.getId()), layoutService.get(layoutId));
    }

    private ResumeDetailResponse createBaseResume(String title) {
        ResumeRequest resumeRequest = ResumeRequest.builder().title(title).build();
        
        Long resumeId = resumeService.create(null, resumeRequest);
        return resumeService.get(resumeId);
    }

    private void addPictureSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) throws IOException {
        SectionItemRequest picture = SectionItemRequest.builder()
                .type(SectionItemType.PICTURE.name())
                .data(new HashMap<>() {{
                        put("caption", "Photo by Ali Mammadli on Unsplash");
                        put("width", 0.9);
                        put("height", 1.1);
                        put("shadow", 2.0);
                        put("zoom", 1.8);
                        put("yoffset", -8.0);
                }})
                .latexMethodId(methodIds.get("pictureitem"))
                .build();

        Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
        byte[] content = Files.readAllBytes(resourcePath);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "ali-mammadli-unsplash.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content
        );
            
        Long sectionId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Picture")
                .showTitle(false)
                .build());
        
        sectionItemService.createPicture(sectionId, file, picture);
        
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .position(position)
                .build());
    }

    private void addContactSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        List<SectionItemRequest> contactItems = new ArrayList<>();
        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faMapMarker");
                    put("label", "Brussels, Belgium");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());
    
        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faEnvelope");
                    put("label", "john@email.com");
                    put("link", "mailto:john@email.com");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());

        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faPhone");
                    put("label", "+32 123 456 789");
                    put("link", "tel:+32123456789");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());

        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faLinkedin");
                    put("label", "johndoe");
                    put("link", "https://linkedin.com/in/johndoe");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());

        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faGithub");
                    put("label", "johndoe");
                    put("link", "https://github.com/johndoe");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());

        contactItems.add(SectionItemRequest.builder()
                .type(SectionItemType.CONTACT.name())
                .data(new HashMap<>() {{
                    put("icon", "\\faCar");
                    put("label", "Driving License B");
                }})
                .latexMethodId(methodIds.get("contactitem"))
                .build());

        Long contactId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Contact")
                .showTitle(true)
                .sectionItems(contactItems)
                .build());

        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(contactId)
                .position(position)
                .itemsep(4.0)
                .build());
        }


    private void addEducationSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        List<SectionItemRequest> educationItems = new ArrayList<>();
        educationItems.add(SectionItemRequest.builder()
                .type(SectionItemType.EDUCATION.name())
                .itemOrder(2)
                .data(new HashMap<>() {{
                    put("degree", "Bachelor of Science in Computer Science");
                    put("institution", "Open Universiteit Utrecht");
                    put("period", "2019 - 2023");
                }})
                .latexMethodId(methodIds.get("educationitem"))
                .build());

        educationItems.add(SectionItemRequest.builder()
                .type(SectionItemType.EDUCATION.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("degree", "Master of Science in Computer Science");
                    put("institution", "Open Universiteit Utrecht");
                    put("period", "2023 -");
                }})
                .latexMethodId(methodIds.get("educationitem"))
                .build());
        
        educationItems.add(SectionItemRequest.builder()
                .type(SectionItemType.EDUCATION.name())
                .itemOrder(3)
                .data(new HashMap<>() {{
                    put("degree", "Science-Mathematics");
                    put("institution", "International School of Brussels");
                    put("period", "2016");
                }})
                .latexMethodId(methodIds.get("educationitem"))
                .build());
        
        Long educationId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Education")
                .showTitle(true)
                .sectionItems(educationItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(educationId)
                .position(position)
                .build());

    }

    private void addExperienceSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        List<SectionItemRequest> experienceItems = new ArrayList<>();
        experienceItems.add(SectionItemRequest.builder()
                .type(SectionItemType.WORK_EXPERIENCE.name())
                .itemOrder(1)
                .data(new HashMap<>() {{
                    put("jobTitle", "Software Engineer");
                    put("companyName", "MIVB/STIB");
                    put("period", "2021 - 2022");
                    put("description", "Developing software for the public transport company of Brussels");
                    put("responsibilities","Developing new features for the website\nMaintaining the existing codebase");
                }})
                .latexMethodId(methodIds.get("experienceitem"))
                .build());
            
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

    private void addTitleSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        SectionItemRequest title = SectionItemRequest.builder()
                .type(SectionItemType.TITLE.name())
                .data(new HashMap<>() {{
                    put("title", "John Doe");
                    put("subtitle", "Software Developer");
                }})
                .latexMethodId(methodIds.get("cvtitle"))
                .build();
            
        Long titleId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Title")
                .showTitle(false)
                .sectionItems(List.of(title))
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(titleId)
                .position(position)
                .endsep(6)
                .build());
    }

    private void addSummarySection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        List<SectionItemRequest> summaryItems = new ArrayList<>();
        summaryItems.add(SectionItemRequest.builder()
                .type(SectionItemType.TEXTBOX.name())
                .data(new HashMap<>() {{
                    put("content", """
                        Passionate software developer with a strong foundation in computer science, currently pursuing MSc at Open Universiteit Utrecht. 
                        Specializing in Java development with hands-on experience in Spring Boot and web technologies. 
                        Strong analytical mindset developed through mathematics-focused secondary education.
                        Eager to apply theoretical knowledge in practical software development challenges.""");
                }})
                .latexMethodId(methodIds.get("textbox"))
                .build());
            
        Long summaryId = sectionService.create(resumeId, SectionRequest.builder()
                .title("About me")
                .sectionItems(summaryItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(summaryId)
                .position(position)
                .build());
    }

    private void addSkillsSection(Long resumeId, Long columnId, int position, Map<String, Long> methodIds) {
        List<SectionItemRequest> skillsItems = new ArrayList<>();
        skillsItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL_BOXES.name())
                .data(new HashMap<>() {{
                    put("skills", "Java, Spring Boot, HTML, CSS, JavaScript, SQL, Python, C, C++");
                }})
                .latexMethodId(methodIds.get("skillboxes"))
                .build());

        skillsItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .data(new HashMap<>() {{
                    put("name", "Dutch");
                }})
                .latexMethodId(methodIds.get("skillitem"))
                .build());

        skillsItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .data(new HashMap<>() {{
                    put("name", "French");
                    put("description", "Intermediate");
                }})
                .latexMethodId(methodIds.get("skilltext"))
                .build());

        skillsItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .data(new HashMap<>() {{
                    put("name", "English");
                    put("proficiency", 9);
                }})
                .latexMethodId(methodIds.get("skillbullets"))
                .build());

        skillsItems.add(SectionItemRequest.builder()
                .type(SectionItemType.SKILL.name())
                .data(new HashMap<>() {{
                    put("name", "Japanese");
                    put("proficiency", 6);
                }})
                .latexMethodId(methodIds.get("skillbar"))
                .build());
            
        Long skillsId = sectionService.create(resumeId, SectionRequest.builder()
                .title("Skills")
                .sectionItems(skillsItems)
                .build());
        columnSectionService.create(columnId, ColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(skillsId)
                .position(position)
                .itemsep(4.0)
                .build());
    }

    private Long addLayout(Long resumeId) {
        LayoutRequest layoutDTO = LayoutRequest.builder().numberOfColumns(2).build();
        return layoutService.create(resumeId, layoutDTO);
    }
}