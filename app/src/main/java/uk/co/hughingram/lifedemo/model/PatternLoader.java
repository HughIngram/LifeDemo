package uk.co.hughingram.lifedemo.model;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to read Patterns from the file system.
 */
final class PatternLoader implements FileHelper {

    private final static String PATTERNS_DIR = "/Patterns/";
    private final static String TAG = "PatternLoader";

    @Override
    public String[] getPatternList() {
        final File directory = new File(Environment.getExternalStorageDirectory(), PATTERNS_DIR);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "failed to make patterns dir");
            }
        }
        if (directory.listFiles() != null) {
            final File[] files = directory.listFiles();
            final String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            return fileNames;
        } else {
            return new String[0];
        }
    }

    @Override
    public boolean[][] loadPattern(final String patternId) {
        final File directory = new File(Environment.getExternalStorageDirectory(), PATTERNS_DIR);
        final File patternFile = new File(directory, patternId);
        final PatternParser parser = new PatternParser();
        return parser.parsePatternFile(patternFile);
    }

}
