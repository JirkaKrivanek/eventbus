package com.kk.bus.timers;


import com.kk.bus.Bus;

/**
 * Bus timer interface.
 *
 * @author Jiri Krivanek
 */
public abstract class BusTimer {

    protected final BusTimers mBusTimers;
    long mNextTickMs; // Important: Zero or less means the baseline was not set yet. Otherwise means pending/running.

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs the basic timer enforcing the timers manager to be set.
     *
     * @param busTimers
     *         The timers manager to set.
     */
    public BusTimer(BusTimers busTimers) {
        mBusTimers = busTimers;
    }

    /**
     * Just hides the default constructor to avoid the uninitialized bus timer to be created.
     */
    @SuppressWarnings("unused")
    private BusTimer() {
        mBusTimers = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks whether timer is marked as running or not.
     *
     * @return If running then {@code true} else {@code false}.
     */
    public abstract boolean isRunning();

    /**
     * Starts the timer.
     */
    public abstract void start();

    /**
     * Stops the timer.
     */
    public abstract void stop();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Calculates the scheduled next tick timestamp in milliseconds.
     *
     * @param currentTimeMs
     *         The current timestamp in milliseconds.
     */
    abstract void establishTicksBaseline(long currentTimeMs);

    /**
     * Performs the desired action when the timer ticks.
     *
     * @param bus
     *         The bus onto which the event should be posted.
     */
    abstract void handleTimerTick(Bus bus);
}
