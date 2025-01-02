package com.coigniez.resumebuilder.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionUpdateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResp;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemUpdateReq;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionServiceWithItemsIntegrationTest {

        @Autowired
        private SectionService sectionService;
        @Autowired
        private ResumeService resumeService;

        private Authentication testuser;
        private Long resumeId;

        @BeforeEach
        void setUp() {
                // Create mock users
                testuser = new UsernamePasswordAuthenticationToken(
                                "testuser",
                                "password",
                                List.of(new SimpleGrantedAuthority("ROLE_USER")));

                // Set the Authentication object in the SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(testuser);

                CreateResumeRequest resumeRequest = CreateResumeRequest.builder().title("Software Developer")
                                .sections(List.of(SectionCreateReq.builder().title("Education").build())).build();

                resumeId = resumeService.create(resumeRequest);
        }

        @Test
        void testCreateAndGet() {
                // Arrange
                List<SectionItemCreateReq> sectionItems = new ArrayList<>();

                // Add a textbox item to the section
                sectionItems.add(SectionItemCreateReq.builder()
                                .itemOrder(1)
                                .item(Textbox.builder().content("This is some example text").build())
                                .build());

                sectionItems.add(SectionItemCreateReq.builder()
                                .itemOrder(2)
                                .item(Skill.builder().name("Java").proficiency(5).build())
                                .build());

                SectionCreateReq request = SectionCreateReq.builder()
                                .resumeId(resumeId)
                                .title("Test Section")
                                .sectionItems(sectionItems)
                                .build();

                // Act
                Long sectionId = sectionService.create(request);
                SectionResp response = sectionService.get(sectionId);

                // Assert
                assertNotNull(sectionId);
                assertEquals("Test Section", response.getTitle());
                assertEquals(2, response.getSectionItems().size());
                assertEquals(1, response.getSectionItems().get(0).getSectionOrder());
                assertEquals(Skill.class, response.getSectionItems().get(1).getItem().getClass());
                assertEquals("This is some example text",
                                ((Textbox) response.getSectionItems().get(0).getItem()).getContent());
                assertEquals("Java", ((Skill) response.getSectionItems().get(1).getItem()).getName());
                assertEquals(5, ((Skill) response.getSectionItems().get(1).getItem()).getProficiency());
        }

        @Test
        void testUpdate() {
                // Arrange
                List<SectionItemCreateReq> sectionItems = new ArrayList<>();

                sectionItems.add(SectionItemCreateReq.builder()
                                .item(Textbox.builder().content("This is some example text").build())
                                .build());

                sectionItems.add(SectionItemCreateReq.builder()
                                .item(Skill.builder().name("Java").proficiency(5).build())
                                .build());

                SectionCreateReq request = SectionCreateReq.builder()
                                .resumeId(resumeId)
                                .title("Test Section")
                                .sectionItems(sectionItems)
                                .build();

                Long sectionId = sectionService.create(request);
                SectionResp initialResponse = sectionService.get(sectionId);

                // Update the section
                List<SectionItemCreateReq> createSectionItems = new ArrayList<>();
                List<SectionItemUpdateReq> updateSectionItems = new ArrayList<>();

                // Add a new skill item to the section
                createSectionItems.add(SectionItemCreateReq.builder()
                                .itemOrder(1)
                                .item(Skill.builder().name("Python").proficiency(4).build())
                                .build());
                updateSectionItems.add(SectionItemUpdateReq.builder()
                                .id(initialResponse.getSectionItems().get(0).getId())
                                .itemOrder(3)
                                .item(Textbox.builder().content("This is some updated text").build())
                                .build());

                SectionUpdateReq updatedRequest = SectionUpdateReq.builder().id(sectionId)
                                .title("Updated Section").showTitle(false)
                                .createSectionItems(createSectionItems).updateSectionItems(updateSectionItems).build();

                // Act
                sectionService.update(updatedRequest);
                SectionResp response = sectionService.get(sectionId);

                // Assert
                assertNotNull(sectionId);
                assertEquals("Updated Section", response.getTitle(), "Section title should be updated");
                assertFalse(response.isShowTitle(), "Show title should be updated");
                assertEquals(3, response.getSectionItems().size(), "Section should have 3 items after update");
                assertEquals(List.of(1, 2, 3), response.getSectionItems().stream()
                                .map(SectionItemResp::getItemOrder)
                                .toList(), "Item order should be updated");

                // First skill item
                SectionItemResp firstSkill = response.getSectionItems().get(0);
                assertTrue(firstSkill.getItem() instanceof Skill);
                Skill skill1 = (Skill) firstSkill.getItem();
                assertEquals("Python", skill1.getName());
                assertEquals(4, skill1.getProficiency());

                // Second skill item
                SectionItemResp secondSkill = response.getSectionItems().get(1);
                assertTrue(secondSkill.getItem() instanceof Skill);
                Skill skill2 = (Skill) secondSkill.getItem();
                assertEquals("Java", skill2.getName());
                assertEquals(5, skill2.getProficiency());

                // Textbox item
                SectionItemResp textbox = response.getSectionItems().get(2);
                assertTrue(textbox.getItem() instanceof Textbox);
                assertEquals(initialResponse.getSectionItems().get(0).getId(), textbox.getId());
                assertEquals("This is some updated text", ((Textbox) textbox.getItem()).getContent());
        }

        @Test
        void testValidation() {
                // Arrange
                List<SectionItemCreateReq> sectionItems = new ArrayList<>();

                sectionItems.add(SectionItemCreateReq.builder()
                                .itemOrder(1)
                                .item(Skill.builder().name("java").proficiency(11).build())
                                .build());

                SectionCreateReq request = SectionCreateReq.builder()
                                .resumeId(resumeId)
                                .title("Test Section")
                                .sectionItems(sectionItems)
                                .build();

                // Act & Assert
                assertThrows(ConstraintViolationException.class, () -> sectionService.create(request),
                                "Validation should fail when skill proficiency is greater than 10");
        }
}
