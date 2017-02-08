package uk.co.hughingram.lifedemo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the PatternParser class.
 */
public class PatternParserTest {

    private PatternParser patternParser;
    private final String gosperGliderGunRle =
            "#CXRLE Pos=-15,-10\n" +
            "x = 36, y = 9, rule = B3/S23\n" +
            "24bo$22bobo$12b2o6b2o12b2o$11bo3bo4b2o12b2o$2o8bo5bo3b2o$2o8bo3bob2o4b\n" +
            "obo$10bo5bo7bo$11bo3bo$12b2o!";

    private final String testPatternRle =
            "#CXRLE Pos=-30,-7\n" +
            "x = 18, y = 9, rule = B3/S23\n" +
            "2o2b2o2b4o2bo$2o2b2o2b4o2bo3$18o2$2o2b2o2b4o2bo2$obobobobobobobo!";

    @Before
    public void setUp() {
        patternParser = new PatternParser();
    }

    // TODO write some tests m8
    @Test
    public void testArraySizing() {
//        final boolean[][] gosperGrid = patternParser.setUpArray(gosperGliderGunRle);
//        assertEquals(gosperGrid.length, 9);
//        assertEquals(gosperGrid[0].length, 36);
    }

    @Test
    public void testArrayReading() {
//        final boolean[][] testGrid = patternParser.setUpArray(testPatternRle);
//        final boolean[][] gosperGrid = patternParser.setUpArray(gosperGliderGunRle);
//        System.out.println(render(testGrid));
    }

}
