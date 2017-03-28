package uk.co.hughingram.lifedemo.model.logicell_wrapper;


import uk.co.hughingram.lifedemo.model.logicell.CCells;

/**
 * Utility for converting blocks to 8 * 8 boolean grids.
 */
public final class BlockParser {

    /**
     * Converts a Block into a readable boolean grid.
     * @param block the Block to convert.
     * @return the boolean array.
     */
    boolean[] getGrid(final CCells block) {
        final String binary = Long.toBinaryString(block.getCellsVal());
        String paddedBinary = String.format("%64s", binary).replace(' ', '0');
        final boolean[] array = new boolean[paddedBinary.length()];
        for (int i = 0; i < paddedBinary.length(); i++) {
            array[i] =  (paddedBinary.charAt(i) == '1');
        }
        return array;
    }
}
