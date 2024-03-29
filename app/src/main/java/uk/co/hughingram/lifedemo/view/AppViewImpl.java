package uk.co.hughingram.lifedemo.view;

import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import uk.co.hughingram.lifedemo.R;
import uk.co.hughingram.lifedemo.presenter.AppPresenterForView;

/**
 * Master class for the AppPresenterImpl component.
 */
public final class AppViewImpl implements AppView {

    private final SystemWrapperForView systemWrapper;
    private final AppPresenterForView presenter;

    private final FloatingActionButton fab;


    public AppViewImpl(final SystemWrapperForView activityWrapper,
                       final AppPresenterForView presenter) {
        this.systemWrapper = activityWrapper;
        this.presenter = presenter;
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
    public void showStatePaused() {
        fab.setImageDrawable(systemWrapper.getResources()
                .getDrawable(android.R.drawable.ic_media_play));
    }

    @Override
    public void showStateRunning() {
        fab.setImageDrawable(systemWrapper.getResources()
                .getDrawable(android.R.drawable.ic_media_pause));
    }

    public static final int PATTERNS_GROUP_ID = 1;

    @Override
    public void setPatternChoices(final SubMenu subMenu, final String[] choices) {
        for (final String s : choices) {
            subMenu.add(PATTERNS_GROUP_ID, Menu.NONE, Menu.NONE, s);
        }
    }
}
