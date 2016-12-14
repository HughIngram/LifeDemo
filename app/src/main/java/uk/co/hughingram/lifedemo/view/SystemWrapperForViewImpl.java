package uk.co.hughingram.lifedemo.view;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Implementation of the System Wrapper for View.
 */
public final class SystemWrapperForViewImpl implements ActivityWrapper_View {
    private final AppCompatActivity activity;

    public SystemWrapperForViewImpl(final AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public View findViewById(@IdRes final int viewId) {
        return activity.findViewById(viewId);
    }

    @Override
    public void runOnUiThread(final Runnable action) {
        activity.runOnUiThread(action);
    }
}
