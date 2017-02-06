package uk.co.hughingram.lifedemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForView;


// TODO implement runnable and try to fix the frame rate at 30fps
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
//        getHolder().addCallback(this);
        this.presenter = presenter;
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
            Log.e(TAG, "ADGAAAAAAAA");
        }
    }

//    @Override
//    public void surfaceCreated(final SurfaceHolder holder) {
////
////        final Canvas c = holder.lockCanvas();
////        int h = this.getHeight();
////        int w = this.getWidth();
////        Bitmap bmp = presenter.getRenderedGrid();
////        c.drawBitmap(bmp, null, new RectF(0, 0, w, h), null);
////        holder.unlockCanvasAndPost(c);
//        isRunning = true;
//    }
//
//    @Override
//    public void surfaceChanged(final SurfaceHolder holder, final int format,
//                               final int width, final int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(final SurfaceHolder holder) {
//
//    }

    // TODO put this in the run method
//    @Override
//    public void onDraw(final Canvas canvas) {
//        super.onDraw(canvas);
//        final Canvas c = holder.lockCanvas();
//        int h = this.getHeight();
//        int w = this.getWidth();
//        Bitmap bmp = presenter.getRenderedGrid();
//        c.drawBitmap(bmp, null, new RectF(0, 0, w, h), null);
//        holder.unlockCanvasAndPost(c);
//    }

    @Override
    public void run() {
         while(isRunning) {
             if (holder.getSurface().isValid()) {
                 final Canvas c = holder.lockCanvas();
                 int h = this.getHeight();
                 int w = this.getWidth();
                 final Bitmap bmp = presenter.getRenderedGrid();
                 RectF rectF = new RectF(0, 0, w, h);
                 c.drawBitmap(bmp, null, rectF, null);
                 holder.unlockCanvasAndPost(c);
             }
         }
    }
}
