package com.coigniez.resumebuilder.util;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    public String addTabToEachLine(String input, int tabCount) {
        return input.lines()
                    .map(line -> " ".repeat(tabCount * 4) + line)
                    .collect(Collectors.joining("\n"));
    }
    
}
