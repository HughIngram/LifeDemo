package uk.co.hughingram.lifedemo.presenter;

import android.graphics.Bitmap;

/**
 * Interface for the App presenter;
 */

public interface AppPresenterForView {

    void toggleSimulationRunning();

    void setUpSimulation();

    // TODO this is not really ideal - this method enables an active View.
    Bitmap getRenderedGrid();
}
