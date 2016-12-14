package uk.co.hughingram.lifedemo.model;

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
}
