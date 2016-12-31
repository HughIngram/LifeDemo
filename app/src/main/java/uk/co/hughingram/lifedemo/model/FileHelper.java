package uk.co.hughingram.lifedemo.model;

/**
 * Interface to help with reading from the File System.
 */
interface FileHelper {

    /**
     * Get the List of available patterns.
     * @return the list of patterns.
     */
    String[] getPatternList();

    /**
     * Loads the given pattern into the Model.
     * @param patternId the Pattern ID.
     */
    void loadPattern(final String patternId);

}
