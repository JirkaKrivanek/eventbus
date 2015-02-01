package com.kk.bus;


import java.util.HashSet;
import java.util.Set;

/**
 * Maintains and serves the list of registered delivery context managers.
 *
 * @author Jiri Krivanek
 */
public class DeliveryContextManagers {

    private static final Set<DeliveryContextManager> sDeliveryContextManagers = new HashSet<>();

    /**
     * This is likely internal method to be used by the unit tests. There is hardly any use in the real life.
     */
    public static synchronized void clearDeliveryContextManagers() {
        sDeliveryContextManagers.clear();
    }

    /**
     * Registers the delivery context manager.
     *
     * @param deliveryContextManager
     *         The delivery context manager to register.
     */
    public static synchronized void registerDeliveryContextManager(DeliveryContextManager deliveryContextManager) {
        sDeliveryContextManagers.add(deliveryContextManager);
    }

    /**
     * Retrieves the current delivery context.
     *
     * @return The current delivery context.
     */
    static synchronized DeliveryContext getCurrentDeliveryContext() {
        for (DeliveryContextManager deliveryContextManager : sDeliveryContextManagers) {
            DeliveryContext deliveryContext = deliveryContextManager.getCurrentDeliveryContext();
            if (deliveryContext != null) {
                return deliveryContext;
            }
        }
        return null;
    }

    /**
     * Checks the specified delivery context matches the current delivery context.
     *
     * @return If the specified delivery context matches the current delivery context then {@code true} else {@code
     * false}.
     */
    static synchronized boolean isCurrentDeliveryContext(DeliveryContext deliveryContext) {
        for (DeliveryContextManager deliveryContextManager : sDeliveryContextManagers) {
            if (deliveryContextManager.isCurrentDeliveryContext(deliveryContext)) {
                return true;
            }
        }
        return false;
    }
}
