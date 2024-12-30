package com.coigniez.resumebuilder.latex.generators;

import java.util.List;

import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.MethodType;

public class LatexMethodGenerator {

    /**
     * Generates the LaTeX method usage using the provided SectionItemData.
     *
     * @param provider the SectionItemData instance containing parameters
     * @return the LaTeX method usage as a string
     */
    public static String generateUsage(MethodType methodType, HasLatexMethod type, String name, List<String> parameters) {
        if (parameters.size() != type.getNumberOfParameters()) {
            throw new IllegalStateException("Number of parameters does not match the method type");
        }

        return methodType.getMethodUsage(name, parameters);
    }

    /**
     * Generates the LaTeX method using the provided information.
     *
     * @return the LaTeX method as a string
     */
    public static String generateMethod(MethodType methodType, HasLatexMethod type, String name, String method) {
        return methodType.getMethodHeader(name, type.getNumberOfParameters()) + method;
    }
}
