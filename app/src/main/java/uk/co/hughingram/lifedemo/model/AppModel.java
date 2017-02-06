package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

/**
 * Interface definition for the App Model.
 */
public interface AppModel {

    boolean isSimulationRunning();

    void runSimulation();

    void pauseSimulation();

    /**
     * Renders the Grid as a Bitmap.
     * @return the rendered grid.
     */
    Bitmap render();

    /**
     * Remove this when the render() method is implemented!
     * @return
     */
    String getModelString();

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
