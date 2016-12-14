package uk.co.hughingram.lifedemo.view;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Encapsulating class for Activities, to be passed to the View component.
 */

public interface ActivityWrapper_View {

    /**
     * Gets a child view of the base layout.
     * @param viewId the view ID.
     * @return the View.
     */
    View findViewById(@IdRes final int viewId);

    /**
     * Runs an action on the UI thread.
     * @param action the action to run.
     */
    void runOnUiThread(final Runnable action);

}
