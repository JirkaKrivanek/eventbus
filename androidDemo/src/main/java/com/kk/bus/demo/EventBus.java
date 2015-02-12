package com.kk.bus.demo;

import com.kk.bus.Bus;
import com.kk.bus.DeliveryContextManagers;
import com.kk.bus.android.DeliveryContextManagerAndroid;

/**
 * The event bus registration and simplification.
 * *
 * @author Jiri Krivanek
 */
public class EventBus {

    private static Bus sBus;

    static {
        sBus = new Bus();
        DeliveryContextManagers.registerDeliveryContextManager(new DeliveryContextManagerAndroid());
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
