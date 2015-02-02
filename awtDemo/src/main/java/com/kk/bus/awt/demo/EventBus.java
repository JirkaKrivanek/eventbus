package com.kk.bus.awt.demo;


import com.kk.bus.Bus;
import com.kk.bus.awt.DeliveryContextManagerAwt;

/**
 * The event bus simplification.
 *
 * @author Jiri Krivanek
 */
public class EventBus {

    private static final Bus sBus;

    static {
        DeliveryContextManagerAwt.register();
        sBus = new Bus();
    }

    public static void register(Object object) {
        sBus.register(object);
    }

    public static void unregister(Object object) {
        sBus.unregister(object);
    }

    public static void post(Object event) {
        sBus.post(event);
    }
}
