package uk.co.hughingram.lifedemo.presenter;

import android.util.Log;

import uk.co.hughingram.lifedemo.model.AppModel;
import uk.co.hughingram.lifedemo.model.Grid;
import uk.co.hughingram.lifedemo.view.AppView;

/**
 * Master class for the AppPresenterImpl component.
 */
public final class AppPresenterImpl implements AppPresenterForView, AppPresenterForModel {

    private AppModel model;
    private AppView view;

    private final static String TAG = "Presenter";

    public void setView(final AppView view) {
        this.view = view;
    }

    @Override
    public void setUpSimulation() {
        final String renderedGrid = model.render();
        view.updateGrid(renderedGrid);
    }

    @Override
    public void toggleSimulationRunning() {
        if (!model.isSimulationRunning()) {
            model.runSimulation();
            view.showStateRunning();
        } else {
            model.pauseSimulation();
            view.showStatePaused();
        }
    }

    @Override
    public void displayGrid(final Grid grid) {
        // TODO
        view.updateGrid(grid.toString());
    }

    public void setModel(final AppModel model) {
        this.model = model;
    }
}
