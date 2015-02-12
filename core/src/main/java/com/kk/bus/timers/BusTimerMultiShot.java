package com.kk.bus.timers;


import com.kk.bus.Bus;

/**
 * Bus timer class: Multi shot.
 * <p/>
 * Tis timer ticks specified number of times with the specified period in milliseconds unless explicitly stopped.
 *
 * @author Jiri Krivanek
 */
public class BusTimerMultiShot extends BusTimer {

    private Object  mEvent;
    private boolean mRunning;
    private long    mPeriodMs;
    private int     mTickCount;
    private int     mTickCounter;

    /**
     * Constructs the timer.
     *
     * @param event
     *         The event object to deliver regularly.
     * @param periodMs
     *         The delay in milliseconds after which the event fil be delivered.
     * @param tickCount
     *         The number of timer ticks to generate with the  specified period.
     */
    public BusTimerMultiShot(BusTimers busTimers, Object event, long periodMs, int tickCount) {
        super(busTimers);
        mEvent = event;
        mRunning = false;
        mPeriodMs = periodMs;
        mTickCount = tickCount;
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
        mTickCounter = 0;
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
        Object event;
        synchronized (this) {
            mTickCounter++;
            if (mTickCounter > mTickCount) {
                stop();
            }
            mNextTickMs += mPeriodMs;
            event = mEvent;
        }
        if (event != null) {
            bus.post(event);
        }
    }
}
