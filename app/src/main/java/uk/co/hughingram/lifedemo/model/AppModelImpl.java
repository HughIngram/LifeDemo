package uk.co.hughingram.lifedemo.model;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

//TODO create a Grid implements Iterator class, with toString() method too
/**
 *  Master class for the AppModelImpl component.
 */
public final class AppModelImpl implements AppModel {

    private final static int INTERVAL_MILLISECONDS = 150;

    private AppPresenterForModel presenter;
    private SystemWrapperForModel system;
    private Grid grid;
    private boolean running = false;
    private Timer timer;


    public AppModelImpl(final SystemWrapperForModel system) {
        grid = new Grid(system);
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
                grid.iterate();
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
            grid.iterate();
            presenter.displayGrid(grid);
        }
    };

    @Override
    public void pauseSimulation() {
        timer.cancel();
        running = !running;
    }

    // for now just use ASCII for rendering!
    @Override
    public String render() {
        // TODO actually render the grid properly
        return grid.toString();
    }

    @Override
    public String[] getAvailablePatterns() {
        return new PatternLoader(system).getPatternList();
    }

    // TODO move stuff like this up, so the Presenter interacts with the Grid directly instead of delegating
    @Override
    public void loadPattern(final String id) {
        grid = new Grid(id, system);
        presenter.displayGrid(grid);
    }

}
