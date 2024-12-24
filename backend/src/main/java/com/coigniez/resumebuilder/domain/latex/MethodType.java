package com.coigniez.resumebuilder.domain.latex;

import java.util.List;

/**
 * Enum representing the different types of latex methods
 */
public enum MethodType {
    COMMAND(
            "\\newcommand{\\%s}[%s]\n",
            "\\%s%s"),
    ENVIRONMENT(
            "\\newenvironment{%s}[%s]\n",
            "\\begin{%s}%s\n%s\\end{%s}");

    private final String header;
    private final String usage;

    MethodType(String header, String usage) {
        this.header = header;
        this.usage = usage;
    }

    /**
     * Returns the method header for the given method name and parameter count
     * 
     * @param name           The name of the method
     * @param parameterCount The number of parameters the method takes
     * @return The method header
     */
    public String getMethodHeader(String name, Integer parameterCount) {
        String parameterCountString = parameterCount == null ? "" : parameterCount.toString();
        return header.formatted(name, parameterCountString);
    }

    /**
     * Returns the command for the given name and parameter list
     * In case of an environment, content can be inserted where %s is placed
     * 
     * @param name          The name of the command
     * @param parameterList The list of parameters
     * @return The command
     */
    public String getMethodUsage(String name, List<String> parameterList) {
        String parameters = "{" + String.join("}{", parameterList) + "}";
        return usage.formatted(name, parameters, "%s", name);
    }

}
