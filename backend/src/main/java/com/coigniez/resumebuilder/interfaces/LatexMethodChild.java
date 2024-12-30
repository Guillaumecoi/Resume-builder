package com.coigniez.resumebuilder.interfaces;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;

/**
 * Interface for providing a LatexMethod to a class.
 * Implementing classes should provide a LatexMethod to the class.
 */
public interface LatexMethodChild extends LatexMethodProvider {

    /**
     * Sets the LatexMethod for the implementing class.
     * 
     * @param latexMethod The LatexMethod to set
     */
    void setLatexMethod(LatexMethod latexMethod);
}