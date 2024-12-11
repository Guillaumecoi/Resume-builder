package com.coigniez.resumebuilder.latex.generators;

/**
 * Interface for generating latex Strings.
 * @param <T> The type of input to generate the String from.
 */
public interface LatexGenerator<T> {

    /**
     * Generates a latex String from the input.
     * @param input The input to generate the String from.
     * @return The generated latex String.
     */
    String generate(T input);
}
