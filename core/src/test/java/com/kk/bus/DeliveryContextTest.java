package com.kk.bus;


import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DeliveryContextTest {

    private static class TestDeliveryContext extends DeliveryContext {

        @Override
        protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
        }

        @Override
        protected void requestCallProducerMethod(EventDeliverer eventDeliverer) {
        }
    }


    private static class Event {}

    @Test
    public void callMethods_allParametersValid_works() throws Exception {
        TestDeliveryContext deliveryContext = new TestDeliveryContext();
        Event event = new Event();
        EventDeliverer eventDeliverer = mock(EventDeliverer.class);
        deliveryContext.callSubscriberMethods(event, eventDeliverer);
        verify(eventDeliverer, times(1)).callSubscriberMethods(event);
    }

    @Test
    public void callMethods_eventNull_doesNotWork() throws Exception {
        TestDeliveryContext deliveryContext = new TestDeliveryContext();
        EventDeliverer eventDeliverer = mock(EventDeliverer.class);
        deliveryContext.callSubscriberMethods(null, eventDeliverer);
        verify(eventDeliverer, never()).callSubscriberMethods(null);
    }
}
