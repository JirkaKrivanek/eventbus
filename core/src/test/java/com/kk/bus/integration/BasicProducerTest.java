package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.EventDeliverer;
import com.kk.bus.Produce;
import com.kk.bus.Subscribe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BasicProducerTest {


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


    private Bus                        mBus;
    private TestDeliveryContextManager mTestDeliveryContextManager;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Before
    public void setUp() {
        mTestDeliveryContextManager = new TestDeliveryContextManager();
        DeliveryContextManagers.registerDeliveryContextManager(mTestDeliveryContextManager);
        mBus = new Bus();
    }

    @After
    public void cleanUp() {
        DeliveryContextManagers.unregisterDeliveryContextManager(mTestDeliveryContextManager);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Basic tests
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class NormalDeliveryEventA {}


    private static class NormalDeliveryEventAA extends NormalDeliveryEventA {}


    private static class NormalDeliveryEventAAA extends NormalDeliveryEventAA {}


    private static class NormalDeliveryEventB {}


    private static class NormalDeliveryEventC {}


    public static class NormalDeliveryProducerA {

        @Produce
        public NormalDeliveryEventA produceEventA() {
            return new NormalDeliveryEventA();
        }
    }


    public static class NormalDeliveryProducerAA extends NormalDeliveryProducerA {}


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


    public static class NormalDeliveryProducerAndSubscriberInOne {

        int producedEventCountA;
        int deliveredEventCountA;
        int deliveredEventCountAA;
        int deliveredEventCountAAA;

        int producedEventCountB;
        int deliveredEventCountB;

        @Produce
        public NormalDeliveryEventAAA produceEventAAA() {
            producedEventCountA++;
            return new NormalDeliveryEventAAA();
        }

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            deliveredEventCountA++;
        }

        @Subscribe
        public void onEventAA(NormalDeliveryEventAA event) {
            deliveredEventCountAA++;
        }

        @Subscribe
        public void onEventAAA(NormalDeliveryEventAAA event) {
            deliveredEventCountAAA++;
        }

        @Produce
        public NormalDeliveryEventB produceEventB() {
            producedEventCountB++;
            return new NormalDeliveryEventB();
        }

        @Subscribe
        public void onEventB(NormalDeliveryEventB event) {
            deliveredEventCountB++;
        }
    }


    public static class PrivateProtectedPackageProtectedProducersNotCalled {

        int producedEventCountA;
        int producedEventCountB;
        int producedEventCountC;

        int subscribedEventCountA;
        int subscribedEventCountB;
        int subscribedEventCountC;

        @Produce
        private NormalDeliveryEventA produceEventA() {
            producedEventCountA++;
            return new NormalDeliveryEventA();
        }

        @Subscribe
        public void onEventA(NormalDeliveryEventA event) {
            subscribedEventCountA++;
        }

        @Produce
        protected NormalDeliveryEventB produceEventB() {
            producedEventCountB++;
            return new NormalDeliveryEventB();
        }

        @Subscribe
        public void onEventB(NormalDeliveryEventB event) {
            subscribedEventCountB++;
        }

        @Produce
        NormalDeliveryEventC produceEventC() {
            producedEventCountC++;
            return new NormalDeliveryEventC();
        }

        @Subscribe
        public void onEventC(NormalDeliveryEventC event) {
            subscribedEventCountC++;
        }
    }

    @Test
    public void privateProtectedPackageProtectedProducersNotCalled() {
        PrivateProtectedPackageProtectedProducersNotCalled producer = new PrivateProtectedPackageProtectedProducersNotCalled();
        mBus.register(producer);
        assertEquals(0, producer.producedEventCountA);
        assertEquals(0, producer.producedEventCountB);
        assertEquals(0, producer.producedEventCountC);
        assertEquals(0, producer.subscribedEventCountA);
        assertEquals(0, producer.subscribedEventCountB);
        assertEquals(0, producer.subscribedEventCountC);
    }

    @Test
    public void producerInSuperClass() {
        NormalDeliveryProducerAA producerAA = new NormalDeliveryProducerAA();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        mBus.register(producerAA);
        mBus.register(subscriberA);
        assertEquals(subscriberA.eventCount, 1);
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
    public void normalProduction_producerRegisteredFirst_stress() {
        final int COUNT = 100000;
        NormalDeliveryProducerA producerA = new NormalDeliveryProducerA();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();
        NormalDeliverySubscriberC subscriberC = new NormalDeliverySubscriberC();
        NormalDeliverySubscriberD subscriberD = new NormalDeliverySubscriberD();
        mBus.register(producerA);
        mBus.register(subscriberA);
        for (int i = 0; i < COUNT; i++) {
            mBus.register(subscriberB);
            mBus.register(subscriberC);
            mBus.register(subscriberD);
            mBus.unregister(subscriberD);
            mBus.unregister(subscriberC);
            mBus.unregister(subscriberB);
        }
        mBus.unregister(subscriberA);
        mBus.unregister(producerA);
        assertEquals(1, subscriberA.eventCount);
        assertEquals(COUNT, subscriberB.eventCount);
        assertEquals(COUNT, subscriberC.eventCount);
        assertEquals(COUNT, subscriberC.eventCount);
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

    @Test
    public void normalProduction_producerAndSubscriberInOne() {
        NormalDeliveryProducerAndSubscriberInOne ps = new NormalDeliveryProducerAndSubscriberInOne();
        mBus.register(ps);
        assertEquals(1, ps.producedEventCountA);
        assertEquals(1, ps.deliveredEventCountA);
        assertEquals(1, ps.deliveredEventCountAA);
        assertEquals(1, ps.deliveredEventCountAAA);
        assertEquals(1, ps.producedEventCountB);
        assertEquals(1, ps.deliveredEventCountB);
    }

    @Test
    public void normalProduction_subscriberRegisteredFirst_stress() {
        final int COUNT = 100000;
        NormalDeliveryProducerA producerA = new NormalDeliveryProducerA();
        NormalDeliverySubscriberA subscriberA = new NormalDeliverySubscriberA();
        NormalDeliverySubscriberB subscriberB = new NormalDeliverySubscriberB();
        NormalDeliverySubscriberC subscriberC = new NormalDeliverySubscriberC();
        NormalDeliverySubscriberD subscriberD = new NormalDeliverySubscriberD();
        mBus.register(subscriberA);
        mBus.register(subscriberB);
        mBus.register(subscriberC);
        mBus.register(subscriberD);
        for (int i = 0; i < COUNT; i++) {
            mBus.register(producerA);
            mBus.unregister(producerA);
        }
        mBus.unregister(subscriberA);
        mBus.unregister(subscriberB);
        mBus.unregister(subscriberC);
        mBus.unregister(subscriberD);
        assertEquals(COUNT, subscriberA.eventCount);
        assertEquals(COUNT, subscriberB.eventCount);
        assertEquals(COUNT, subscriberC.eventCount);
        assertEquals(COUNT, subscriberD.eventCount);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Event inheritance test
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class InheritanceEventOther {}


    private static class InheritanceEventA {}


    private static class InheritanceEventAA extends InheritanceEventA {}


    private static class InheritanceEventAAA extends InheritanceEventAA {}


    public static class InheritanceProducerA {

        @Produce
        public InheritanceEventA produceEventA() {
            return new InheritanceEventA();
        }
    }


    public static class InheritanceProducerOther {

        @Produce
        public InheritanceEventOther produceEventOther() {
            return new InheritanceEventOther();
        }
    }


    public static class InheritanceProducerAA {

        @Produce
        public InheritanceEventAA produceEventAA() {
            return new InheritanceEventAA();
        }
    }


    public static class InheritanceProducerAAA {

        @Produce
        public InheritanceEventAAA produceEventAAA() {
            return new InheritanceEventAAA();
        }
    }


    public static class InheritanceSubscriber {

        int eventCountA1;
        int eventCountA2;
        int eventCountAA1;
        int eventCountAA2;
        int eventCountAAA1;
        int eventCountAAA2;
        int eventCountAAA3;

        public InheritanceSubscriber() {
            eventCountA1 = eventCountA2 = eventCountAA1 = eventCountAA2 = eventCountAAA1 = eventCountAAA2 = eventCountAAA3 = 0;
        }

        @Subscribe
        public void onEventA1(InheritanceEventA event) {
            eventCountA1++;
        }

        @Subscribe
        public void onEventA2(InheritanceEventA event) {
            eventCountA2++;
        }

        @Subscribe
        public void onEventAA1(InheritanceEventAA event) {
            eventCountAA1++;
        }

        @Subscribe
        public void onEventAA2(InheritanceEventAA event) {
            eventCountAA2++;
        }

        @Subscribe
        public void onEventAAA1(InheritanceEventAAA event) {
            eventCountAAA1++;
        }

        @Subscribe
        public void onEventAAA2(InheritanceEventAAA event) {
            eventCountAAA2++;
        }

        @Subscribe
        public void onEventAAA3(InheritanceEventAAA event) {
            eventCountAAA3++;
        }
    }

    @Test
    public void inheritance() {
        InheritanceProducerOther inheritanceProducerOther = new InheritanceProducerOther();
        InheritanceProducerA inheritanceProducerA1 = new InheritanceProducerA();
        InheritanceProducerA inheritanceProducerA2 = new InheritanceProducerA();
        InheritanceProducerA inheritanceProducerA3 = new InheritanceProducerA();
        InheritanceProducerAA inheritanceProducerAA = new InheritanceProducerAA();
        InheritanceProducerAAA inheritanceProducerAAA = new InheritanceProducerAAA();
        InheritanceSubscriber inheritanceSubscriber1 = new InheritanceSubscriber();
        InheritanceSubscriber inheritanceSubscriber2 = new InheritanceSubscriber();
        //
        mBus.register(inheritanceSubscriber1);
        mBus.register(inheritanceProducerOther);
        mBus.register(inheritanceProducerA1);
        mBus.register(inheritanceProducerA2);
        mBus.register(inheritanceProducerA3);
        mBus.register(inheritanceProducerAA);
        mBus.register(inheritanceProducerAAA);
        mBus.register(inheritanceSubscriber2);
        //
        mBus.unregister(inheritanceSubscriber2);
        mBus.unregister(inheritanceProducerAAA);
        mBus.unregister(inheritanceProducerAA);
        mBus.unregister(inheritanceProducerA3);
        mBus.unregister(inheritanceProducerA2);
        mBus.unregister(inheritanceProducerA1);
        mBus.unregister(inheritanceProducerOther);
        mBus.unregister(inheritanceSubscriber1);
        //
        assertEquals(5, inheritanceSubscriber1.eventCountA1);
        assertEquals(5, inheritanceSubscriber1.eventCountA2);
        assertEquals(2, inheritanceSubscriber1.eventCountAA1);
        assertEquals(2, inheritanceSubscriber1.eventCountAA2);
        assertEquals(1, inheritanceSubscriber1.eventCountAAA1);
        assertEquals(1, inheritanceSubscriber1.eventCountAAA2);
        assertEquals(1, inheritanceSubscriber1.eventCountAAA3);
        assertEquals(5, inheritanceSubscriber2.eventCountA1);
        assertEquals(5, inheritanceSubscriber2.eventCountA2);
        assertEquals(2, inheritanceSubscriber2.eventCountAA1);
        assertEquals(2, inheritanceSubscriber2.eventCountAA2);
        assertEquals(1, inheritanceSubscriber2.eventCountAAA1);
        assertEquals(1, inheritanceSubscriber2.eventCountAAA2);
        assertEquals(1, inheritanceSubscriber2.eventCountAAA3);
    }
}
