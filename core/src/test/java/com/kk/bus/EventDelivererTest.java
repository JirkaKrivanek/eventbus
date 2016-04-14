package com.kk.bus;


import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
        protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
            mEvent = event;
            mEventDeliverer = eventDeliverer;
        }

        @Override
        protected void requestCallProducerMethod(EventDeliverer eventDeliverer,
                                                 List<EventDeliverer> subscriberDeliverers) {
        }

        public void callNow() throws InvocationTargetException {
            callSubscriberMethods(mEvent, mEventDeliverer);
        }
    }

    @Test
    public void normalDelivery_shouldDeliverEvent() throws Exception {
        EventSubscriber eventSubscriber = new EventSubscriber();
        RegisteredClass registeredClass = new RegisteredClass(eventSubscriber.getClass());
        DeliveryContextTest deliveryContextTest = new DeliveryContextTest();
        EventDeliverer eventDeliverer = new EventDeliverer(null, eventSubscriber, deliveryContextTest);
        eventDeliverer.setSubscriberMethods(registeredClass.getSubscriberMethods(new Subscriber(Event.class, 0)));
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.requestCallSubscriberMethods(event);
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
        EventDeliverer eventDeliverer = new EventDeliverer(null, eventSubscriber, deliveryContextTest);
        eventDeliverer.setSubscriberMethods(registeredClass.getSubscriberMethods(new Subscriber(Event.class, 0)));
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.clearOut();
        eventDeliverer.requestCallSubscriberMethods(event);
        assertNull(mDeliveredEvent);
        deliveryContextTest.callNow();
        assertNull(mDeliveredEvent);
    }

    @Test
    public void clearedAfterDeliverButBeforeCall_shouldNotDeliverEvent() throws Exception {
        EventSubscriber eventSubscriber = new EventSubscriber();
        RegisteredClass registeredClass = new RegisteredClass(eventSubscriber.getClass());
        DeliveryContextTest deliveryContextTest = new DeliveryContextTest();
        EventDeliverer eventDeliverer = new EventDeliverer(null, eventSubscriber, deliveryContextTest);
        eventDeliverer.setSubscriberMethods(registeredClass.getSubscriberMethods(new Subscriber(Event.class, 0)));
        Event event = new Event();
        mDeliveredEvent = null;
        eventDeliverer.requestCallSubscriberMethods(event);
        assertNull(mDeliveredEvent);
        eventDeliverer.clearOut();
        deliveryContextTest.callNow();
        assertNull(mDeliveredEvent);
    }
}
