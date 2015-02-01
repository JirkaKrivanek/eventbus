package com.kk.bus;


import org.junit.Test;

public class BusTest {

    @Test
    public void register_shouldNotThrowAnyException() throws Exception {
        Bus bus = new Bus();
        bus.register(new SomeObject());
    }

    @Test
    public void unregister_shouldNotThrowAnyException() throws Exception {
        Bus bus = new Bus();
        bus.unregister(new SomeObject());
    }

    @Test
    public void post_shouldNotThrowAnyException() throws Exception {
        Bus bus = new Bus();
        bus.post(new SomeObject());
    }

    private static class SomeObject {}
}
