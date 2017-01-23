package uk.co.hughingram.lifedemo.model;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

/**
 * Interface definition for the App Model.
 */
public interface AppModel {

    boolean isSimulationRunning();

    void runSimulation();

    void pauseSimulation();

    /**
     * For a given grid, an ascii rendering is produce.
     * @param grid the grid data.
     * @return the rendered grid.
     */
    String render(final boolean[][] grid);

    // should it be possible for the presenter to have access to the un-encapsulated array?
    /**
     * Gets the Grid in its current state.
     *
     * @return the Grid.
     */
    boolean[][] getGrid();

    /**
     * Gets an array of ID's of patterns available to be loaded.
     *
     * @return the available patterns.
     */
    String[] getAvailablePatterns();

    /**
     * Load the pattern with the given ID.
     *
     * @param id the ID of the pattern to load.
     */
    void loadPattern(final String id);

    void setPresenter(final AppPresenterForModel presenter);

}
