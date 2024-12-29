package com.coigniez.resumebuilder.templates.layouts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.resume.dtos.CreateResumeRequest;
import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.services.ColumnSectionService;
import com.coigniez.resumebuilder.services.LayoutService;
import com.coigniez.resumebuilder.services.ResumeService;
import com.coigniez.resumebuilder.services.SectionItemService;
import com.coigniez.resumebuilder.services.SectionService;
import com.coigniez.resumebuilder.templates.color.ColorTemplates;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;

@Component
public class StandardNavyBlueTemplate {

    public static final String LAYOUT_NAME = "Standard Navy Blue";
    public static final String LAYOUT_DESCRIPTION = "A simple, clean layout with a navy blue color scheme.";
    public static final byte[] IMAGE_CONTENT;

    static {
        byte[] content = null;
        try {
            Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
            content = Files.readAllBytes(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IMAGE_CONTENT = content;
    }

    private final ResumeService resumeService;
    private final LayoutService layoutService;
    private final SectionService sectionService;
    private final SectionItemService sectionItemService;
    private final ColumnSectionService columnSectionService;

    @Lazy
    public StandardNavyBlueTemplate(ResumeService resumeService, LayoutService layoutService,
            SectionService sectionService, SectionItemService sectionItemService,
            ColumnSectionService columnSectionService) {
        this.resumeService = resumeService;
        this.layoutService = layoutService;
        this.sectionService = sectionService;
        this.sectionItemService = sectionItemService;
        this.columnSectionService = columnSectionService;
    }

    public long generate(String title) throws IOException {
        long resumeId = createResume(title);
        long layoutId = createLayout(resumeId);
        LayoutResponse layout = layoutService.get(layoutId);
        long leftColumnId = layout.getColumns().get(0).getId();
        long rightColumnId = layout.getColumns().get(1).getId();
        Map<String, Long> methodIds = layoutService.getLatexMethodsMap(layoutId);

        addPictureSection(resumeId, leftColumnId, 1, methodIds);

        return resumeId;
    }

    private long createResume(String title) {
        CreateResumeRequest request = CreateResumeRequest.builder()
                .title(title)
                .build();
        return resumeService.create(request);

    }

    private long createLayout(long resumeId) {
        CreateLayoutRequest request = CreateLayoutRequest.builder()
                .resumeId(resumeId)
                .numberOfColumns(1)
                .columns(List.of(
                        CreateColumnRequest.builder()
                                .columnNumber(1)
                                .backgroundColor(ColorLocation.LIGHT_BG)
                                .textColor(ColorLocation.DARK_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .borderRight(2.0)
                                .build(),
                        CreateColumnRequest.builder()
                                .columnNumber(2)
                                .backgroundColor(ColorLocation.LIGHT_BG)
                                .textColor(ColorLocation.DARK_TEXT)
                                .borderColor(ColorLocation.ACCENT)
                                .build()))
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(LatexMethodTemplates.getStandardMethods())
                .build();

        return layoutService.create(request);
    }

    private void addPictureSection(Long resumeId, Long columnId, int sectionOrder, Map<String, Long> methodIds)
            throws IOException {
        Long sectionId = sectionService.create(CreateSectionRequest.builder()
                .resumeId(resumeId)
                .title("Picture")
                .showTitle(false)
                .build());

        CreateSectionItemRequest pictureRequest = CreateSectionItemRequest.builder()
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

        //Todo: handle exception
        Path resourcePath = Paths.get("src", "main", "resources", "images", "ali-mammadli-unsplash.jpg");
        byte[] content = Files.readAllBytes(resourcePath);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "ali-mammadli-unsplash.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                content);
        sectionItemService.createPicture(file, pictureRequest);

        columnSectionService.create(CreateColumnSectionRequest.builder()
                .columnId(columnId)
                .sectionId(sectionId)
                .latexMethodId(methodIds.get("sectiontitle"))
                .itemOrder(sectionOrder)
                .build());
    }

}
