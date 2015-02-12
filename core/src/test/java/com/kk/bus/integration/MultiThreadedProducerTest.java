package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.Produce;
import com.kk.bus.Subscribe;
import com.kk.bus.thread.BusThread;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class MultiThreadedProducerTest {

    private static Bus sBus;


    private static class EventA {}


    private static class EventB {}


    private static class EventC {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @BeforeClass
    public static void setUpTest() {
        DeliveryContextManagers.clearDeliveryContextManagers();
        sBus = new Bus();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Normal production
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static class NormalProduction_ProducerA extends BusThread {

        final CountDownLatch eventCount;

        public NormalProduction_ProducerA(Bus bus, int count) {
            super(bus);
            eventCount = new CountDownLatch(count);
            setDaemon(true);
            setName("Bus: Producer A");
            init();
            start();
        }

        @Produce
        public EventA produceEventA() {
            eventCount.countDown();
            return new EventA();
        }
    }


    public static class NormalProduction_SubscriberA extends BusThread {

        final CountDownLatch eventCount;

        public NormalProduction_SubscriberA(Bus bus, int count) {
            super(bus);
            eventCount = new CountDownLatch(count);
            setDaemon(true);
            setName("Bus: Subscriber A");
            init();
            start();
        }

        @Subscribe
        public void subscribeEventA(EventA event) {
            eventCount.countDown();
        }
    }


    public static class NormalProduction_ProducerAndSubscriberInOneA extends BusThread {

        final CountDownLatch eventProduceCount;
        final CountDownLatch eventSubscribeCount;

        public NormalProduction_ProducerAndSubscriberInOneA(Bus bus) {
            super(bus);
            eventProduceCount = new CountDownLatch(1);
            eventSubscribeCount = new CountDownLatch(1);
            setDaemon(true);
            setName("Bus: Subscriber A");
            init();
            start();
        }

        @Produce
        public EventA produceEventA() {
            eventProduceCount.countDown();
            return new EventA();
        }

        @Subscribe
        public void subscribeEventA(EventA event) {
            eventSubscribeCount.countDown();
        }
    }

    @Test
    public void normalProductionA() throws Exception {
        NormalProduction_ProducerA producerA;
        NormalProduction_SubscriberA subscriberA;
        NormalProduction_ProducerAndSubscriberInOneA producerAndSubscriberInOneA;
        //
        producerA = new NormalProduction_ProducerA(sBus, 1);
        subscriberA = new NormalProduction_SubscriberA(sBus, 1);
        assertTrue(producerA.eventCount.await(10, TimeUnit.SECONDS));
        assertTrue(subscriberA.eventCount.await(10, TimeUnit.SECONDS));
        assertTrue(subscriberA.terminateAndWait(10000));
        assertTrue(producerA.terminateAndWait(10000));
        //
        subscriberA = new NormalProduction_SubscriberA(sBus, 1);
        producerA = new NormalProduction_ProducerA(sBus, 1);
        assertTrue(producerA.eventCount.await(10, TimeUnit.SECONDS));
        assertTrue(subscriberA.eventCount.await(10, TimeUnit.SECONDS));
        assertTrue(producerA.terminateAndWait(10000));
        assertTrue(subscriberA.terminateAndWait(10000));
        //
        producerAndSubscriberInOneA = new NormalProduction_ProducerAndSubscriberInOneA(sBus);
        assertTrue(producerAndSubscriberInOneA.eventProduceCount.await(10, TimeUnit.SECONDS));
        assertTrue(producerAndSubscriberInOneA.eventSubscribeCount.await(10, TimeUnit.SECONDS));
        assertTrue(producerAndSubscriberInOneA.terminateAndWait(10000));
    }

    @Test
    public void normalProductionA_stress() throws Exception {
        final int COUNT = 1000;
        NormalProduction_ProducerA producerA;
        NormalProduction_SubscriberA subscriberA;
        NormalProduction_ProducerAndSubscriberInOneA producerAndSubscriberInOneA;
        for (int i = 0; i < COUNT; i++) {
            producerA = new NormalProduction_ProducerA(sBus, 1);
            subscriberA = new NormalProduction_SubscriberA(sBus, 1);
            assertTrue(producerA.eventCount.await(10, TimeUnit.SECONDS));
            assertTrue(subscriberA.eventCount.await(10, TimeUnit.SECONDS));
            assertTrue(subscriberA.terminateAndWait(10000));
            assertTrue(producerA.terminateAndWait(10000));
            //
            subscriberA = new NormalProduction_SubscriberA(sBus, 1);
            producerA = new NormalProduction_ProducerA(sBus, 1);
            assertTrue(producerA.eventCount.await(10, TimeUnit.SECONDS));
            assertTrue(subscriberA.eventCount.await(10, TimeUnit.SECONDS));
            assertTrue(producerA.terminateAndWait(10000));
            assertTrue(subscriberA.terminateAndWait(10000));
            //
            producerAndSubscriberInOneA = new NormalProduction_ProducerAndSubscriberInOneA(sBus);
            assertTrue(producerAndSubscriberInOneA.eventProduceCount.await(10, TimeUnit.SECONDS));
            assertTrue(producerAndSubscriberInOneA.eventSubscribeCount.await(10, TimeUnit.SECONDS));
            assertTrue(producerAndSubscriberInOneA.terminateAndWait(10000));
        }
    }
}
