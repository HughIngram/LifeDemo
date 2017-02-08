package uk.co.hughingram.lifedemo.model;

import android.util.Log;

/**
 * Holds the game grid, and logic for manipulating it.
 * Do not allow other classes to manipulate this.grid!
 */
final class GridHolder {

    private boolean isLocked = false;
    private final static String TAG = "GridHolder";
    private Grid grid;

    GridHolder(final Grid grid) {
        this.grid = grid;
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
        grid = step(grid);
        unlock();
    }

    void lock() {
        isLocked = true;
    }

    void unlock() {
        isLocked = false;
    }

    int getWidth() {
        return grid.getWidth();
    }

    int getHeight() {
        return grid.getHeight();
    }

    boolean getCell(final int x, final int y) {
        return grid.getCell(x, y);
    }

    boolean isLocked() {
        return isLocked;
    }

    // TODO this method can probably be simplified
    private Grid step(final Grid in) {
        final Grid newGrid = new Grid(in.getHeight(), in.getWidth());
        int liveNeighbours;
        for (int y = 0; y < in.getHeight(); y++) {
            for (int x = 0; x < in.getWidth(); x++) {
                liveNeighbours = countLiveNeighbours(in, x, y);
                if (liveNeighbours <= 1) {
                    newGrid.setCell(x, y, false);
                } else if (liveNeighbours == 3) {
                    newGrid.setCell(x, y, true);
                } else if (liveNeighbours > 3) {
                    newGrid.setCell(x, y, false);
                } else {
                    newGrid.setCell(x, y, in.getCell(x, y));
                }
            }
        }
        return newGrid;
    }


    private int countLiveNeighbours(final Grid grid, final int x, final int y) {
        int neighbors = 0;

        // right
        if (grid.getCell(x + 1, y)) {
            neighbors++;
        }
        // bottom right
        if (grid.getCell(x + 1, y + 1)) {
            neighbors++;
        }
        // bottom
        if (grid.getCell(x, y + 1)) {
            neighbors++;
        }
        // bottom left
        if (grid.getCell(x - 1, y + 1)) {
            neighbors++;
        }
        // left
        if (grid.getCell(x - 1, y)) {
            neighbors++;
        }
        // top left
        if (grid.getCell(x - 1, y - 1)) {
            neighbors++;
        }
        // top
        if (grid.getCell(x, y - 1)) {
            neighbors++;
        }
        // top right
        if (grid.getCell(x + 1, y - 1)) {
            neighbors++;
        }
        return neighbors;
    }

    @Override
    public String toString() {
        lock();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.getHeight(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (grid.getCell(j, i)) {
                    sb.append("x");
                } else {
                    sb.append(" ");
                }
            }
            if (i < grid.getWidth() - 1) {
                sb.append("\n");
            }
        }
        unlock();
        return sb.toString();
    }

}
