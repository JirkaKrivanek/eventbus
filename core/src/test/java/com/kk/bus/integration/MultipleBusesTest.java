package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.Produce;
import com.kk.bus.Subscribe;
import com.kk.bus.thread.BusThread;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class MultipleBusesTest {

    private Bus mBus1;
    private Bus mBus2;


    public static class EventA {}


    public static class EventB {}


    public static class ProducerClass extends BusThread {

        private CountDownLatch mCounterA;
        private CountDownLatch mCounterB;

        public ProducerClass(Bus bus, int countA, int countB) throws InterruptedException {
            super(bus);
            mCounterA = new CountDownLatch(countA);
            mCounterB = new CountDownLatch(countB);
            startAndWait();
        }

        @Produce
        public EventA produceEventA() {
            mCounterA.countDown();
            return new EventA();
        }

        @Produce
        public EventB produceEventB() {
            mCounterB.countDown();
            return new EventB();
        }

        public void waitCountersAndAssertCounts() throws InterruptedException {
            assertTrue(mCounterA.await(2, TimeUnit.SECONDS));
            assertTrue(mCounterB.await(2, TimeUnit.SECONDS));
        }
    }


    public static class SubscriberClass extends BusThread {

        private CountDownLatch mCounterA;
        private CountDownLatch mCounterB;

        public SubscriberClass(Bus bus, int countA, int countB) throws InterruptedException {
            super(bus);
            mCounterA = new CountDownLatch(countA);
            mCounterB = new CountDownLatch(countB);
            startAndWait();
        }

        @Subscribe
        public void onEventA(EventA event) {
            mCounterA.countDown();
        }

        @Subscribe
        public void onEventB(EventB event) {
            mCounterB.countDown();
        }

        public void waitCountersAndAssertCounts() throws InterruptedException {
            assertTrue(mCounterA.await(2, TimeUnit.SECONDS));
            assertTrue(mCounterB.await(2, TimeUnit.SECONDS));
        }
    }

    @Before
    public void setUp() {
        mBus1 = new Bus();
        mBus2 = new Bus();
    }

    @Test
    public void checkBusIndependence() throws Exception {

        // Create and register bus threads for producers and subscribers and configure the expected counts of events
        ProducerClass producer1 = new ProducerClass(mBus1, 1, 1);
        SubscriberClass subscriber1 = new SubscriberClass(mBus1, 4, 4);

        ProducerClass producer2 = new ProducerClass(mBus2, 1, 1);
        SubscriberClass subscriber2 = new SubscriberClass(mBus2, 2, 2);

        // Post some events to both buses - intentionally different counts per bus
        mBus1.post(new EventA());
        mBus1.post(new EventB());
        mBus1.post(new EventA());
        mBus1.post(new EventB());
        mBus1.post(new EventA());
        mBus1.post(new EventB());

        mBus2.post(new EventA());
        mBus2.post(new EventB());

        // Wait the production and subscription to happen and insist on the configured counts
        producer1.waitCountersAndAssertCounts();
        producer2.waitCountersAndAssertCounts();

        subscriber1.waitCountersAndAssertCounts();
        subscriber2.waitCountersAndAssertCounts();

        // Terminate the threads and assert the in-time finish
        assertTrue(subscriber1.terminateAndWait(1000));
        assertTrue(producer1.terminateAndWait(1000));

        assertTrue(producer2.terminateAndWait(1000));
        assertTrue(subscriber2.terminateAndWait(1000));
    }
}
