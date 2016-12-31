package uk.co.hughingram.lifedemo.presenter;

/**
 * App Presenter for the Model.
 */

public interface AppPresenterForModel {

    // maybe replace this with a callback?
    // ie the model calls back when it's done rendering
    void displayGrid(final boolean[][] grid);
}
