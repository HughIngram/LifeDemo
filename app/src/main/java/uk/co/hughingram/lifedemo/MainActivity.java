package uk.co.hughingram.lifedemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//TODO create a Grid implements Iterator class
public final class MainActivity extends AppCompatActivity {

    public final static int GRID_WIDTH = 16;
    public final static int GRID_HEIGHT= 12;
    private boolean[][] grid = makeGrid(GRID_WIDTH, GRID_HEIGHT);
    private TextView gridView;
    private boolean running = false;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (TextView) findViewById(R.id.grid);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                running = !running;
                runSimulation(running);
                Log.d("TAG", renderGrid(grid));
            }
        });
        showGrid(renderGrid(grid));
        Log.d("TAG", renderGrid(grid));
    }

    final Runnable render = new Runnable() {
        @Override
        public void run() {
            showGrid(renderGrid(grid));
            Log.d("TAG", renderGrid(grid));
        }
    };

    private void runSimulation(final boolean running) {
        if (running) {
            TimerTask timerTask = genTimerTask();
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 100);
        } else {
            timer.cancel();
        }
    }

    private TimerTask genTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                grid = step(grid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render.run();
                    }
                });
                try {
                    Thread.sleep(200);
                } catch (final InterruptedException e){
                    Log.e("TAG", "no", e);
                }
            }

        };
    }

    private void showGrid(String grid) {
        gridView.setText(grid);
    }

    //returns a glider in a 16*12 grid
    private boolean[][] makeGrid(final int width, final int height) {
        return new boolean[][]{
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

            {false,false,false,false,false,true, false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,true, true, true,false,false,false,false,false,false,false,false,false},

            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false}
        };

    }

    private boolean[][] makeGridRandom(final int width, final int height) {
        final Random random = new Random();
        final boolean[][] grid = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = random.nextBoolean();
            }
        }
        return grid;
    }

    // for now just use ASCII for rendering!
    private String renderGrid(final boolean[][] grid) {
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
        return sb.toString();
    }

    // build up a new grid to replace the old one - the old must be preserved for comparison
    private boolean[][] step(final boolean[][] grid) {
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
        return newGrid;
    }

    // wrap around edges
    private int countLiveNeighbours(final boolean[][] grid, final int x, final int y) {
        int neighbors = 0;
        int xOfRhs = (x + 1) % grid[0].length;
        int xOfLhs;
        if (x == 0) {
            xOfLhs = grid[0].length - 1;
        } else {
            xOfLhs = x - 1;
        }
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
