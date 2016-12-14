package uk.co.hughingram.lifedemo.view;

/**
 * Interface for the App's view.
 */

public interface AppView {

    /**
     * Displays the given grid.
     * @param grid the grid to show.
     */
    void updateGrid(final String grid);

    void showStatePaused();

    void showStateRunning();

}
