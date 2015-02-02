package com.kk.bus.android;


import android.os.Looper;

import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;

/**
 * Manager of the delivery context for the Android main UI thread.
 * <p/>
 * <dl><dt><b>Attention:</b></dt><dd>Do not forget to register this manager via the {@link
 * DeliveryContextManagerAndroid#register()} method.</dd></dl>
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextManagerAndroid implements DeliveryContextManager<DeliveryContextAndroid> {

    /**
     * The only delivery context of the Android main UI thread.
     */
    private final DeliveryContextAndroid mDeliveryContextAndroid = new DeliveryContextAndroid();

    /**
     * Registers this manager among other managers.
     */
    public static void register() {
        DeliveryContextManagers.registerDeliveryContextManager(new DeliveryContextManagerAndroid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeliveryContextAndroid getCurrentDeliveryContext() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return mDeliveryContextAndroid;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentDeliveryContext(DeliveryContext deliveryContext) {
        return deliveryContext == mDeliveryContextAndroid;
    }
}
