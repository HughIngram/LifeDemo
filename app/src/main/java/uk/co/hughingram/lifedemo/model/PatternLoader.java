package uk.co.hughingram.lifedemo.model;

import android.os.Environment;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.File;

/**
 * Class to read Patterns from the file system.
 */
final class PatternLoader implements FileHelper {

    private final static String PATTERNS_DIR = "/Patterns/";
    private final static String TAG = "PatternLoader";

    private final SystemWrapperForModel system;

    PatternLoader(final SystemWrapperForModel system) {
        this.system = system;
    }

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
    public Grid loadPattern(final String patternId) {
        final File directory = new File(Environment.getExternalStorageDirectory(), PATTERNS_DIR);
        final File patternFile = new File(directory, patternId);
        final PatternParser parser = new PatternParser();
        return parser.parsePatternFile(patternFile);
    }

    Grid getDefaultGrid() {
        return loadPatternFromResources(SystemWrapperForModel.DEFAULT);
    }

    private Grid loadPatternFromResources(@RawRes final int id) {
        final String patternRle = system.getStringFromRawResource(id);
        return new PatternParser().setUpArray(patternRle);
    }
}
