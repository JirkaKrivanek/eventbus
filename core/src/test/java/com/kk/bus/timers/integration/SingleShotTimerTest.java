package com.kk.bus.timers.integration;


import com.kk.bus.Bus;
import com.kk.bus.timers.BusTimerSingleShot;
import com.kk.bus.timers.BusTimers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class SingleShotTimerTest {

    @Test
    public void normalSingleTick() throws Exception {
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
        assertTrue(countDownLatch.await(DELAY_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
        assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
    }

    @Test
    public void startedAgainAndAgainAfterTick() throws Exception {
        final long TOLERANCE_MS = 1000;
        final long DELAY_MS = 5000;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerSingleShot timer = new BusTimerSingleShot(busTimers, event, DELAY_MS);
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
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
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
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
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
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void changedTimerDelayWhenRunning() throws Exception {
        final long DELAY_1_MS = 6000;
        final long DELAY_2_MS = 3000;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerSingleShot timer = new BusTimerSingleShot(busTimers, event, DELAY_1_MS);
        // Tick #1: Delay 1
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
            timer.start();
            assertFalse(countDownLatch.await(DELAY_2_MS, TimeUnit.MILLISECONDS));
            timer.setDelayMs(DELAY_2_MS);
        }
    }

    @Test
    public void changedTimerDelayAfterTick() throws Exception {
        final long TOLERANCE_MS = 1000;
        final long DELAY_1_MS = 3000;
        final long DELAY_2_MS = 6000;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerSingleShot timer = new BusTimerSingleShot(busTimers, event, DELAY_1_MS);
        // Tick #1: Delay 1
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
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_1_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_1_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
        }
        // Tick #2: Delay 2
        {
            timer.setDelayMs(DELAY_2_MS);
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    countDownLatch.countDown();
                    return null;
                }
            }).when(bus).post(event);
            //
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_2_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_2_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
        }
    }

    @Test
    public void stoppedBeforeTickAndStartedAgain() throws Exception {
        final long TOLERANCE_MS = 1000;
        final long DELAY_MS = 5000;
        Bus bus = spy(new Bus());
        BusTimers busTimers = new BusTimers(bus);
        Object event = new Object();
        //
        BusTimerSingleShot timer = new BusTimerSingleShot(busTimers, event, DELAY_MS);
        // Stopped just before the tick
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
            timer.start();
            assertFalse(countDownLatch.await(DELAY_MS - TOLERANCE_MS, TimeUnit.MILLISECONDS));
            timer.stop();
            assertFalse(countDownLatch.await(2 * TOLERANCE_MS, TimeUnit.MILLISECONDS));
        }
        // Normal tick #1
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
            long toTickOnMs = busTimers.getCurrentMs() + DELAY_MS;
            timer.start();
            assertTrue(countDownLatch.await(DELAY_MS + TOLERANCE_MS, TimeUnit.MILLISECONDS));
            assertTrue(busTimers.getCurrentMs() >= toTickOnMs);
        }
    }
}
