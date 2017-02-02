package uk.co.hughingram.lifedemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * View to show a graphic representation of the grid.
 */
public class GridGraphic extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    Thread t = null;
    final SurfaceHolder holder;
    boolean isRunning = false;
    int i = 0;

    // TODO pass in a reference to the grid and the grid semaphore
    public GridGraphic(final Context context) {
        super(context);
        holder = getHolder();
        getHolder().addCallback(this);
    }

    public GridGraphic(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);
        holder = getHolder();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        final Canvas c = holder.lockCanvas();
        c.drawARGB(255, 100, i, i);
        holder.unlockCanvasAndPost(c);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, final int format,
                               final int width, final int height) {

    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {

    }

    @Override
    public void run() {
        // TODO construct a bitmap and draw it
        // should this be a loop? only one Bitmap needs to be made per screen refresh
        while (isRunning) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            final Canvas c = holder.lockCanvas();
            c.drawARGB(255, 100, i, i);
            holder.unlockCanvasAndPost(c);
            i++;
            if (i > 255) {
                i = 0;
            }
        }
    }

    public void pause() {
        isRunning = false;
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("grid graphic", "failed to pause");
        }
        t = null;
    }

    public void resume() {
        isRunning = true;
        t = new Thread(this);
        t.start();
    }
}
