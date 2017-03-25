package uk.co.hughingram.lifedemo.model.logicell_wrapper;

import android.util.Log;

import java.util.Vector;

import uk.co.hughingram.lifedemo.model.Grid;
import uk.co.hughingram.lifedemo.model.logicell.CLogicellUniverse;

/**
 * Generates grids via Logicell.
 */
public final class PatternGenerator {

    private final static String TAG = "PatternGen";
    private CLogicellUniverse logicellUniverse;

    public PatternGenerator() {
        logicellUniverse = new CLogicellUniverse();
    }
    /**
     * Generates a pattern for the given boolean equation.
     */
    public Grid genPattern(final String equation) {
        String mysteryStringArray[] = new String[1];
        mysteryStringArray[0] = equation;
        final boolean[] booleans = {false, false, false, false};
        logicellUniverse.GenLogiProblem(mysteryStringArray, 1, booleans, 4);

        // blocks represents all blocks in the universe
        final Vector blocks = logicellUniverse.getBlocks();

        Log.d(TAG, "size: " + blocks.size());
        // how to get dimensions of universe??
        // logicellUniverse.patHeight is not used.
        // cellBlocs.size() might return the number of 8x8 grids overall?
        // what about counting - e.g.
        // while(cells.SBloc != null) i++
        return null;
    }
}
