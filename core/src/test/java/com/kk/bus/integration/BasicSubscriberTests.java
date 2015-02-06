package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.EventDeliverer;
import com.kk.bus.Subscribe;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicSubscriberTests {

    private Bus mBus;


    private static class TestDeliveryContext extends DeliveryContext {

        @Override
        protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
            callSubscriberMethods(event, eventDeliverer);
        }

        @Override
        protected void requestCallProducerMethod(EventDeliverer eventDeliverer) {
        }
    }


    private static class TestDeliveryContextManager implements DeliveryContextManager<TestDeliveryContext> {

        private final TestDeliveryContext mDeliveryContext = new TestDeliveryContext();

        @Override
        public TestDeliveryContext getCurrentDeliveryContext() {
            return mDeliveryContext;
        }

        @Override
        public boolean isCurrentDeliveryContext(DeliveryContext deliveryContext) {
            return deliveryContext == mDeliveryContext;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Before
    public void setUpTest() {
        DeliveryContextManagers.clearDeliveryContextManagers();
        DeliveryContextManagers.registerDeliveryContextManager(new TestDeliveryContextManager());
        mBus = new Bus();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class NormalDeliveryEventA {}


    private static class NormalDeliveryEventB {}


    private static class NormalDeliveryEventC {}


    public static class NormalDeliverySubscriberA {

        int eventCount;

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            eventCount++;
        }
    }


    public static class NormalDeliverySubscriberB {

        int eventCount;

        @Subscribe
        public void onEventB(NormalDeliveryEventB event) {
            eventCount++;
        }
    }


    public static class NormalDeliverySubscriberC {

        int eventCount;

        @Subscribe
        public void onEventC(NormalDeliveryEventC event) {
            eventCount++;
        }
    }

    @Test
    public void normalDelivery() {
        NormalDeliveryEventA eventA = new NormalDeliveryEventA();
        NormalDeliveryEventB eventB = new NormalDeliveryEventB();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();

        // Not registered yet, must not be delivered
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(0, subscriberA.eventCount);
        assertEquals(0, subscriberB.eventCount);

        // A registered, B not registered yet, must be delivered only through A, not B
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.register(subscriberA);
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(2, subscriberA.eventCount);
        assertEquals(0, subscriberB.eventCount);

        // A registered, B registered, must be delivered through both A and B
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.register(subscriberB);
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(2, subscriberA.eventCount);
        assertEquals(2, subscriberB.eventCount);

        // A registered twice, B registered twice, must be delivered only once per post through both A and B
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.register(subscriberA);
        mBus.register(subscriberB);
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(2, subscriberA.eventCount);
        assertEquals(2, subscriberB.eventCount);

        // A unregistered, B registered, must be delivered only through B, not A
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.unregister(subscriberA);
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(0, subscriberA.eventCount);
        assertEquals(2, subscriberB.eventCount);

        // A unregistered, B unregistered, must not be delivered
        subscriberA.eventCount = subscriberB.eventCount = 0;
        mBus.unregister(subscriberB);
        mBus.post(eventA);
        mBus.post(eventB);
        mBus.post(eventA);
        mBus.post(eventB);
        assertEquals(0, subscriberA.eventCount);
        assertEquals(0, subscriberB.eventCount);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class NormalDeliveryEventAA extends NormalDeliveryEventA {}


    private static class NormalDeliveryEventAAA extends NormalDeliveryEventAA {}


    private static class NormalDeliveryEventAB extends NormalDeliveryEventA {}


    private static class NormalDeliveryEventAC extends NormalDeliveryEventA {}


    private static class NormalDeliveryEventBA extends NormalDeliveryEventB {}


    private static class NormalDeliveryEventBB extends NormalDeliveryEventB {}


    private static class NormalDeliveryEventBBB extends NormalDeliveryEventBB {}


    private static class NormalDeliveryEventBBBB extends NormalDeliveryEventBBB {}


    private static class NormalDeliveryEventBC extends NormalDeliveryEventB {}


    public static class NormalDeliverySubscriberInheritance {

        int eventCountA;
        int eventCountAA;
        int eventCountAAA;
        int eventCountAB;
        int eventCountAC;
        int eventCountB;
        int eventCountBA;
        int eventCountBB;
        int eventCountBBB;
        int eventCountBBBB;
        int eventCountBC;
        int eventCountC;

        void clearAllCounters() {
            eventCountA = eventCountAA = eventCountAAA = eventCountAB = eventCountAC = eventCountB = eventCountBA = eventCountBB = eventCountBBB = eventCountBBBB = eventCountBC = eventCountC = 0;
        }

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            eventCountA++;
        }

        @Subscribe
        public void onEventAA(NormalDeliveryEventAA event) {
            eventCountAA++;
        }

        @Subscribe
        public void onEventAAA(NormalDeliveryEventAAA event) {
            eventCountAAA++;
        }

        @Subscribe
        public void onEventAB(NormalDeliveryEventAB event) {
            eventCountAB++;
        }

        @Subscribe
        public void onEventAC(NormalDeliveryEventAC event) {
            eventCountAC++;
        }

        @Subscribe
        public void onEventB(NormalDeliveryEventB event) {
            eventCountB++;
        }

        @Subscribe
        public void onEventBA(NormalDeliveryEventBA event) {
            eventCountBA++;
        }

        @Subscribe
        public void onEventBB(NormalDeliveryEventBB event) {
            eventCountBB++;
        }

        @Subscribe
        public void onEventBBB(NormalDeliveryEventBBB event) {
            eventCountBBB++;
        }

        @Subscribe
        public void onEventBBBB(NormalDeliveryEventBBBB event) {
            eventCountBBBB++;
        }

        @Subscribe
        public void onEventBC(NormalDeliveryEventBC event) {
            eventCountBC++;
        }

        @Subscribe
        public void onEventC(NormalDeliveryEventC event) {
            eventCountC++;
        }
    }

    @Test
    public void inheritedEvents() {
        // Prepare
        NormalDeliverySubscriberInheritance subscriber = new NormalDeliverySubscriberInheritance();
        mBus.register(subscriber);
        // Clear counters
        subscriber.clearAllCounters();
        // Deliver each (well intentionally do not post some events) event twice
        mBus.post(new NormalDeliveryEventA());
        mBus.post(new NormalDeliveryEventAA());
        mBus.post(new NormalDeliveryEventAAA());
        mBus.post(new NormalDeliveryEventAB());
        mBus.post(new NormalDeliveryEventAC());
        mBus.post(new NormalDeliveryEventB());
        mBus.post(new NormalDeliveryEventBA());
        mBus.post(new NormalDeliveryEventBB());
        mBus.post(new NormalDeliveryEventBBBB());
        mBus.post(new NormalDeliveryEventC());
        //
        mBus.post(new NormalDeliveryEventA());
        mBus.post(new NormalDeliveryEventAA());
        mBus.post(new NormalDeliveryEventAAA());
        mBus.post(new NormalDeliveryEventAB());
        mBus.post(new NormalDeliveryEventAC());
        mBus.post(new NormalDeliveryEventB());
        mBus.post(new NormalDeliveryEventBA());
        mBus.post(new NormalDeliveryEventBB());
        mBus.post(new NormalDeliveryEventBBBB());
        mBus.post(new NormalDeliveryEventC());
        // Check proper counts
        assertEquals(10, subscriber.eventCountA);
        assertEquals(4, subscriber.eventCountAA);
        assertEquals(2, subscriber.eventCountAAA);
        assertEquals(2, subscriber.eventCountAB);
        assertEquals(2, subscriber.eventCountAC);
        assertEquals(8, subscriber.eventCountB);
        assertEquals(2, subscriber.eventCountBA);
        assertEquals(4, subscriber.eventCountBB);
        assertEquals(2, subscriber.eventCountBBBB);
        assertEquals(2, subscriber.eventCountC);
        // Cleanup
        mBus.unregister(subscriber);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void stress() {
        NormalDeliveryEventA eventA = new NormalDeliveryEventA();
        NormalDeliveryEventB eventB = new NormalDeliveryEventB();
        NormalDeliveryEventC eventC = new NormalDeliveryEventC();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();
        NormalDeliverySubscriberC subscriberC = new NormalDeliverySubscriberC();
        mBus.register(subscriberA);
        mBus.register(subscriberB);

        final int COUNT = 100000;
        boolean subscriberCisRegistered = false;
        subscriberA.eventCount = subscriberB.eventCount = subscriberC.eventCount = 0;
        for (int i = 0; i < COUNT; i++) {
            mBus.post(eventA);
            mBus.post(eventB);
            if (subscriberCisRegistered) {
                subscriberCisRegistered = false;
                mBus.unregister(subscriberC);
            } else {
                subscriberCisRegistered = true;
                mBus.register(subscriberC);
            }
            mBus.post(eventC);
        }
        assertEquals(COUNT, subscriberA.eventCount);
        assertEquals(COUNT, subscriberB.eventCount);
        assertEquals(COUNT / 2, subscriberC.eventCount);

        mBus.unregister(subscriberA);
        mBus.unregister(subscriberB);
        mBus.unregister(subscriberC);
    }
}
