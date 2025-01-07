package com.coigniez.resumebuilder.services;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.coigniez.resumebuilder.domain.common.PageResponse;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeCreateReq;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeSimpleResp;
import com.coigniez.resumebuilder.domain.resume.dtos.ResumeUpdateReq;
import com.coigniez.resumebuilder.domain.resume.enums.ResumeOrderBy;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ResumeServiceIntegrationTest {

    @Autowired
    private ResumeService resumeService;

    private Authentication testuser;
    private Authentication otheruser;

    @BeforeEach
    void setUp() {
        // Create mock users
        testuser = new UsernamePasswordAuthenticationToken(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        otheruser = new UsernamePasswordAuthenticationToken(
                "otheruser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        // Set the Authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(testuser);
    }

    @Test
    void testCreateAndGet() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        // Act
        Long resumeId = resumeService.create(resumeRequest);
        ResumeResp resume = resumeService.get(resumeId);

        // Assert
        assertNotNull(resume, "Resume should not be null after creation");
        assertNotNull(resume.getId(), "The resume should have an id");
        assertEquals("Software Engineer", resume.getTitle(), "Title should be Software Engineer");
        assertNotNull(resume.getCreatedDate(), "Created date should not be null");
        assertNotNull(resume.getLastModifiedDate(), "Last modified date should not be null");
        assertEquals(resume.getCreatedDate(), resume.getLastModifiedDate(),
                "Created date and last modified date should be the same");
        assertEquals(resume.getSections().size(), 2, "There should be 2 sections");
        assertThat(resume.getSections().stream().map(SectionResp::getTitle))
                .containsExactlyInAnyOrder("Education", "Experience");
    }

    @Test
    void testUpdate() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        ResumeUpdateReq updatedResumeRequest = ResumeUpdateReq.builder()
                .id(resumeId)
                .title("Barista")
                .build();
        // Act
        resumeService.update(updatedResumeRequest);
        ResumeResp updatedResume = resumeService.get(resumeId);

        // Assert
        assertNotNull(updatedResume, "Resume should not be null after update");
        assertEquals("Barista", updatedResume.getTitle(), "Title should be updated");
        assertEquals(2, updatedResume.getSections().size(), "There should be 3 sections");
        assertThat(updatedResume.getSections().stream().map(SectionResp::getTitle))
                .containsExactlyInAnyOrder("Education", "Experience");
    }

    @Test
    void testDelete() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Act
        resumeService.delete(resumeId);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId);
        }, "Resume should not be found after deletion");

    }

    @Test
    void testAuthentications() {
        // Arrange
        ResumeCreateReq resumeRequest = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        Long resumeId = resumeService.create(resumeRequest);

        // Set it to otheruser
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.get(resumeId);
        },
                "Should throw AccessDeniedException when trying to get resume of other user");
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.update(ResumeUpdateReq.builder().id(resumeId).title("updated title").build());
        },
                "Should throw AccessDeniedException when trying to update resume of other user");
        assertThrows(AccessDeniedException.class, () -> {
            resumeService.delete(resumeId);
        },
                "Should throw AccessDeniedException when trying to delete resume of other user");
    }

    @Test
    void testEntityNotFound() {
        // Arrange
        Long resumeId = -1L;

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId);
        },
                "Should throw EntityNotFoundException when resume is not found");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.update(ResumeUpdateReq.builder().id(resumeId).title("updated").build());
        },
                "Should throw EntityNotFoundException when trying to update non-existing resume");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.delete(resumeId);
        },
                "Should throw EntityNotFoundException when trying to delete non-existing resume");
    }

    @Test
    void testGetAll() {
        // Arrange
        ResumeCreateReq resumeRequest1 = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();
        ResumeCreateReq resumeRequest2 = ResumeCreateReq.builder()
                .title("Barista")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();
        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        PageResponse<ResumeSimpleResp> resumes = resumeService.getAll(0, 10, ResumeOrderBy.LAST_MODIFIED_DATE,
                Sort.Direction.DESC);

        // Assert
        assertNotNull(resumes, "Resumes should not be null");
        assertEquals(2, resumes.getContent().size(), "There should be 2 resumes");
        // Check the order
        assertEquals(resumeId2, resumes.getContent().get(0).getId(),
                "Barista was added last so should be first");
        assertEquals(resumeId1, resumes.getContent().get(1).getId(),
                "Software Engineer was added first so should be last");
        // Check if resumes are correct
        assertEquals("Software Engineer", resumes.getContent().get(1).getTitle(),
                "Title should be Software Engineer");
        assertEquals("Barista", resumes.getContent().get(0).getTitle(), "Title should be Barista");
        // Check the pageresponse
        assertEquals(0, resumes.getNumber(), "Page number should be 0");
        assertEquals(10, resumes.getSize(), "Page size should be 10");
        assertEquals(2, resumes.getTotalElements(), "Total elements should be 2");
        assertEquals(1, resumes.getTotalPages(), "Total pages should be 1");
    }

    @ParameterizedTest
    @MethodSource("orderByParameters")
    void testGetAllOrdering(ResumeOrderBy orderBy, Sort.Direction direction, BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean> orderValidator) throws InterruptedException {
        // Arrange
        ResumeCreateReq resumeRequest1 = ResumeCreateReq.builder()
                .title("A-Software Engineer")
                .sections(List.of(SectionSimpleCreateReq.builder().title("Education").build()))
                .build();
        ResumeCreateReq resumeRequest2 = ResumeCreateReq.builder()
                .title("B-Barista")
                .sections(List.of(SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();
    
        // Act
        resumeService.create(resumeRequest1);
        // Add delay to ensure different creation times
        Thread.sleep(100);
        resumeService.create(resumeRequest2);
        
        PageResponse<ResumeSimpleResp> resumes = resumeService.getAll(0, 10, orderBy, direction);
    
        // Assert
        assertNotNull(resumes);
        assertEquals(2, resumes.getContent().size());
        
        // Verify ordering
        ResumeSimpleResp first = resumes.getContent().get(0);
        ResumeSimpleResp second = resumes.getContent().get(1);
        assertTrue(orderValidator.apply(first, second));
    }
    
    private static Stream<Arguments> orderByParameters() {
        return Stream.of(
            // Title ordering
            Arguments.of(ResumeOrderBy.TITLE, Sort.Direction.ASC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getTitle().compareTo(second.getTitle()) <= 0),
            Arguments.of(ResumeOrderBy.TITLE, Sort.Direction.DESC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getTitle().compareTo(second.getTitle()) >= 0),
                
            // Created date ordering
            Arguments.of(ResumeOrderBy.CREATED_DATE, Sort.Direction.ASC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getCreatedDate().compareTo(second.getCreatedDate()) <= 0),
            Arguments.of(ResumeOrderBy.CREATED_DATE, Sort.Direction.DESC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getCreatedDate().compareTo(second.getCreatedDate()) >= 0),
                
            // Last modified date ordering
            Arguments.of(ResumeOrderBy.LAST_MODIFIED_DATE, Sort.Direction.ASC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getLastModifiedDate().compareTo(second.getLastModifiedDate()) <= 0),
            Arguments.of(ResumeOrderBy.LAST_MODIFIED_DATE, Sort.Direction.DESC, 
                (BiFunction<ResumeSimpleResp, ResumeSimpleResp, Boolean>) 
                (first, second) -> first.getLastModifiedDate().compareTo(second.getLastModifiedDate()) >= 0)
        );
    }

    @Test
    void testDeleteAll() {
        // Arrange
        ResumeCreateReq resumeRequest1 = ResumeCreateReq.builder()
                .title("Software Engineer")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();
        ResumeCreateReq resumeRequest2 = ResumeCreateReq.builder()
                .title("Barista")
                .sections(List.of(
                        SectionSimpleCreateReq.builder().title("Education").build(),
                        SectionSimpleCreateReq.builder().title("Experience").build()))
                .build();

        // Act
        Long resumeId1 = resumeService.create(resumeRequest1);
        Long resumeId2 = resumeService.create(resumeRequest2);
        resumeService.deleteAll();

        // Assert
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId1);
        }, "Resume 1 should not be found after deletion");
        assertThrows(EntityNotFoundException.class, () -> {
            resumeService.get(resumeId2);
        }, "Resume 2 should not be found after deletion");

    }

}
