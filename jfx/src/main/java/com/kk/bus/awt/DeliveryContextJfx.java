package com.kk.bus.awt;


import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;
import javafx.application.Platform;

import java.util.List;

/**
 * Delivery context for the java AWT dispatch thread.
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextJfx extends DeliveryContext {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void requestCallSubscriberMethods(final Object event, final EventDeliverer eventDeliverer) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                callSubscriberMethods(event, eventDeliverer);
            }
        });
    }

    @Override
    protected void requestCallProducerMethod(final EventDeliverer producerDeliverer,
                                             final List<EventDeliverer> subscriberDeliverers) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                callProducerMethod(producerDeliverer, subscriberDeliverers);
            }
        });
    }
}
