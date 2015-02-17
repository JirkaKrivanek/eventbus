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

public class BusTimerSingleShotTest {

    @Test
    public void constructor() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, new Object(), 0);
        assertNotNull(busTimer);
    }

    @Test
    public void testSetGetEvent() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, 0);
        assertSame(event, busTimer.getEvent());
        event = new Object();
        busTimer.setEvent(event);
        assertSame(event, busTimer.getEvent());
    }

    @Test
    public void testIsRunning() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, 0);
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
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, 0);
        busTimer.start();
        verify(busTimers).addTimer(busTimer);
    }

    @Test
    public void testStop() throws Exception {
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, 0);
        busTimer.stop();
        verify(busTimers).removeTimer(busTimer);
    }

    @Test
    public void testEstablishTicksBaseline() throws Exception {
        final int PERIOD_MS = 100;
        final int CURRENT_MS = 999;
        //
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, PERIOD_MS);
        //
        busTimer.start();
        busTimer.establishTicksBaseline(CURRENT_MS);
        assertEquals(CURRENT_MS + PERIOD_MS, busTimer.mNextTickMs);
        //
        busTimer.handleTimerTick(mock(Bus.class));
        busTimer.establishTicksBaseline(CURRENT_MS);
        assertEquals(CURRENT_MS + PERIOD_MS, busTimer.mNextTickMs);
    }

    @Test
    public void testHandleTimerTick() throws Exception {
        Bus bus = mock(Bus.class);
        BusTimers busTimers = mock(BusTimers.class);
        Object event = new Object();
        BusTimerSingleShot busTimer = new BusTimerSingleShot(busTimers, event, 0);
        //
        busTimer.start();
        assertTrue(busTimer.isRunning());
        //
        busTimer.handleTimerTick(bus);
        assertFalse(busTimer.isRunning());
        //
        busTimer.handleTimerTick(bus);
        assertFalse(busTimer.isRunning());
        //
        busTimer.handleTimerTick(bus);
        assertFalse(busTimer.isRunning());
        //
        busTimer.handleTimerTick(bus);
        assertFalse(busTimer.isRunning());
        //
        verify(bus, times(1)).post(event);
    }
}
