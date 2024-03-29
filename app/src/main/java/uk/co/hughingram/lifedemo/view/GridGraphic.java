package uk.co.hughingram.lifedemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForView;


/**
 * View to show a graphic representation of the grid.
 */
public final class GridGraphic extends SurfaceView implements Runnable {

    private final static String TAG = "GridGraphic";

    Thread thread = null;
    final SurfaceHolder holder;
    final AppPresenterForView presenter;
    boolean isRunning;

    // these constructors will not work with XML!
    public GridGraphic(final Context context, final AppPresenterForView presenter) {
        this(context, null, presenter);
    }

    public GridGraphic(final Context context, final AttributeSet attributeSet,
                       final AppPresenterForView presenter) {
        super(context, attributeSet);
        holder = getHolder();
        this.presenter = presenter;
        // This may cause problems when trying to put another view element on top.
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        isRunning = false;
        try {
            thread.join();
        } catch (final InterruptedException e) {
            Log.e(TAG, "pausing failed");
        }
    }

    @Override
    public void run() {
         while(isRunning) {
             draw();
         }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            final Canvas c = holder.lockCanvas();
            // clear previous frame
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            float viewHeight = this.getHeight();
            float viewWidth = this.getWidth();
            final Bitmap bmp = presenter.getRenderedGrid();
            float bmpHeight = bmp.getHeight();
            float bmpWidth = bmp.getWidth();
            float bmpAspectRatio = bmpWidth / bmpHeight;
            // just fit to width for now
            float scaledHeight = viewWidth / bmpAspectRatio;
            final RectF rectF = new RectF(0, 0, viewWidth, scaledHeight);
            c.drawBitmap(bmp, null, rectF, null);
            holder.unlockCanvasAndPost(c);
        }
    }
}
