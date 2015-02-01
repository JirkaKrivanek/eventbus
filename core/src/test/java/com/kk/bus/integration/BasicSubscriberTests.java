package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.EventDeliverer;
import com.kk.bus.Subscribe;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BasicSubscriberTests {

    private Bus mBus;


    private static class TestDeliveryContext extends DeliveryContext {

        @Override
        protected void deliverEvent(Object event, EventDeliverer eventDeliverer) {
            try {
                callMethods(event, eventDeliverer);
            } catch (InvocationTargetException e) {
                assertTrue(e.getMessage(), false);
            }
        }
    }


    private static class TestDeliveryContextManager implements DeliveryContextManager<TestDeliveryContext> {

        private final TestDeliveryContext mDeliveryContext = new TestDeliveryContext();

        @Override
        public TestDeliveryContext getCurrentDeliveryContext() {
            return mDeliveryContext;
        }

        @Override
        public boolean isCurrentDeliveryContext(TestDeliveryContext deliveryContext) {
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
