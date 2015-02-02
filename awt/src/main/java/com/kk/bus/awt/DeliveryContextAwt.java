package com.kk.bus.awt;


import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

/**
 * Delivery context for the java AWT dispatch thread.
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextAwt extends DeliveryContext {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void deliverEvent(final Object event, final EventDeliverer eventDeliverer) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    callMethods(event, eventDeliverer);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e); // Hack to hide the exception (dirty but sufficient here)
                }
            }
        });
    }
}
