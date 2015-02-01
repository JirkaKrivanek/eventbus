package com.kk.bus.thread;


import com.kk.bus.DeliveryContext;
import com.kk.bus.DeliveryContextManager;
import com.kk.bus.DeliveryContextManagers;

/**
 * Delivery context manager for the bus thread.
 *
 * @author Jiri Krivanek
 */
class DeliveryContextManagerThread implements DeliveryContextManager<DeliveryContextThread> {

    /**
     * The local variable of the current thread.
     * <p/>
     * For the threads of the {@link BusThread} class, it holds the delivery context. For all other threads, it is
     * {@code null}.
     */
    private static final ThreadLocal<DeliveryContextThread> sThreadDeliveryContext = new ThreadLocal<>();

    static {
        DeliveryContextManagers.registerDeliveryContextManager(new DeliveryContextManagerThread());
    }

    /**
     * Registers the delivery context manager to the currents thread local variable.
     *
     * @param deliveryContext
     *         The delivery context to register to the current threads local variable.
     */
    static void registerDeliveryContext(DeliveryContextThread deliveryContext) {
        sThreadDeliveryContext.set(deliveryContext);
    }

    /**
     * Unregisters the delivery context from the currents thread local variable.
     */
    static void clearDeliveryContext() {
        sThreadDeliveryContext.remove();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeliveryContextThread getCurrentDeliveryContext() {
        return sThreadDeliveryContext.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentDeliveryContext(DeliveryContext deliveryContext) {
        return deliveryContext == sThreadDeliveryContext.get();
    }
}
