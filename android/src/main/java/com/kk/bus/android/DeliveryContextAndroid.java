package com.kk.bus.android;


import android.os.Handler;
import android.os.Looper;

import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;

import java.lang.reflect.InvocationTargetException;

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
    protected void deliverEvent(final Object event, final EventDeliverer eventDeliverer) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callMethods(event, eventDeliverer);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e); // Dirty but effective hack to hide the exception
                }
            }
        });
    }
}
