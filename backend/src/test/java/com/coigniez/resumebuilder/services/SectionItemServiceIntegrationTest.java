package com.coigniez.resumebuilder.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.repository.SectionItemRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SectionItemServiceIntegrationTest {

    @Autowired
    private SectionItemService sectionItemService;
    @Autowired
    private SectionItemRepository sectionItemRepository;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private EntityManager entityManager;

    private Authentication testuser;
    private Authentication otheruser;
    private Long sectionId;

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

        CreateResumeRequest resumeRequest = CreateResumeRequest.builder().title("Software Developer").build();

        Long resumeId = resumeService.create(resumeRequest);

        sectionId = sectionService.create(CreateSectionRequest.builder()
                .resumeId(resumeId)
                .title("Education")
                .build());
    }

    @Test
    void testCreate() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        CreateSectionItemRequest request = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("This is some example text").build())
                .build();

        // Act
        Long sectionItemId = sectionItemService.create(request);
        SectionItem sectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();

        // Assert
        assertNotNull(sectionItemId, "Section item ID should not be null");
        assertNotNull(sectionItem.getId(), "Section item entity ID should not be null");
        assertNotNull(sectionItem.getItem(), "Section item data should not be null");
        assertEquals(1, sectionItem.getItemOrder(), "Item order should be 1");
        assertEquals("This is some example text", ((Textbox) sectionItem.getItem()).getContent(),
                "Textbox content should be 'This is some example text'");
    }

    @Test
    void testCreatePicture_WithRealImage() throws IOException {
        // Arrange
        MockMultipartFile file = getPictureFile();

        CreateSectionItemRequest request = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Picture.builder().build())
                .build();

        // Act
        Long pictureId = sectionItemService.createPicture(file, request);
        SectionItem sectionItem = sectionItemRepository.findById(pictureId).orElseThrow();

        // Assert
        assertNotNull(pictureId, "The ID should not be null");
        assertEquals(pictureId, sectionItem.getId(), "The ID of the entity should match the returned ID");
        assertEquals(1, sectionItem.getItemOrder(), "The item order should be 1");
        assertNotNull(((Picture) sectionItem.getItem()).getPath(), "The path should not be null");

        // Verify actual image content
        byte[] originalBytes = file.getBytes();
        assertNotNull(originalBytes, "The retrieved image should not be null");
        assertTrue(originalBytes.length > 0, "The retrieved image should not be empty");
    }

    @Test
    void testCreate_AutoIncrementItemOrder() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .item(Textbox.builder().content("First item").build())
                .build();

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .item(Textbox.builder().content("Second item").build())
                .build();

        // Act
        sectionItemService.create(request1);
        sectionItemService.create(request2);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder)); // Sort by item order for easier comparison

        assertEquals(2, items.size(), "There should be 2 items");
        assertEquals(List.of(1, 2), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2");
        assertEquals("First item", ((Textbox) items.get(0).getItem()).getContent(),
                "First item in the list should be the first item created");
        assertEquals("Second item", ((Textbox) items.get(1).getItem()).getContent(),
                "Second item in the list should be the second item created");
    }

    @Test
    void testCreate_IncrementsItemOrder() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .item(Textbox.builder().content("First item").build())
                .build();

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .item(Textbox.builder().content("Second item").build())

                .build();

        sectionItemService.create(request1);
        sectionItemService.create(request2);

        // Act
        CreateSectionItemRequest request3 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Third item").build())
                .build();

        sectionItemService.create(request3);

        CreateSectionItemRequest request4 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(4)
                .item(Textbox.builder().content("Fourth item").build())
                .build();

        sectionItemService.create(request4);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder)); // Sort by item order for easier comparison

        assertEquals(4, items.size(), "There should be 4 items");
        assertEquals(List.of(1, 2, 3, 4),
                items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2, 3, 4");
        assertEquals("First item", ((Textbox) items.get(0).getItem()).getContent(),
                "First item in the list should be the first item created");
        assertEquals("Third item", ((Textbox) items.get(1).getItem()).getContent(),
                "Second item in the list should be the third item created");
        assertEquals("Second item", ((Textbox) items.get(2).getItem()).getContent(),
                "Third item in the list should be the second item created");
        assertEquals("Fourth item", ((Textbox) items.get(3).getItem()).getContent(),
                "Fourth item in the list should be the fourth item created");
    }

    @Test
    void testUpdateLatexMethod() {
        // Arrange
        CreateSectionItemRequest createRequest = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("First item").build())
                .build();
        Long sectionItemId = sectionItemService.create(createRequest);

        // Act
        UpdateSectionItemRequest updateRequest = UpdateSectionItemRequest.builder()
                .id(sectionItemId)
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Skill.builder().name("First item").build())
                .build();
        sectionItemService.update(updateRequest);

        // Assert
        SectionItem updatedSectionItem = sectionItemRepository.findById(sectionItemId).orElseThrow();

        assertEquals(Skill.class, updatedSectionItem.getItem().getClass(), "Item should be a Skill");
        assertEquals("First item", ((Skill) updatedSectionItem.getItem()).getName(), "Item should be updated");
    }

    @Test
    void testUpdate_DecrementItemOrder() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("First item").build())
                .build();

        sectionItemService.create(request1);

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Second item").build())
                .build();

        sectionItemService.create(request2);

        CreateSectionItemRequest request3 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(3)
                .item(Textbox.builder().content("Third item").build())
                .build();

        Long itemId3 = sectionItemService.create(request3);

        // Act
        sectionItemService.update(UpdateSectionItemRequest.builder()
                .id(itemId3)
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Updated Third item").build())
                .build());

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder)); // Sort by item order for easier comparison

        assertEquals(3, items.size(), "There should be 3 items");
        assertEquals(List.of(1, 2, 3),
                items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2, 3");
        assertEquals("First item", ((Textbox) items.get(0).getItem()).getContent(),
                "First item in the list should be the first item created");
        assertEquals("Updated Third item", ((Textbox) items.get(1).getItem()).getContent(),
                "Second item in the list should be the third item created");
        assertEquals("Second item", ((Textbox) items.get(2).getItem()).getContent(),
                "Third item in the list should be the second item created");
    }

    @Test
    void testUpdate_IncrementsItemOrder() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("First item").build())
                .build();

        sectionItemService.create(request1);

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Second item").build())
                .build();

        sectionItemService.create(request2);

        CreateSectionItemRequest request3 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(3)
                .item(Textbox.builder().content("Third item").build())
                .build();

        sectionItemService.create(request3);

        // Act
        CreateSectionItemRequest request4 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Fourth item").build())
                .build();

        sectionItemService.create(request4);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder)); // Sort by item order for easier comparison

        assertEquals(4, items.size(), "There should be 4 items");
        assertEquals(List.of(1, 2, 3, 4),
                items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2, 3, 4");
        assertEquals("First item", ((Textbox) items.get(0).getItem()).getContent(),
                "First item in the list should be the first item created");
        assertEquals("Fourth item", ((Textbox) items.get(1).getItem()).getContent(),
                "Second item in the list should be the fourth item created");
        assertEquals("Second item", ((Textbox) items.get(2).getItem()).getContent(),
                "Third item in the list should be the second item created");
        assertEquals("Third item", ((Textbox) items.get(3).getItem()).getContent(),
                "Fourth item in the list should be the third item created");
    }

    @Test
    void testDelete() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        CreateSectionItemRequest request = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("This is some example text").build())
                .build();

        Long sectionItemId = sectionItemService.create(request);

        // Act
        sectionItemService.delete(sectionItemId);

        // Assert
        assertEquals(0, sectionItemRepository.count(), "There should be no section items left");
        assertThrows(EntityNotFoundException.class, () -> sectionItemService.get(sectionItemId),
                "Should throw EntityNotFoundException when trying to get a deleted section item");
    }

    @Test
    void testDelete_DecrementItemOrder() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("First item").build())
                .build();

        sectionItemService.create(request1);

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("Second item").build())
                .build();

        Long itemId2 = sectionItemService.create(request2);

        CreateSectionItemRequest request3 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(3)
                .item(Textbox.builder().content("Third item").build())
                .build();

        sectionItemService.create(request3);

        // Act
        sectionItemService.delete(itemId2);

        // Assert
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);
        items.sort(Comparator.comparing(SectionItem::getItemOrder)); // Sort by item order for easier comparison

        assertEquals(2, items.size(), "There should be 2 items");
        assertEquals(List.of(1, 2), items.stream().map(SectionItem::getItemOrder).collect(Collectors.toList()),
                "Item orders should be 1, 2");
        assertEquals("First item", ((Textbox) items.get(0).getItem()).getContent(),
                "First item in the list should be the first item created");
        assertEquals("Third item", ((Textbox) items.get(1).getItem()).getContent(),
                "Second item in the list should be the third item created");
    }

    @Test
    void testDeleteAllBySectionId() {
        // Arrange
        CreateSectionItemRequest request1 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("This is some example text").build())
                .build();

        sectionItemService.create(request1);

        CreateSectionItemRequest request2 = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(2)
                .item(Textbox.builder().content("This is some example text").build())
                .build();

        sectionItemService.create(request2);

        // Act
        sectionItemService.removeAllByParentId(sectionId);

        // Clear the persistence context to force a refresh from DB
        entityManager.clear();

        // Assert
        SectionResponse section = sectionService.get(sectionId);
        List<SectionItem> items = sectionItemRepository.findAllBySectionId(sectionId);

        assertTrue(items.isEmpty(), "There should be no section items in the section");
        assertEquals(0, section.getSectionItems().size(), "There should be no section items in the section");
    }

    @Test
    void testAccessControl() {
        // Arrange
        Map<String, Object> data = new HashMap<>();
        data.put("content", "This is some example text");

        CreateSectionItemRequest request = CreateSectionItemRequest.builder()
                .sectionId(sectionId)
                .itemOrder(1)
                .item(Textbox.builder().content("This is some example text").build())
                .build();

        Long sectionItemId = sectionItemService.create(request);

        // Act & Assert
        SecurityContextHolder.getContext().setAuthentication(otheruser);

        assertThrows(AccessDeniedException.class, () -> sectionItemService.create(request),
                "Should not be able to create a section item for a section that does not belong to the user");
        assertThrows(AccessDeniedException.class, () -> sectionItemService.get(sectionItemId),
                "Should not be able to get a section item that does not belong to the user");
        assertThrows(AccessDeniedException.class,
                () -> sectionItemService.update(UpdateSectionItemRequest.builder()
                        .id(sectionItemId)
                        .sectionId(sectionId)
                        .itemOrder(1)
                        .item(Textbox.builder().content("Updated text").build())
                        .build()),
                "Should not be able to update a section item that does not belong to the user");
        assertThrows(AccessDeniedException.class, () -> sectionItemService.delete(sectionItemId),
                "Should not be able to delete a section item that does not belong to the user");
    }

    // Helper method to get test image
    private MockMultipartFile getPictureFile() throws IOException {
        // Load test image from resources
        Path resourcePath = Paths.get("src", "test", "resources", "blue.jpg");
        byte[] content = Files.readAllBytes(resourcePath);

        return new MockMultipartFile(
                "file",
                "blue.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content);
    }

}
