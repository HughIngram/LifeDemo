package uk.co.hughingram.lifedemo.model;

import android.util.Log;

/**
 * Holds the game grid.
 */
public final class Grid {

    private boolean[][] grid;
    private boolean isLocked = false;

    private final static String TAG = "Grid";

    Grid(final SystemWrapperForModel system) {
        grid = new PatternLoader(system).getDefaultGrid();
    }

    Grid(final String patternId, final SystemWrapperForModel system) {
        grid = new PatternLoader(system).loadPattern(patternId);
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

    public boolean getCell(final int x, final int y) {
        return grid[x][y];
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
        final boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        int liveNeighbours;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                liveNeighbours = countLiveNeighbours(grid, x, y);
                if (liveNeighbours <= 1) {
                    newGrid[y][x] = false;
                } else if (liveNeighbours == 3 || (grid[y][x] && liveNeighbours == 2)) {
                    newGrid[y][x] = true;
                } else if (liveNeighbours > 3) {
                    newGrid[y][x] = false;
                } else {
                    newGrid[y][x] = grid[y][x];
                }
            }
        }
        grid = newGrid;
        unlock();
    }

    private int countLiveNeighbours(final boolean[][] grid, final int x, final int y) {
        int neighbors = 0;
        int xOfRhs = (x + 1) % grid[0].length;
        int xOfLhs;
        if (x == 0) {
            xOfLhs = grid[0].length - 1;
        } else {
            xOfLhs = x - 1;
        }
        // TODO disable wrapping
        int yOfTop;
        int yOfBottom = (y + 1) % grid.length;
        if (y == 0) {
            yOfTop = grid.length - 1;
        } else {
            yOfTop = y - 1;
        }

        if (grid[y][xOfRhs]) {
            neighbors++;
        }
        if (grid[yOfBottom][xOfRhs]) {
            neighbors++;
        }
        if (grid[yOfBottom][x]) {
            neighbors++;
        }
        if (grid[yOfBottom][xOfLhs]) {
            neighbors++;
        }
        if (grid[y][xOfLhs]) {
            neighbors++;
        }
        if (grid[yOfTop][xOfLhs]) {
            neighbors++;
        }
        if (grid[yOfTop][x]) {
            neighbors++;
        }
        if (grid[yOfTop][xOfRhs]) {
            neighbors++;
        }
        return neighbors;
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
