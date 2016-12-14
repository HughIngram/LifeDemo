package uk.co.hughingram.lifedemo.view;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import uk.co.hughingram.lifedemo.R;
import uk.co.hughingram.lifedemo.presenter.AppPresenterForView;

/**
 * Master class for the AppPresenterImpl component.
 */

public class AppViewImpl implements AppView {

    private final ActivityWrapper_View systemWrapper;
    private final AppPresenterForView presenter;

    private final TextView gridView;
    private final FloatingActionButton fab;

    public AppViewImpl(final ActivityWrapper_View activityWrapper,
                       final AppPresenterForView presenter) {
        this.systemWrapper = activityWrapper;
        this.presenter = presenter;
        gridView = (TextView) systemWrapper.findViewById(R.id.grid);
        fab = (FloatingActionButton) systemWrapper.findViewById(R.id.fab);
        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        // TODO lambdas
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                presenter.toggleSimulationRunning();
            }
        });
    }

    @Override
    public void updateGrid(final String grid) {
        // TODO check that the threading is actually working as expected
        // creating a new runnable *every* time???
        systemWrapper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gridView.setText(grid);
            }
        });
    }

    @Override
    public void showStatePaused() {

    }

    @Override
    public void showStateRunning() {

    }
}
