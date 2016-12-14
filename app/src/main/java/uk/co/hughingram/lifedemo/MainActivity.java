package uk.co.hughingram.lifedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uk.co.hughingram.lifedemo.model.AppModelImpl;
import uk.co.hughingram.lifedemo.presenter.AppPresenterImpl;
import uk.co.hughingram.lifedemo.view.SystemWrapperForViewImpl;
import uk.co.hughingram.lifedemo.view.ActivityWrapper_View;
import uk.co.hughingram.lifedemo.view.AppViewImpl;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO is there a more elegant way to do this?
        ActivityWrapper_View activityWrapperForView = new SystemWrapperForViewImpl(this);
        AppModelImpl model = new AppModelImpl();
        AppPresenterImpl presenterImpl = new AppPresenterImpl();
        AppViewImpl view = new AppViewImpl(activityWrapperForView, presenterImpl);
        model.setPresenter(presenterImpl);
        presenterImpl.setModel(model);
        presenterImpl.setView(view);

        // TODO set up the app before the button is pressed.
    }

}
