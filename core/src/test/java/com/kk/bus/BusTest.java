package com.kk.bus;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BusTest {

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
