package com.kk.bus;


import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class EventDelivererTest {

    private static class Event {}


    private Event mDeliveredEvent;


    private class EventSubscriber {

        @Subscribe
        public void onEvent(Event event) {
            mDeliveredEvent = event;
        }
    }


    private static class DeliveryContextTest extends DeliveryContext {

        private Object         mEvent;
        private EventDeliverer mEventDeliverer;

        @Override
        protected void deliverEvent(Object event, EventDeliverer eventDeliverer) {
            mEvent = event;
            mEventDeliverer = eventDeliverer;
        }

        public void callNow() throws InvocationTargetException {
            callMethods(mEvent, mEventDeliverer);
        }
    }

    @Test
    public void normalDelivery_shouldDeliverEvent() throws Exception {
        EventSubscriber eventSubscriber = new EventSubscriber();
        RegisteredClass registeredClass = new RegisteredClass(eventSubscriber.getClass());
        DeliveryContextTest deliveryContextTest = new DeliveryContextTest();
        EventDeliverer eventDeliverer = new EventDeliverer(eventSubscriber,
                                                           registeredClass.getSubscribedMethods(Event.class),
                                                           deliveryContextTest);
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.deliverEvent(event);
        assertNull(mDeliveredEvent);
        deliveryContextTest.callNow();
        assertNotNull(mDeliveredEvent);
        assertSame(event, mDeliveredEvent);
    }

    @Test
    public void clearedBeforeDeliver_shouldNotDeliverEvent() throws Exception {
        EventSubscriber eventSubscriber = new EventSubscriber();
        RegisteredClass registeredClass = new RegisteredClass(eventSubscriber.getClass());
        DeliveryContextTest deliveryContextTest = new DeliveryContextTest();
        EventDeliverer eventDeliverer = new EventDeliverer(eventSubscriber,
                                                           registeredClass.getSubscribedMethods(Event.class),
                                                           deliveryContextTest);
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.clearOut();
        eventDeliverer.deliverEvent(event);
        assertNull(mDeliveredEvent);
        deliveryContextTest.callNow();
        assertNull(mDeliveredEvent);
    }

    @Test
    public void clearedAfterDeliverButBeforeCall_shouldNotDeliverEvent() throws Exception {
        EventSubscriber eventSubscriber = new EventSubscriber();
        RegisteredClass registeredClass = new RegisteredClass(eventSubscriber.getClass());
        DeliveryContextTest deliveryContextTest = new DeliveryContextTest();
        EventDeliverer eventDeliverer = new EventDeliverer(eventSubscriber,
                                                           registeredClass.getSubscribedMethods(Event.class),
                                                           deliveryContextTest);
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.deliverEvent(event);
        assertNull(mDeliveredEvent);
        eventDeliverer.clearOut();
        deliveryContextTest.callNow();
        assertNull(mDeliveredEvent);
    }
}
