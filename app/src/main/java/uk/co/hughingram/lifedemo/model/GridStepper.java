package uk.co.hughingram.lifedemo.model;

/**
 * Responsible for all logic for iterating between CA steps.
 */
final class GridStepper {

    boolean[][] step(final boolean[][] in) {
        final boolean[][] newGrid = new boolean[in.length][in[0].length];
        int liveNeighbours;
        for (int y = 0; y < in.length; y++) {
            for (int x = 0; x < in[0].length; x++) {
                liveNeighbours = countLiveNeighbours(in, x, y);
                if (liveNeighbours <= 1) {
                    newGrid[y][x] = false;
                } else if (liveNeighbours == 3 || (in[y][x] && liveNeighbours == 2)) {
                    newGrid[y][x] = true;
                } else if (liveNeighbours > 3) {
                    newGrid[y][x] = false;
                } else {
                    newGrid[y][x] = in[y][x];
                }
            }
        }
        return newGrid;
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
}
