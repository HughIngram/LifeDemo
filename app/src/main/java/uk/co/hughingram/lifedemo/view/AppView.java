package uk.co.hughingram.lifedemo.view;

import android.view.SubMenu;

/**
 * Interface for the App's view.
 */
public interface AppView {

    void showStatePaused();

    void showStateRunning();

    void setPatternChoices(final SubMenu subMenu, final String[] choices);

}
