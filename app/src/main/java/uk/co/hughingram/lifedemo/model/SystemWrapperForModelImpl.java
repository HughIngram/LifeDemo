package uk.co.hughingram.lifedemo.model;

import android.app.Activity;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.co.hughingram.lifedemo.R;

/**
 * Implementation of the System wrapper for the Model.
 */
public final class SystemWrapperForModelImpl implements SystemWrapperForModel {

    private final Activity activity;

    public SystemWrapperForModelImpl(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getStringFromRawResource(@RawRes int resId) {
        try {
            InputStream is = activity.getResources().openRawResource(resId);
            String statesText = convertStreamToString(is);
            is.close();
            return statesText;
        } catch (final IOException e) {
            Log.e("", "ugh");
            return null;
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        return baos.toString();
    }
}
