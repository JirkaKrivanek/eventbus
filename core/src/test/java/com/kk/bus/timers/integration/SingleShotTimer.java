package com.kk.bus.timers.integration;


import com.kk.bus.Bus;
import com.kk.bus.timers.BusTimerSingleShot;
import com.kk.bus.timers.BusTimers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class SingleShotTimer {

    @Test
    public void normalDelivery() throws Exception {
        final long TOLERANCE_MS = 1000;
        final long DELAY_MS = 5000;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerSingleShot timer = new BusTimerSingleShot(busTimers, event, DELAY_MS);
        //
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                countDownLatch.countDown();
                return null;
            }
        }).when(bus).post(event);
        //
        long toTickOnMs = busTimers.getCurrentMs() + DELAY_MS;
        timer.start();
        assertTrue(countDownLatch.await(TOLERANCE_MS + DELAY_MS, TimeUnit.MILLISECONDS));
        assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
    }
}
