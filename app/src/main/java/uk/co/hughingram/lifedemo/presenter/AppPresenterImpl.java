package uk.co.hughingram.lifedemo.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import uk.co.hughingram.lifedemo.model.AppModel;
import uk.co.hughingram.lifedemo.view.AppView;

/**
 * Master class for the AppPresenterImpl component.
 */
final class AppPresenterImpl implements AppPresenterForView, AppPresenterForModel {

    private AppModel model;
    private AppView view;

    private final static String TAG = "Presenter";

    public void setView(final AppView view) {
        this.view = view;
    }

    @Override
    public void toggleSimulationRunning() {
        if (!model.isSimulationRunning()) {
            model.setUpSimulation();
            model.runSimulation();
            view.showStateRunning();
        } else {
            model.pauseSimulation();
            view.showStatePaused();
        }
    }

    @Override
    public Bitmap getRenderedGrid() {
        return model.render();
    }

    public void setModel(final AppModel model) {
        this.model = model;
    }
}
