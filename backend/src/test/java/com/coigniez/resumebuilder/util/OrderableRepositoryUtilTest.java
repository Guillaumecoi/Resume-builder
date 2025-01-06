package com.coigniez.resumebuilder.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.AllArgsConstructor;
import lombok.Data;

@SpringBootTest
class OrderableRepositoryUtilTest {

    @Autowired
    private OrderableRepositoryUtil util;

    @Data
    @AllArgsConstructor
    static class TestItem {
        private String name;
        private Integer order;
    }

    @Test
    void testNullInput() {
        TestItem[] result = util.assignOrders(null, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);
        assertEquals(0, result.length);
    }

    @Test
    void testEmptyList() {
        TestItem[] result = util.assignOrders(List.of(), 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);
        assertEquals(0, result.length);
    }

    @Test
    void testAllOrdered() {
        List<TestItem> items = Arrays.asList(
            new TestItem("A", 2),
            new TestItem("B", 1),
            new TestItem("C", 3)
        );

        TestItem[] result = util.assignOrders(items, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);

        assertEquals(3, result.length);
        assertEquals("B", result[0].getName());
        assertEquals("A", result[1].getName());
        assertEquals("C", result[2].getName());
    }

    @Test
    void testAllUnordered() {
        List<TestItem> items = Arrays.asList(
            new TestItem("A", null),
            new TestItem("B", null),
            new TestItem("C", null)
        );

        TestItem[] result = util.assignOrders(items, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);

        assertEquals(3, result.length);
        assertEquals(1, result[0].getOrder());
        assertEquals(2, result[1].getOrder());
        assertEquals(3, result[2].getOrder());
    }

    @Test
    void testMixedOrdering() {
        List<TestItem> items = Arrays.asList(
            new TestItem("A", null),
            new TestItem("B", 2),
            new TestItem("C", null)
        );

        TestItem[] result = util.assignOrders(items, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);

        assertEquals(3, result.length);
        assertEquals("A", result[0].getName());
        assertEquals("B", result[1].getName());
        assertEquals("C", result[2].getName());
        assertEquals(1, result[0].getOrder());
        assertEquals(2, result[1].getOrder());
        assertEquals(3, result[2].getOrder());
    }

    @Test
    void testInvalidOrders() {
        List<TestItem> items = Arrays.asList(
            new TestItem("A", 0),    // invalid
            new TestItem("B", 99),   // invalid
            new TestItem("C", 2)     // valid
        );

        TestItem[] result = util.assignOrders(items, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);

        assertEquals(3, result.length);
        assertEquals("A", result[0].getName());
        assertEquals("C", result[1].getName());
        assertEquals("B", result[2].getName());
        assertEquals(1, result[0].getOrder());
        assertEquals(2, result[1].getOrder());
        assertEquals(3, result[2].getOrder());
    }

    @Test
    void testDuplicateOrders() {
        List<TestItem> items = Arrays.asList(
            new TestItem("A", 1),
            new TestItem("B", 1),
            new TestItem("C", 2)
        );

        TestItem[] result = util.assignOrders(items, 
            TestItem::getOrder, 
            TestItem::setOrder, 
            TestItem[]::new);

        assertEquals(3, result.length);
        assertEquals("A", result[0].getName());
        assertEquals("B", result[1].getName());
        assertEquals("C", result[2].getName());
        assertEquals(1, result[0].getOrder());
        assertEquals(2, result[1].getOrder());
        assertEquals(3, result[2].getOrder());
    }
}
