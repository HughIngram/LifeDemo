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
     * Gets the given pattern.
     * @param patternId the Pattern ID.
     */
    boolean[][] loadPattern(final String patternId);

}
