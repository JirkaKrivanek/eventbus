package com.kk.bus.awt;


import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;
import javafx.application.Platform;

/**
 * Manager of the delivery context for the AWT dispatch thread.
 * <p/>
 * <dl><dt><b>Attention:</b></dt><dd>Do not forget to register this manager via the {@link
 * DeliveryContextManagerJfx#register()} method.</dd></dl>
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextManagerJfx implements DeliveryContextManager<DeliveryContextJfx> {

    /**
     * The only delivery context of the AWD dispatch thread.
     */
    private DeliveryContextJfx mDeliveryContextAwt = new DeliveryContextJfx();

    /**
     * Registers this manager among other managers.
     */
    public static void register() {
        DeliveryContextManagers.registerDeliveryContextManager(new DeliveryContextManagerJfx());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeliveryContextJfx getCurrentDeliveryContext() {
        if (Platform.isFxApplicationThread()) {
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
