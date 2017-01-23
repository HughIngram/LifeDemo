package uk.co.hughingram.lifedemo.model;

import android.support.annotation.RawRes;

import uk.co.hughingram.lifedemo.R;

/**
 * Interface to isolate the Model from Android APIs.
 */
public interface SystemWrapperForModel {

    @RawRes int DEFAULT = R.raw.gosper_glider_gun;

    String getStringFromRawResource(@RawRes final int resId);

}
