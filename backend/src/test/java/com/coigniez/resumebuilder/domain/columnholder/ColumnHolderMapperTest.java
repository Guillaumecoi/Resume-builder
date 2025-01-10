package com.coigniez.resumebuilder.domain.columnholder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.header.Header;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderResp;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;

@SpringBootTest
public class ColumnHolderMapperTest {

    @Autowired
    private ColumnHolderMapper columnHolderMapper;

    @Test
    void testToEntity_HeaderFooterCreateReq() {
        HeaderSimpleCreateReq request = HeaderSimpleCreateReq.builder()
                .height(10.0)
                .repeatOnEveryPage(true)
                .build();

        ColumnHolder entity = columnHolderMapper.toEntity(request);

        assertNotNull(entity);
        assertTrue(entity instanceof Header);
        Header headerFooter = (Header) entity;
        assertEquals(10.0, headerFooter.getHeight());
        assertTrue(headerFooter.getRepeatOnEveryPage());
        assertEquals(0, headerFooter.getColumns().size());
    }

    @Test
    void testToEntity_PageCreateReq() {
        PageSimpleCreateReq request = PageSimpleCreateReq.builder()
                .pageNumber(1)
                .build();

        ColumnHolder entity = columnHolderMapper.toEntity(request);

        assertNotNull(entity);
        assertTrue(entity instanceof LayoutPage);
        LayoutPage layoutPage = (LayoutPage) entity;
        assertEquals(1, layoutPage.getPageNumber());
    }

    @Test
    void testToDto_HeaderFooter() {
        Header entity = Header.builder()
                .height(10.0)
                .repeatOnEveryPage(true)
                .build();

        ColumnHolderResp dto = columnHolderMapper.toDto(entity);

        assertNotNull(dto);
        assertTrue(dto instanceof HeaderResp);
        HeaderResp headerFooterResp = (HeaderResp) dto;
        assertEquals(10.0, headerFooterResp.getHeight());
        assertTrue(headerFooterResp.getRepeatOnEveryPage());
    }

    @Test
    void testToDto_LayoutPage() {
        LayoutPage entity = LayoutPage.builder()
                .pageNumber(1)
                .build();

        ColumnHolderResp dto = columnHolderMapper.toDto(entity);

        assertNotNull(dto);
        assertTrue(dto instanceof PageResp);
        PageResp pageResp = (PageResp) dto;
        assertEquals(1, pageResp.getPageNumber());
    }
}