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
final class PatternParser {

    private final static String TAG = "PatternParser";

    boolean[][] parsePatternFile(final File file) {
        final String patternText = getPatternFileContent(file);
        boolean[][] array = setUpArray(patternText);
        // populate fill the array
        return array;
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
    private boolean[][] setUpArray(final String patternText) {
        final String dimensionsLine = getDimensionsLine(patternText);

        final String commaDelimiter = "[,]+";
        final String[] tokens = dimensionsLine.split(commaDelimiter);
        // tokens[0] contains "x = ???",  and tokens[1] contains "y = ???"
        Pattern p = Pattern.compile("[\\d]+");
        final int width = getDimensionFromToken(tokens[0], p);
        final int height = getDimensionFromToken(tokens[1], p);

        return initialiseArray(height, width);
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

    private int getDimensionFromToken(final String token, final Pattern p) {
        final Matcher m = p.matcher(token);
        if (m.find()) {
            final String x = m.group();
            return Integer.valueOf(x);
        } else {
            return -1;
        }
    }

    private boolean[][] initialiseArray(final int height, final int width) {
        boolean[][] array = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                array[i][j] = false;
            }
        }
        return array;
    }

}
