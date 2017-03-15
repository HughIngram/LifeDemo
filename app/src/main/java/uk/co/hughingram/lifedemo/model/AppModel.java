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
    void loadPattern(String id);

    void setPresenter(AppPresenterForModel presenter);

    /**
     * Set the speed of the simulation.
     *
     * @param speed the speed, between 0 and 1000.
     */
    void setSpeed(int speed);

}
