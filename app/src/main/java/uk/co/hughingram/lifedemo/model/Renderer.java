package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Renders Bitmaps of the GridHolder.
 */
final class Renderer {

    private static final String TAG = "Renderer";

    Bitmap render(final GridHolder grid) {
        int w = grid.getWidth();
        int h = grid.getHeight();
        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmp = Bitmap.createBitmap(w, h, conf);
        if (grid.isLocked()) {
            try {
                Thread.sleep(5);
            } catch (final InterruptedException e) {
                Log.e(TAG, "FAILED TO SLEEP!");
            }
        }
        grid.lock();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (grid.getCell(j, i)) {
                    bmp.setPixel(j, i, Color.BLUE);
                } else {
                    bmp.setPixel(j, i, Color.GRAY);
                }
            }
        }
        grid.unlock();
        return bmp;
    }
}
