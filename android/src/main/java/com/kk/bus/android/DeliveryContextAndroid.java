package com.kk.bus.android;


import android.os.Handler;
import android.os.Looper;

import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;

import java.util.List;

/**
 * Delivery context for the Android main UI thread.
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextAndroid extends DeliveryContext {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * {@inheritDoc}
     */
    @Override
    protected void requestCallSubscriberMethods(final Object event, final EventDeliverer eventDeliverer) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                callSubscriberMethods(event, eventDeliverer);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void requestCallProducerMethod(final EventDeliverer eventDeliverer,
                                             final List<EventDeliverer> subscriberDeliverers) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                callProducerMethod(eventDeliverer, subscriberDeliverers);
            }
        });
    }
}
