package uk.co.hughingram.lifedemo.model;

import android.util.Log;

/**
 * Holds the game grid.
 */
public final class Grid {

    private boolean[][] grid;
    private boolean isLocked = false;
    private GridStepper stepper;

    private final static String TAG = "Grid";

    Grid(final SystemWrapperForModel system) {
        grid = new PatternLoader(system).getDefaultGrid();
        stepper = new GridStepper();
    }

    Grid(final String patternId, final SystemWrapperForModel system) {
        grid = new PatternLoader(system).loadPattern(patternId);
        stepper = new GridStepper();
    }

    void lock() {
        isLocked = true;
    }

    void unlock() {
        isLocked = false;
    }

    boolean isLocked() {
        return isLocked;
    }

    boolean getCell(final int x, final int y) {
        return grid[y][x];
    }

    int getWidth() {
        return grid[0].length;
    }

    int getHeight() {
        return grid.length;
    }

    void iterate() {
        while(isLocked()) {
            // wait for the grid to be unlocked
            try {
                Thread.sleep(5);
            } catch (final InterruptedException e) {
                Log.d(TAG, "sleep interrupted");
            }
        }
        lock();
        grid = stepper.step(grid);
        unlock();
    }

    @Override
    public String toString() {
        lock();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j]) {
                    sb.append("x");
                } else {
                    sb.append(" ");
                }
            }
            if (i < grid.length - 1) {
                sb.append("\n");
            }
        }
        unlock();
        return sb.toString();
    }
}
