package com.coigniez.resumebuilder.interfaces;

import java.util.List;

/**
 * Interface for providing data for generating LaTeX commands.
 * Implementing classes should provide the data required for generating LaTeX commands.
 */
public interface LatexMethodProvider {

    /**
     * The number of parameters getData() should return.
     * This value needs to be overwritten in the implementing class.
     */
    static int getBaseParameterCount() {
        return -1;
    }

    /**
     * Provides the list of data fields required for generating LaTeX commands.
     * Each item in the list corresponds to a placeholder in the command.
     * 
     * @return List of strings representing the data for LaTeX generation
     */
    List<String> getData();
}