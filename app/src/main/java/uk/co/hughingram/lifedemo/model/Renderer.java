package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Renders Bitmaps of the Grid.
 */
final class Renderer {

    Bitmap render(final Grid grid) {
        int w = grid.getWidth();
        int h = grid.getHeight();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(w, h, conf);
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
