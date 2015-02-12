package com.kk.bus.timers;


import com.kk.bus.Bus;

/**
 * Bus timer class: One-time shot.
 * <p/>
 * This timer ticks exactly once after the specified delay in milliseconds unless explicitly stopped earlier.
 *
 * @author Jiri Krivanek
 */
public class BusTimerSingleShot extends BusTimer {

    private Object  mEvent;
    private boolean mRunning;
    private long    mDelayMs;

    /**
     * Constructs the timer.
     *
     * @param event
     *         The event object to deliver regularly.
     * @param delayMs
     *         The delay in milliseconds after which the event fil be delivered.
     */
    public BusTimerSingleShot(BusTimers busTimers, Object event, long delayMs) {
        super(busTimers);
        mEvent = event;
        mRunning = false;
        mDelayMs = delayMs;
    }

    public Object getEvent() {
        return mEvent;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isRunning() {
        return mRunning;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void start() {
        mRunning = true;
        mNextTickMs = 0; // This determines the baseline for the ticks calculations
        mBusTimers.addTimer(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void stop() {
        mRunning = false;
        mBusTimers.removeTimer(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized void establishTicksBaseline(long currentTimeMs) {
        if (mNextTickMs <= 0) {
            mNextTickMs = currentTimeMs + mDelayMs;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleTimerTick(Bus bus) {
        Object event = null;
        synchronized (this) {
            if (mRunning) {
                stop();
                event = mEvent;
            }
        }
        if (event != null) {
            bus.post(event);
        }
    }
}
