package com.coigniez.resumebuilder.examples;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Experience;
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
        ResumeResp resume = createBaseResume(title);
        Long layoutId = addLayout(resume.getId());
        LayoutResponse layout = layoutService.get(layoutId);
        Map<String, Long> methodIds = null;
        
        addPictureSection(resume.getId(), layout.getColumns().get(0).getId(), 1, methodIds);
        addContactSection(resume.getId(), layout.getColumns().get(0).getId(), 2, methodIds);
        addEducationSection(resume.getId(), layout.getColumns().get(0).getId(), 3, methodIds);
        addSkillsSection(resume.getId(), layout.getColumns().get(0).getId(), 4, methodIds);
        addTitleSection(resume.getId(), layout.getColumns().get(1).getId(), 1, methodIds);
        addSummarySection(resume.getId(), layout.getColumns().get(1).getId(), 2, methodIds);
        addExperienceSection(resume.getId(),layout.getColumns().get(1).getId(), 3, methodIds);

        return List.of(resumeService.get(resume.getId()), layoutService.get(layoutId));
    }

    private ResumeResp createBaseResume(String title) {
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder().title(title).build();
        
        Long resumeId = resumeService.create(resumeRequest);
        return resumeService.get(resumeId);
    }

    private void addPictureSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) throws IOException {
        Long sectionId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Picture")
                .showTitle(false)
                .build());
                
        SectionItemCreateReq pictureRequest = SectionItemCreateReq.builder()
                .sectionId(sectionId)
                .item(Picture.builder()
                        .caption("Photo by Ali Mammadli on Unsplash")
                        .width(0.9)
                        .height(1.1)
                        .shadow(2.0)
                        .zoom(1.8)
                        .yoffset(-8.0)
                        .build())
                .build();

        Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
        byte[] content = Files.readAllBytes(resourcePath);
        
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "ali-mammadli-unsplash.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content
        );
                
        sectionItemService.createPicture(file, pictureRequest);
        
        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .build());
    }

    private void addContactSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        List<SectionItemCreateReq> contactItems = new ArrayList<>();
        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faMapMarker")
                        .label("Brussels, Belgium")
                        .build())
                .build());
    
        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faEnvelope")
                        .label("john@email.com")
                        .link("mailto:john@email.com")
                        .build())
                .build());

        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faPhone")
                        .label("+32 123 456 789")
                        .link("tel:+32123456789")
                        .build())
                .build());

        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faLinkedin")
                        .label("johndoe")
                        .link("https://linkedin.com/in/johndoe")
                        .build())
                .build());

        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faGithub")
                        .label("johndoe")
                        .link("https://github.com/johndoe")
                        .build())
                .build());

        contactItems.add(SectionItemCreateReq.builder()
                .item(Contact.builder()
                        .icon("\\faCar")
                        .label("Driving License B")
                        .build())
                .build());

        Long contactId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Contact")
                .showTitle(true)
                .sectionItems(contactItems)
                .build());

        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(contactId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .itemsep(4.0)
                .build());
        }


    private void addEducationSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        List<SectionItemCreateReq> educationItems = new ArrayList<>();
        educationItems.add(SectionItemCreateReq.builder()
                .itemOrder(2)
                .item(Education.builder()
                        .degree("Bachelor of Science in Computer Science")
                        .institution("Open Universiteit Utrecht")
                        .period("2019 - 2023")
                        .build())
                .build());

        educationItems.add(SectionItemCreateReq.builder()
                .itemOrder(1)
                .item(Education.builder()
                        .degree("Master of Science in Computer Science")
                        .institution("Open Universiteit Utrecht")
                        .period("2023 -")
                        .build())
                .build());
        
        educationItems.add(SectionItemCreateReq.builder()
                .itemOrder(3)
                .item(Education.builder()
                        .degree("Science-Mathematics")
                        .institution("International School of Brussels")
                        .period("2016")
                        .build())
                .build());
        
        Long educationId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Education")
                .showTitle(true)
                .sectionItems(educationItems)
                .build());

        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(educationId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .build());

    }

    private void addExperienceSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        List<SectionItemCreateReq> experienceItems = new ArrayList<>();
        experienceItems.add(SectionItemCreateReq.builder()
                .item(Experience.builder()
                        .jobTitle("Software Engineer")
                        .companyName("MIVB/STIB")
                        .period("2021 - 2022")
                        .description("Developing software for the public transport company of Brussels")
                        .responsibilities(List.of("Developing new features for the website", "Maintaining the existing codebase"))
                        .build())
                .build());
            
        Long experienceId = sectionService.create(SectionCreateReq.builder()
            .resumeId(resumeId)
            .title("Experience")
            .sectionItems(experienceItems)
            .build());
        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(experienceId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .build());
    }

    private void addTitleSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        SectionItemCreateReq title = SectionItemCreateReq.builder()
                .item(Title.builder()
                        .title("John Doe")
                        .subtitle("Software Developer")
                        .build())
                .build();
            
        Long titleId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Title")
                .showTitle(false)
                .sectionItems(List.of(title))
                .build());
        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(titleId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .endsep(6.0)
                .build());
    }

    private void addSummarySection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        List<SectionItemCreateReq> summaryItems = new ArrayList<>();
        summaryItems.add(SectionItemCreateReq.builder()
                .item(Textbox.builder()
                        .content("""
                            Passionate software developer with a strong foundation in computer science, currently pursuing MSc at Open Universiteit Utrecht. 
                            Specializing in Java development with hands-on experience in Spring Boot and web technologies. 
                            Strong analytical mindset developed through mathematics-focused secondary education.
                            Eager to apply theoretical knowledge in practical software development challenges.
                        """)
                        .build())
                .build());
            
        Long summaryId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("About me")
                .sectionItems(summaryItems)
                .build());
        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(summaryId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .build());
    }

    private void addSkillsSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds) {
        List<SectionItemCreateReq> skillsItems = new ArrayList<>();
        skillsItems.add(SectionItemCreateReq.builder()
                .item(Skillboxes.builder()
                        .skills(List.of("Java", "Spring Boot", "HTML", "CSS", "JavaScript", "SQL", "Python", "C", "C++"))
                        .build())
                .build());

        skillsItems.add(SectionItemCreateReq.builder()
                .item(Skill.builder()
                        .name("Dutch")
                        .build())
                .build());

        skillsItems.add(SectionItemCreateReq.builder()
                .item(Skill.builder()
                        .name("French")
                        .description("Intermediate")
                        .build())
                .build());

        skillsItems.add(SectionItemCreateReq.builder()
                .item(Skill.builder()
                        .name("English")
                        .proficiency(9)
                        .build())
                .build());

        skillsItems.add(SectionItemCreateReq.builder()
                .item(Skill.builder()
                        .name("Japanese")
                        .proficiency(6)
                        .build())
                .build());
            
        Long skillsId = sectionService.create(SectionCreateReq.builder()
                .resumeId(resumeId)
                .title("Skills")
                .sectionItems(skillsItems)
                .build());

        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(skillsId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .itemsep(4.0)
                .build());
    }

    private Long addLayout(Long resumeId) {
        CreateLayoutRequest layoutRequest = CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(2)
                .build();
        return layoutService.create(layoutRequest);
    }
}