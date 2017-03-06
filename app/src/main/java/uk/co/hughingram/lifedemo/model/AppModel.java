package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

/**
 * Interface definition for the App Model.
 */
public interface AppModel {

    boolean isSimulationRunning();

    /**
     * Prepare the simulation to be run.
     */
    void setUpSimulation();

    /**
     * 'play' the simulation.
     */
    void runSimulation();

    void pauseSimulation();

    /**
     * Steps the grid a single iteration.
     */
    void iterateOnce();

    /**
     * Renders the GridHolder as a Bitmap.
     * @return the rendered grid.
     */
    Bitmap render();

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

    // TODO replace this with a setSpeed() method to work with a slider.

    /**
     * Increase the speed of the simulation.
     */
    void speedUp();

    /**
     * Decrease the speed of teh simulation.
     */
    void speedDown();

}
