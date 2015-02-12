package com.kk.bus.timers;


import com.kk.bus.Bus;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class BusTimerTest {

    private static class TestTimer extends BusTimer {

        public TestTimer(BusTimers busTimers) {
            super(busTimers);
        }

        @Override
        public boolean isRunning() {
            return false;
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
        }

        @Override
        void establishTicksBaseline(long currentTimeMs) {
        }

        @Override
        void handleTimerTick(Bus bus) {
        }
    }

    @Test
    public void constructor() throws Exception {
        BusTimer busTimer = new TestTimer(mock(BusTimers.class));
        assertNotNull(busTimer);
    }
}
