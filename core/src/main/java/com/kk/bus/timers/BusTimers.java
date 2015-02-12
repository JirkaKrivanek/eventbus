package com.kk.bus.timers;


import com.kk.bus.Bus;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager of the bus timers.
 * <p/>
 * It implements the background thread which powers the timed events delivery for multiple timer objects.
 *
 * @author Jiri Krivanek
 */
public class BusTimers extends Thread {

    private final Bus            mBus;
    private final List<BusTimer> mActiveTimers;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public BusTimers(Bus bus) {
        super();
        mBus = bus;
        mActiveTimers = new ArrayList<>();
        setName("Bus timers");
        setDaemon(true);
        start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                synchronized (this) {
                    long currentMs = getCurrentMs();
                    establishTicksBaseline(currentMs);
                    BusTimer busTimer = findSoonestTimer();
                    if (busTimer == null) {
                        wait();
                        currentMs = getCurrentMs();
                    } else {
                        long sleepMs = busTimer.mNextTickMs - currentMs;
                        if (sleepMs > 0) {
                            wait(sleepMs);
                            currentMs = getCurrentMs();
                        }
                    }
                    fireEventOnExpiredTimers(currentMs);
                }
            }
        } catch (InterruptedException e) {
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    synchronized void addTimer(BusTimer busTimer) {
        mActiveTimers.add(busTimer);
        notify();
    }

    synchronized void removeTimer(BusTimer busTimer) {
        mActiveTimers.remove(busTimer);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void establishTicksBaseline(long currentMs) {
        for (BusTimer busTimer : mActiveTimers) {
            busTimer.establishTicksBaseline(currentMs);
        }
    }

    private BusTimer findSoonestTimer() {
        BusTimer retBusTimer = null;
        long minMs = Long.MAX_VALUE;
        for (BusTimer busTimer : mActiveTimers) {
            if (minMs > busTimer.mNextTickMs) {
                minMs = busTimer.mNextTickMs;
                retBusTimer = busTimer;
            }
        }
        return retBusTimer;
    }

    private void fireEventOnExpiredTimers(long currentMs) {
        for (BusTimer busTimer : mActiveTimers) {
            long tickInMs = busTimer.mNextTickMs - currentMs;
            if (tickInMs <= 0) {
                busTimer.handleTimerTick(mBus);
            }
        }
    }

    private long getCurrentMs() {
        return System.currentTimeMillis();
    }
}
