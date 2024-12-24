package com.coigniez.resumebuilder.domain.latex;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTypeTest {

    @Test
    public void testGetMethodHeader_Command() {
        // Arrange
        MethodType methodType = MethodType.COMMAND;
        String name = "testCommand";
        Integer parameterCount = 2;

        // Act
        String actualHeader = methodType.getMethodHeader(name, parameterCount);

        // Assert
        String expectedHeader = "\\newcommand{\\testCommand}[2]\n";
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testGetMethodHeader_Environment() {
        // Arrange
        MethodType methodType = MethodType.ENVIRONMENT;
        String name = "testEnvironment";
        Integer parameterCount = 3;

        // Act
        String actualHeader = methodType.getMethodHeader(name, parameterCount);

        // Assert
        String expectedHeader = "\\newenvironment{testEnvironment}[3]\n";
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testGetMethodUsage_Command() {
        // Arrange
        MethodType methodType = MethodType.COMMAND;
        String name = "testCommand";
        List<String> parameterList = Arrays.asList("param1", "param2");

        // Act
        String actualUsage = methodType.getMethodUsage(name, parameterList);

        // Assert
        String expectedUsage = "\\testCommand{param1}{param2}";
        assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testGetMethodUsage_Environment() {
        // Arrange
        MethodType methodType = MethodType.ENVIRONMENT;
        String name = "testEnvironment";
        List<String> parameterList = Arrays.asList("param1", "param2");

        // Act
        String actualUsage = methodType.getMethodUsage(name, parameterList);

        // Assert
        String expectedUsage = "\\begin{testEnvironment}{param1}{param2}\n%s\\end{testEnvironment}";
        assertEquals(expectedUsage, actualUsage);
    }
}