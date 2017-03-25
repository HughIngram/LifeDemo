package uk.co.hughingram.lifedemo.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to parse .rle files to grid arrays.
 */
final class PatternParserRle {

    private final static String TAG = "PatternParserRle";

    Grid parsePatternFile(final File file) {
        final String patternText = getPatternFileContent(file);
        return setUpArray(patternText);
    }

    // note : make unit tests for this! - write up why unit testing was done
    private String getPatternFileContent(final File patternFile) {
        final StringBuilder text = new StringBuilder();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(patternFile));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (final IOException e) {
            Log.e(TAG, "Reading pattern file failed");
        }
        return text.toString();
    }

    // creates an array of the correct size for the given pattern, with all values set to false.
    Grid setUpArray(final String patternText) {
        final String dimensionsLine = getDimensionsLine(patternText);

        final String commaDelimiter = "[,]+";
        final String[] tokens = dimensionsLine.split(commaDelimiter);
        // tokens[0] contains "x = ???",  and tokens[1] contains "y = ???"
        // This regex look for numerical digits.
        final int width = getDimensionFromToken(tokens[0]);
        final int height = getDimensionFromToken(tokens[1]);

        final Grid blankGrid = new Grid(height, width);
        populateGrid(blankGrid, patternText);
        return blankGrid;
    }

    private String getDimensionsLine(final String patternText) {
        int startOfSizeLine = -1;
        int endOfSizeLine = -1;
        boolean isStartOfLine = true;
        // find the index of the line which describes the dimensions of the pattern.
        for (int i = 0; i < patternText.length(); i++) {
            if (isStartOfLine && patternText.charAt(i) == 'x') {
                startOfSizeLine = i;
                while (patternText.charAt(i) != '\n') {
                    i++;
                }
                endOfSizeLine = i;
            }
            isStartOfLine = (patternText.charAt(i) == '\n');
        }
        return patternText.substring(startOfSizeLine, endOfSizeLine);
    }

    private int getDimensionFromToken(final String token) {
        final Pattern p = Pattern.compile("[\\d]+");
        final Matcher m = p.matcher(token);
        if (m.find()) {
            final String x = m.group();
            return Integer.valueOf(x);
        } else {
            return -1;
        }
    }

    //look the first line which starts with a number or a letter, but not 'x'
    // ignore the possibility of a #comment in the middle of the pattern
    private void populateGrid(final Grid grid, final String patternText) {
        //first step: extract the rle code only
        String rle = getRleSection(patternText);
        //delete all whitespace from this string...
        rle = rle.replaceAll("\\s","");

        // each string needs to be split by a single instance of 'o' or 'b'
        // \D will match any non-digit. Good enough.
        final Pattern p = Pattern.compile("(\\d+\\D)|\\D");
        int i = 0;  //row index
        int j = 0;  //column index
        //fill each line
        Matcher matcher = p.matcher(rle);
        while (matcher.find()) {
            final String run = matcher.group();
            if (run.length() > 1) {
                final int runLength = getDimensionFromToken(run);
                final char c = run.charAt(run.length() - 1);
                for (int k = 0; k < runLength; k++) {
                    if (c == 'o') {
//                        array[i][j] = true;
                        grid.setCell(j, i, true);
                        j++;
                    } else if (c == 'b') {
//                        array[i][j] = false;
                        grid.setCell(j, i, false);
                        j++;
                    } else if (c == '$') {
                        i++;
                        j = 0;
                    }
                }
            } else {
                // run length is 1;
                final char c = run.charAt(0);
                if (c == 'o') {
//                    array[i][j] = true;
                    grid.setCell(j, i, true);
                    j++;
                } else if (c == 'b') {
//                    array[i][j] = false;
                    grid.setCell(j, i, false);
                    j++;
                } else if (c == '$') {
                    i++;
                    j = 0;
                }
            }

        }
    }

    private String getRleSection(final String patternText) {
        boolean isStartOfLine = false;
        for (int i = 0; i < patternText.length(); i++) {
            if (isStartOfLine && characterIsStartOfRle(patternText.charAt(i))) {
                return patternText.substring(i, patternText.length() -1);
            }
            isStartOfLine = (patternText.charAt(i) == '\n');
        }
        return null;
    }

    private boolean characterIsStartOfRle(final char c) {
        return (Character.isLetter(c) || Character.isDigit(c)) && c != 'x';
    }

}
