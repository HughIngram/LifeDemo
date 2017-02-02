package uk.co.hughingram.lifedemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Implementation of the System Wrapper for View.
 */
public final class SystemWrapperForViewImpl implements SystemWrapperForView {
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

    @Override
    public Resources getResources() {
        return activity.getResources();
    }

    @Override
    public Context getContext() {
        return activity.getApplicationContext();
    }

}
