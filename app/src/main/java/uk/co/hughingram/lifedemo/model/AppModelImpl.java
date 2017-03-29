package uk.co.hughingram.lifedemo.model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.hughingram.lifedemo.model.logicell_wrapper.PatternGenerator;
import uk.co.hughingram.lifedemo.presenter.AppPresenterForModel;

/**
 *  Master class for the AppModelImpl component.
 */
public final class AppModelImpl implements AppModel {

    // default interval between steps in milliseconds.
    private final static int DEFAULT_INTERVAL = 150;
    private int interval = DEFAULT_INTERVAL;

    private AppPresenterForModel presenter;
    private SystemWrapperForModel system;
    private GridHolder gridHolder;
    private boolean running = false;
    private Timer timer;
    private final Renderer renderer = new Renderer();
    final private PatternGenerator patternGenerator;


    public AppModelImpl(final SystemWrapperForModel system) {
        patternGenerator = new PatternGenerator();
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
    public void setUpSimulation() {
        timer = new Timer();
    }

    @Override
    public void runSimulation() {
        TimerTask gridStepTask = genGridStepTask();
        timer.scheduleAtFixedRate(gridStepTask, 0, 100);
        running = !running;
    }

    private TimerTask genGridStepTask() {
        return new TimerTask() {
            @Override
            public void run() {
                gridHolder.iterate();
                try {
                    Thread.sleep(interval);
                } catch (final InterruptedException e){
                    Log.e("TAG", "no", e);
                }
            }
        };
    }

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

    @Override
    public void createPattern(final String equation) {
        final Grid grid = patternGenerator.genPattern(equation);
        gridHolder = new GridHolder(grid);
    }

    @Override
    public void setSpeed(final int speed) {
        if (speed == 0) {
            interval = 1000;
        } else {
            interval = 1000 - speed;
        }
    }

}