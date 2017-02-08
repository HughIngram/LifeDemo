package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

/**
 *  Master class for the AppModelImpl component.
 */
public final class AppModelImpl implements AppModel {

    private final static int INTERVAL_MILLISECONDS = 150;

    private AppPresenterForModel presenter;
    private SystemWrapperForModel system;
    private GridHolder gridHolder;
    private boolean running = false;
    private Timer timer;
    private final Renderer renderer = new Renderer();


    public AppModelImpl(final SystemWrapperForModel system) {
        gridHolder = new GridHolder(new PatternLoader(system).getDefaultGrid());
        this.system = system;
    }

    @Override
    public void setPresenter(final AppPresenterForModel presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isSimulationRunning() {
        return running;
    }

    @Override
    public void runSimulation() {
        TimerTask timerTask = genTimerTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 100);
        running = !running;
    }

    private TimerTask genTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                gridHolder.iterate();
                runnable.run();
                try {
                    Thread.sleep(INTERVAL_MILLISECONDS);
                } catch (final InterruptedException e){
                    Log.e("TAG", "no", e);
                }
            }
        };
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            gridHolder.iterate();
        }
    };

    @Override
    public void pauseSimulation() {
        timer.cancel();
        running = !running;
    }

    @Override
    public void iterateOnce() {
        gridHolder.iterate();
    }

    @Override
    public Bitmap render() {
        return renderer.render(gridHolder);
    }

    @Override
    public String[] getAvailablePatterns() {
        return new PatternLoader(system).getPatternList();
    }

    // TODO move stuff like this up, so the Presenter interacts with the GridHolder directly instead of delegating
    @Override
    public void loadPattern(final String id) {
        gridHolder = new GridHolder(new PatternLoader(system).loadPattern(id));
    }

}