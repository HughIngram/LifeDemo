package uk.co.hughingram.lifedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import uk.co.hughingram.lifedemo.model.AppModel;
import uk.co.hughingram.lifedemo.model.AppModelImpl;
import uk.co.hughingram.lifedemo.model.SystemWrapperForModel;
import uk.co.hughingram.lifedemo.model.SystemWrapperForModelImpl;
import uk.co.hughingram.lifedemo.presenter.AppPresenterImpl;
import uk.co.hughingram.lifedemo.view.AppView;
import uk.co.hughingram.lifedemo.view.SystemWrapperForViewImpl;
import uk.co.hughingram.lifedemo.view.SystemWrapperForView;
import uk.co.hughingram.lifedemo.view.AppViewImpl;

/**
 * Main Activity class. This is effectively part of the Presenter layer (?)
 *
 * This class should be responsible for lifeclye events.
 *
 * This class should not be responsible for View logic.
 *
 */
public final class MainActivity extends AppCompatActivity {

    final AppPresenterImpl presenterImpl = new AppPresenterImpl();
    AppView view;
    AppModel model;
    private final static int PERMISSION_STORAGE = 1;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImpl.setUpSimulation();
        getWriteStoragePermission();
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
        // pause the simulation.
        // how exactly should this Activity be able to interact with the Presenter?
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getGroupId() == AppViewImpl.PATTERNS_GROUP_ID) {
            Log.d("Activity", "Loading " + item.getTitle().toString());
            model.loadPattern(item.getTitle().toString());
        }
//        int id = item.getItemId();
//        if (id == R.id.menu_item_load) {
//            presenterImpl.onLoadButtonPressed();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        final SubMenu subMenu = menu.findItem(R.id.menu_item_load).getSubMenu();
        view.setPatternChoices(subMenu, model.getAvailablePatterns());
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }
}
