package com.kk.bus.timers;


import com.kk.bus.Bus;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class BusTimersTest {

    @Test
    public void constructor() throws Exception {
        BusTimers busTimers = new BusTimers(mock(Bus.class));
        assertNotNull(busTimers);
    }
}
