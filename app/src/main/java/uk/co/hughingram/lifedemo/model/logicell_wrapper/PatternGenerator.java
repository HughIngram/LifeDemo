package uk.co.hughingram.lifedemo.model.logicell_wrapper;

import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

import uk.co.hughingram.lifedemo.model.Grid;
import uk.co.hughingram.lifedemo.model.logicell.CCells;
import uk.co.hughingram.lifedemo.model.logicell.CLogicellUniverse;

// TODO pull PatternParser into an interface shared with PatternParserRle
/**
 * Generates grids via Logicell.
 */
public final class PatternGenerator {

    private final static String TAG = "PatternGen";
    private CLogicellUniverse logicellUniverse;
    private final int BLOCK_LENGTH = 8;

    public PatternGenerator() {
        logicellUniverse = new CLogicellUniverse();
    }
    /**
     * Generates a pattern for the given boolean equation.
     */
    public Grid genPattern(final String equation, final boolean[] values) {
        String equationArray[] = new String[1];
        equationArray[0] = equation;
        logicellUniverse.genLogiProblem(equationArray, 1, values, 4);
        final Vector<CCells> blocks = logicellUniverse.getBlocks(); //the Universe

        int width = 0;
        int height = 0;
        for (final CCells c : blocks) {
            if (c.getY() >= height) {
                height = c.getY() + 1;
            }
            if (c.getX() >= width) {
                width = c.getX() + 1;
            }
        }
        final Grid grid = new Grid(height * BLOCK_LENGTH, width * BLOCK_LENGTH);
        final BlockParser parser = new BlockParser();
        final ArrayList<boolean[]> gridStrings = new ArrayList<>();
        for (final CCells c : blocks) {
            gridStrings.add(parser.getGrid(c));
        }

        // parse blocks into the grid
        // b is block index
        for (int b = 0; b < gridStrings.size(); b++) {
            // x and y offsets
            int x = BLOCK_LENGTH * blocks.elementAt(b).getX();
            int y = BLOCK_LENGTH * blocks.elementAt(b).getY();
            // fill out each block
            for (int i = 0; i < BLOCK_LENGTH; i++) {
                for (int j = 0; j < BLOCK_LENGTH; j++) {
                    int cellIndex = i * BLOCK_LENGTH + j;  // the index of the cell within the block
                    grid.setCell(x + j, y + i, gridStrings.get(b)[cellIndex]);
                }
            }
        }

        return grid;
    }
}
