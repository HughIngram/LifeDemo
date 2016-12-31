package uk.co.hughingram.lifedemo.view;

import android.view.SubMenu;

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

    void setPatternChoices(final SubMenu subMenu, final String[] choices);

}
