package com.kk.bus.awt;


import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;

import java.awt.EventQueue;

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
    protected void requestCallSubscriberMethods(final Object event, final EventDeliverer eventDeliverer) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                callSubscriberMethods(event, eventDeliverer);
            }
        });
    }

    @Override
    protected void requestCallProducerMethod(final EventDeliverer eventDeliverer) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                callProducerMethod(eventDeliverer);
            }
        });
    }
}
