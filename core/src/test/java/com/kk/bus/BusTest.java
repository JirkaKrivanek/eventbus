package com.kk.bus;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BusTest {

    private DeliveryContext        mDeliveryContext;
    private DeliveryContextManager mDeliveryContextManager;

    @Before
    public void setUp() {
        mDeliveryContext = mock(DeliveryContext.class);
        mDeliveryContextManager = mock(DeliveryContextManager.class);
        when(mDeliveryContextManager.getCurrentDeliveryContext()).thenReturn(mDeliveryContext);
        DeliveryContextManagers.registerDeliveryContextManager(mDeliveryContextManager);
    }

    @After
    public void cleanUp() {
        DeliveryContextManagers.unregisterDeliveryContextManager(mDeliveryContextManager);
    }

    @Test
    public void register_shouldNotThrowAnyException() {
        Bus bus = new Bus();
        assertNotNull(bus);
        bus.register(new SomeObject());
    }

    @Test
    public void unregister_shouldNotThrowAnyException() {
        Bus bus = new Bus();
        assertNotNull(bus);
        bus.unregister(new SomeObject());
    }

    @Test
    public void post_shouldNotThrowAnyException() {
        Bus bus = new Bus();
        assertNotNull(bus);
        bus.post(new SomeObject());
    }

    private static class SomeObject {}
}
