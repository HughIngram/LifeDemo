package uk.co.hughingram.lifedemo.model;


import android.util.Log;

/**
 * Holds a array.
 */
final class Grid {

    // this type should never be used outside of this class.
    private boolean[][] array;

    private final static String TAG = "Grid";

    Grid(final int height, final int width) {
        array = initialiseArray(height, width);
    }

    private boolean[][] initialiseArray(final int height, final int width) {
        boolean[][] array = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                array[i][j] = false;
            }
        }
        return array;
    }

    boolean getCell(final int x, final int y) {
        // return false if out of bounds
        if (y < 0 || x < 0 || y > getHeight() - 1|| x > getWidth() - 1) {
            return false;
        } else {
            return array[y][x];
        }
    }

    void setCell(final int x, final int y, boolean state) {
        array[y][x] = state;
    }

    int getWidth() {
        return array[0].length;
    }

    int getHeight() {
        return array.length;
    }

}
