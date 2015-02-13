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
     * Registers the delivery context manager.
     *
     * @param deliveryContextManager
     *         The delivery context manager to register.
     */
    public static synchronized void registerDeliveryContextManager(DeliveryContextManager deliveryContextManager) {
        sDeliveryContextManagers.add(deliveryContextManager);
    }

    /**
     * Unregisters the delivery context manager.
     *
     * @param deliveryContextManager
     *         The delivery context manager to unregister.
     */
    public static synchronized void unregisterDeliveryContextManager(DeliveryContextManager deliveryContextManager) {
        sDeliveryContextManagers.remove(deliveryContextManager);
    }

    /**
     * Retrieves the current delivery context.
     *
     * @return The current delivery context.
     * @throws IllegalStateException
     *         When there is no delivery context found for the current thread.
     */
    static synchronized DeliveryContext getCurrentDeliveryContext() {
        for (DeliveryContextManager deliveryContextManager : sDeliveryContextManagers) {
            DeliveryContext deliveryContext = deliveryContextManager.getCurrentDeliveryContext();
            if (deliveryContext != null) {
                return deliveryContext;
            }
        }
        Thread thread = Thread.currentThread();
        throw new IllegalStateException(new StringBuilder().append("Missing delivery context for current thread ")
                                                           .append(thread.getId())
                                                           .append(":")
                                                           .append(thread.getName())
                                                           .append(":")
                                                           .append(thread.getState().toString())
                                                           .append(". Are you sure you registered required delivery context manager(s)?")
                                                           .toString());
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
