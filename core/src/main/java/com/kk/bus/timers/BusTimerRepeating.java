package com.kk.bus.timers;


import com.kk.bus.Bus;

/**
 * Bus timer class: Repeating until stopped.
 * <p/>
 * This timer ticks forever with the specified period in milliseconds until explicitly stopped.
 *
 * @author Jiri Krivanek
 */
public class BusTimerRepeating extends BusTimer {

    private Object  mEvent;
    private boolean mRunning;
    private long    mPeriodMs;

    /**
     * Constructs the timer.
     *
     * @param event
     *         The event object to deliver regularly.
     * @param periodMs
     *         The delay in milliseconds after which the event fil be delivered.
     */
    public BusTimerRepeating(BusTimers busTimers, Object event, long periodMs) {
        super(busTimers);
        mEvent = event;
        mRunning = false;
        mPeriodMs = periodMs;
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
            mNextTickMs = currentTimeMs + mPeriodMs;
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
                mNextTickMs += mPeriodMs;
                event = mEvent;
            }
        }
        if (event != null) {
            bus.post(event);
        }
    }
}
