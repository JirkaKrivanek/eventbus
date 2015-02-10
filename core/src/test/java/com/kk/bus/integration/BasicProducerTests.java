package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.EventDeliverer;
import com.kk.bus.Produce;
import com.kk.bus.Subscribe;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BasicProducerTests {

    private Bus mBus;


    private static class TestDeliveryContext extends DeliveryContext {

        @Override
        protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
            callSubscriberMethods(event, eventDeliverer);
        }

        @Override
        protected void requestCallProducerMethod(EventDeliverer eventDeliverer,
                                                 List<EventDeliverer> subscriberDeliverers) {
            callProducerMethod(eventDeliverer, subscriberDeliverers);
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


    public static class NormalDeliveryProducerA {

        @Produce
        public NormalDeliveryEventA produceEventA() {
            return new NormalDeliveryEventA();
        }
    }


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
        public void onEventA(NormalDeliveryEventA event) {
            eventCount++;
        }
    }


    public static class NormalDeliverySubscriberC {

        int eventCount;

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            eventCount++;
        }
    }


    public static class NormalDeliverySubscriberD {

        int eventCount;

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            eventCount++;
        }
    }

    @Test
    public void normalProduction_producerRegisteredFirst() {
        NormalDeliveryProducerA producerA = new NormalDeliveryProducerA();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();
        NormalDeliverySubscriberC subscriberC = new NormalDeliverySubscriberC();
        NormalDeliverySubscriberD subscriberD = new NormalDeliverySubscriberD();
        mBus.register(producerA);
        mBus.register(subscriberA);
        mBus.register(subscriberB);
        mBus.register(subscriberC);
        mBus.register(subscriberD);
        mBus.unregister(producerA);
        mBus.unregister(subscriberA);
        mBus.unregister(subscriberB);
        mBus.unregister(subscriberC);
        mBus.unregister(subscriberD);
        assertEquals(1, subscriberA.eventCount);
        assertEquals(1, subscriberB.eventCount);
        assertEquals(1, subscriberC.eventCount);
        assertEquals(1, subscriberC.eventCount);
    }

    @Test
    public void normalProduction_subscriberRegisteredFirst() {
        NormalDeliveryProducerA producerA = new NormalDeliveryProducerA();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();
        NormalDeliverySubscriberC subscriberC = new NormalDeliverySubscriberC();
        NormalDeliverySubscriberD subscriberD = new NormalDeliverySubscriberD();
        mBus.register(subscriberA);
        mBus.register(subscriberB);
        mBus.register(subscriberC);
        mBus.register(subscriberD);
        mBus.register(producerA);
        mBus.unregister(producerA);
        mBus.unregister(subscriberA);
        mBus.unregister(subscriberB);
        mBus.unregister(subscriberC);
        mBus.unregister(subscriberD);
        assertEquals(1, subscriberA.eventCount);
        assertEquals(1, subscriberB.eventCount);
        assertEquals(1, subscriberC.eventCount);
        assertEquals(1, subscriberD.eventCount);
    }
}
