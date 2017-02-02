package uk.co.hughingram.lifedemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Encapsulating class for Activities, to be passed to the View component.
 */
public interface SystemWrapperForView {

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

    /**
     * Returns a Resources instance.
     * @return resources
     */
    Resources getResources();

    Context getContext();
}
