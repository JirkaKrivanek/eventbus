package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.EventDeliverer;
import com.kk.bus.Subscribe;
import com.kk.bus.Token;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TokenSubscriberTest {

    private static class TestDeliveryContext extends DeliveryContext {

        @Override
        protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
            callSubscriberMethods(event, eventDeliverer);
        }

        @Override
        protected void requestCallProducerMethod(EventDeliverer eventDeliverer,
                                                 List<EventDeliverer> subscriberDeliverers) {
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

    private static final int TOKEN_BC = Bus.BROADCAST_TOKEN;
    private static final int TOKEN_1  = 1;
    private static final int TOKEN_2  = 2;
    private static final int TOKEN_3  = 3;


    private static class EventWithoutToken {}


    private static class EventWithBroadcastToken {

        @Token
        public int getToken() {
            return TOKEN_BC;
        }
    }


    private static class EventWithToken1 {

        @Token
        public int getToken() {
            return TOKEN_1;
        }
    }


    private static class EventWithToken2 {

        @Token
        public int getToken() {
            return TOKEN_2;
        }
    }


    private static class EventWithToken3 {

        @Token
        public int getToken() {
            return TOKEN_3;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class SubscriberAll {

        int eventCountNoToken;
        int eventCountBcToken;
        int eventCount1;
        int eventCount2;
        int eventCount3;

        @Subscribe
        public void onEventNoToken(EventWithoutToken event) {
            eventCountNoToken++;
        }

        @Subscribe
        public void onEventBcToken(EventWithBroadcastToken event) {
            eventCountBcToken++;
        }

        @Subscribe
        public void onEvent1(EventWithToken1 event) {
            eventCount1++;
        }

        @Subscribe(token = TOKEN_BC)
        public void onEvent2(EventWithToken2 event) {
            eventCount2++;
        }

        @Subscribe
        public void onEvent3(EventWithToken3 event) {
            eventCount3++;
        }
    }


    @Test
    public void broadcastSubscribers() {
        EventWithoutToken eventWithoutToken = new EventWithoutToken();
        EventWithBroadcastToken eventWithBroadcastToken = new EventWithBroadcastToken();
        EventWithToken1 eventWithToken1 = new EventWithToken1();
        EventWithToken2 eventWithToken2 = new EventWithToken2();
        EventWithToken3 eventWithToken3 = new EventWithToken3();
        SubscriberAll subscriberAll = new SubscriberAll();
        mBus.register(subscriberAll);
        mBus.post(eventWithoutToken);
        mBus.post(eventWithoutToken, TOKEN_BC);
        mBus.post(eventWithoutToken, TOKEN_1);
        mBus.post(eventWithBroadcastToken);
        mBus.post(eventWithBroadcastToken, TOKEN_BC);
        mBus.post(eventWithBroadcastToken, TOKEN_1);
        mBus.post(eventWithToken1);
        mBus.post(eventWithToken1, TOKEN_BC);
        mBus.post(eventWithToken1, TOKEN_1);
        mBus.post(eventWithToken2);
        mBus.post(eventWithToken2, TOKEN_BC);
        mBus.post(eventWithToken2, TOKEN_1);
        mBus.post(eventWithToken3);
        mBus.post(eventWithToken3, TOKEN_BC);
        mBus.post(eventWithToken3, TOKEN_1);
        assertEquals(3, subscriberAll.eventCountNoToken);
        assertEquals(3, subscriberAll.eventCountBcToken);
        assertEquals(3, subscriberAll.eventCount1);
        assertEquals(3, subscriberAll.eventCount2);
        assertEquals(3, subscriberAll.eventCount3);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class SubscriberSelected {

        int eventCount1;
        int eventCount2;
        int eventCount3;

        @Subscribe(token = TOKEN_1)
        public void onEvent1(EventWithToken1 event) {
            eventCount1++;
        }

        @Subscribe(token = TOKEN_2)
        public void onEvent2(EventWithToken2 event) {
            eventCount2++;
        }

        @Subscribe(token = TOKEN_3)
        public void onEvent3(EventWithToken3 event) {
            eventCount3++;
        }
    }


    @Test
    public void selectedSubscribers() {
        EventWithoutToken eventWithoutToken = new EventWithoutToken();
        EventWithBroadcastToken eventWithBroadcastToken = new EventWithBroadcastToken();
        EventWithToken1 eventWithToken1 = new EventWithToken1();
        EventWithToken2 eventWithToken2 = new EventWithToken2();
        EventWithToken3 eventWithToken3 = new EventWithToken3();
        SubscriberSelected subscriberSelected = new SubscriberSelected();
        mBus.register(subscriberSelected);
        mBus.post(eventWithoutToken);
        mBus.post(eventWithoutToken, TOKEN_BC);
        mBus.post(eventWithoutToken, TOKEN_1);
        mBus.post(eventWithoutToken, TOKEN_2);
        mBus.post(eventWithoutToken, TOKEN_3);
        mBus.post(eventWithBroadcastToken);
        mBus.post(eventWithBroadcastToken, TOKEN_BC);
        mBus.post(eventWithBroadcastToken, TOKEN_1);
        mBus.post(eventWithBroadcastToken, TOKEN_2);
        mBus.post(eventWithBroadcastToken, TOKEN_3);
        mBus.post(eventWithToken1);
        mBus.post(eventWithToken1, TOKEN_BC);
        mBus.post(eventWithToken1, TOKEN_1);
        mBus.post(eventWithToken1, TOKEN_2);
        mBus.post(eventWithToken1, TOKEN_3);
        mBus.post(eventWithToken2);
        mBus.post(eventWithToken2, TOKEN_BC);
        mBus.post(eventWithToken2, TOKEN_1);
        mBus.post(eventWithToken2, TOKEN_2);
        mBus.post(eventWithToken2, TOKEN_3);
        mBus.post(eventWithToken3);
        mBus.post(eventWithToken3, TOKEN_BC);
        mBus.post(eventWithToken3, TOKEN_1);
        mBus.post(eventWithToken3, TOKEN_2);
        mBus.post(eventWithToken3, TOKEN_3);
        assertEquals(3, subscriberSelected.eventCount1);
        assertEquals(3, subscriberSelected.eventCount2);
        assertEquals(3, subscriberSelected.eventCount3);
    }
}
