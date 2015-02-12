package com.kk.bus.timers;


import com.kk.bus.Bus;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BusTimerRepeatingTest {

    @Test
    public void constructor() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, new Object(), 0);
        assertNotNull(busTimer);
    }

    @Test
    public void testGetEvent() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, 0);
        assertSame(event, busTimer.getEvent());
    }

    @Test
    public void testIsRunning() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, 0);
        assertFalse(busTimer.isRunning());
        busTimer.start();
        assertTrue(busTimer.isRunning());
        busTimer.stop();
        assertFalse(busTimer.isRunning());
    }

    @Test
    public void testStart() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, 0);
        busTimer.start();
        verify(busTimers).addTimer(busTimer);
    }

    @Test
    public void testStop() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, 0);
        busTimer.stop();
        verify(busTimers).removeTimer(busTimer);
    }

    @Test
    public void testEstablishTicksBaseline() throws Exception {
        final int COUNT = 100000;
        final long PERIOD_MS = 100;
        final long CURRENT_MS = 999;
        long nextTickMs;
        //
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, PERIOD_MS);
        //
        busTimer.start();
        busTimer.establishTicksBaseline(CURRENT_MS);
        nextTickMs = CURRENT_MS + PERIOD_MS;
        assertEquals(nextTickMs, busTimer.mNextTickMs);
        //
        for (int i = 0; i < COUNT; i++) {
            busTimer.handleTimerTick(mock(Bus.class));
            busTimer.establishTicksBaseline(CURRENT_MS);
            nextTickMs += PERIOD_MS;
            assertEquals(nextTickMs, busTimer.mNextTickMs);
        }
        //
        busTimer.start();
        busTimer.establishTicksBaseline(CURRENT_MS);
        assertEquals(CURRENT_MS + PERIOD_MS, busTimer.mNextTickMs);
    }

    @Test
    public void testHandleTimerTick() throws Exception {
        final int COUNT = 100000;
        //
        Bus bus = mock(Bus.class);
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerRepeating busTimer = new BusTimerRepeating(busTimers, event, 0);
        //
        busTimer.start();
        assertTrue(busTimer.isRunning());
        //
        for (int i = 0; i < COUNT; i++) {
            busTimer.handleTimerTick(bus);
        }
        //
        busTimer.stop();
        assertFalse(busTimer.isRunning());
        //
        for (int i = 0; i < COUNT; i++) {
            busTimer.handleTimerTick(bus);
        }
        //
        verify(bus, times(COUNT)).post(event);
    }
}
