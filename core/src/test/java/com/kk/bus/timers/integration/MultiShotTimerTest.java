package com.kk.bus.timers.integration;


import com.kk.bus.Bus;
import com.kk.bus.timers.BusTimerMultiShot;
import com.kk.bus.timers.BusTimers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.kk.bus.KkAsserts.assertHigherOrEqual;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class MultiShotTimerTest {

    @Test
    public void normalMultipleTick() throws Exception {
        final long TOLERANCE_MS = 1000;
        final long PERIOD_MS = 3000;
        final int REPEAT_COUNT = 3;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerMultiShot timer = new BusTimerMultiShot(busTimers, event, PERIOD_MS, REPEAT_COUNT);
        long toTick1OnMs = busTimers.getCurrentMs() + 1 * PERIOD_MS;
        long toTick2OnMs = busTimers.getCurrentMs() + 2 * PERIOD_MS;
        long toTick3OnMs = busTimers.getCurrentMs() + 3 * PERIOD_MS;
        timer.start();
        // Tick #1
        {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    countDownLatch.countDown();
                    return null;
                }
            }).when(bus).post(event);
            //
            assertTrue(countDownLatch.await(PERIOD_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertHigherOrEqual(busTimers.getCurrentMs(), toTick1OnMs);
        }
        // Tick #2
        {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    countDownLatch.countDown();
                    return null;
                }
            }).when(bus).post(event);
            //
            assertTrue(countDownLatch.await(PERIOD_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertHigherOrEqual(busTimers.getCurrentMs(), toTick2OnMs);
        }
        // Tick #3
        {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    countDownLatch.countDown();
                    return null;
                }
            }).when(bus).post(event);
            //
            assertTrue(countDownLatch.await(PERIOD_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertHigherOrEqual(busTimers.getCurrentMs(), toTick3OnMs);
        }
        // No more tick
        {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    countDownLatch.countDown();
                    return null;
                }
            }).when(bus).post(event);
            //
            assertFalse(countDownLatch.await(PERIOD_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
        }
    }
}
