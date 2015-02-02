package com.kk.bus.awt;


import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;

import java.awt.EventQueue;

/**
 * Manager of the delivery context for the AWT dispatch thread.
 * <p/>
 * <dl><dt><b>Attention:</b></dt><dd>Do not forget to register this manager via the {@link
 * DeliveryContextManagerAwt#register()} method.</dd></dl>
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextManagerAwt implements DeliveryContextManager<DeliveryContextAwt> {

    /**
     * The only delivery context of the AWD dispatch thread.
     */
    private DeliveryContextAwt mDeliveryContextAwt = new DeliveryContextAwt();

    /**
     * Registers this manager among other managers.
     */
    public static void register() {
        DeliveryContextManagers.registerDeliveryContextManager(new DeliveryContextManagerAwt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeliveryContextAwt getCurrentDeliveryContext() {
        if (EventQueue.isDispatchThread()) {
            return mDeliveryContextAwt;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentDeliveryContext(DeliveryContext deliveryContext) {
        return deliveryContext == mDeliveryContextAwt;
    }
}
