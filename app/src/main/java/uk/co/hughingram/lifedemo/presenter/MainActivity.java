package uk.co.hughingram.lifedemo.presenter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import uk.co.hughingram.lifedemo.R;
import uk.co.hughingram.lifedemo.model.AppModel;
import uk.co.hughingram.lifedemo.model.AppModelImpl;
import uk.co.hughingram.lifedemo.model.SystemWrapperForModel;
import uk.co.hughingram.lifedemo.model.SystemWrapperForModelImpl;
import uk.co.hughingram.lifedemo.view.AppView;
import uk.co.hughingram.lifedemo.view.GridGraphic;
import uk.co.hughingram.lifedemo.view.SystemWrapperForViewImpl;
import uk.co.hughingram.lifedemo.view.SystemWrapperForView;
import uk.co.hughingram.lifedemo.view.AppViewImpl;

/**
 * Main Activity class. This is effectively part of the Presenter layer (?)
 *
 * This class should be responsible for lifecycle events.
 *
 * This class should not be responsible for View logic.
 *
 */
public final class MainActivity extends AppCompatActivity
        implements FormulaInputDialog.FormulaInputDialogListener {

    final AppPresenterImpl presenterImpl = new AppPresenterImpl();
    AppView view;
    AppModel model;
    private final static int PERMISSION_STORAGE = 1;

    // I'm not sure this is the best place to hold this field ...
    private GridGraphic gridGraphic;

    final SeekBar.OnSeekBarChangeListener seekBarChangeListener
            = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
            model.setSpeed(seekBar.getProgress());
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {

        }
    };

    /**  this is called on Rotate! not what i want... */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO is there a more elegant way to do this?
        final SystemWrapperForView systemWrapperForView = new SystemWrapperForViewImpl(this);
        final SystemWrapperForModel systemWrapperForModel = new SystemWrapperForModelImpl(this);
        model = new AppModelImpl(systemWrapperForModel);
        view = new AppViewImpl(systemWrapperForView, presenterImpl);
        model.setPresenter(presenterImpl);
        presenterImpl.setModel(model);
        presenterImpl.setView(view);

        final ViewGroup vg = (ViewGroup) findViewById(R.id.grid_holder);
        gridGraphic = new GridGraphic(this.getApplicationContext(), presenterImpl);
        vg.addView(gridGraphic);
        gridGraphic.run();

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar_speed);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        seekBar.setProgress(500);
    }

    @Override
    public void onResume() {
        super.onResume();
        model.setUpSimulation();
        getWriteStoragePermission();
        gridGraphic.start();
    }

    private void getWriteStoragePermission() {
        final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                  Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseSimulation();
    }

    private void pauseSimulation() {
        view.showStatePaused();
        model.pauseSimulation();
        gridGraphic.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        gridGraphic.invalidate();
        if (item.getGroupId() == AppViewImpl.PATTERNS_GROUP_ID) {
            Log.d("Activity", "Loading " + item.getTitle().toString());
            model.loadPattern(item.getTitle().toString());
        }
        int id = item.getItemId();
        if (id == R.id.menu_item_step) {
            model.iterateOnce();
        }
        if (id == R.id.formula) {
            gridGraphic.pause();
            FragmentManager fm = getSupportFragmentManager();
            FormulaInputDialog editNameDialog = new FormulaInputDialog();
            editNameDialog.show(fm, "fragment_formula");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final SubMenu subMenu = menu.findItem(R.id.menu_item_load).getSubMenu();
        view.setPatternChoices(subMenu, model.getAvailablePatterns());
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onFinishEditDialog(final String inputText, boolean[] values) {
        model.createPattern(inputText, values);
        gridGraphic.invalidate();
        gridGraphic.start();
        Toast.makeText(this, "Pattern to solve equation: " + inputText, Toast.LENGTH_SHORT).show();
    }

}
