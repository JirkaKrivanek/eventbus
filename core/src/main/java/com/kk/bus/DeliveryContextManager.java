package com.kk.bus;


/**
 * Interface of the delivery context manager.
 * <p/>
 * To be subclassed for the target system (for instance Android or Swing).
 *
 * @author Jiri Krivanek
 * @see DeliveryContext
 */
public interface DeliveryContextManager<T extends DeliveryContext> {

    /**
     * Retrieves the delivery context for the current execution context (thread).
     * <p/>
     * To be implemented by the subclass according to the target system capabilities (e.g. for Android it can be based
     * on the loopers and handlers).
     * <p/>
     * <dl><dt><b>Attention:</b></dt>The object returned for this thread MUST be the same as the one for the same thread
     * in the past - i.e. one thread = exactly one instance of delivery context.</dl>
     *
     * @return The delivery context object for the current execution context (thread). Or {@code null} if this delivery
     * context manager does not know the delivery context for the current thread.
     */
    abstract T getCurrentDeliveryContext();

    /**
     * Checks whether the given delivery context is the same as the context of the current execution context (thread).
     * <p/>
     * To be implemented by the subclass according to the target system capabilities (e.g. for Android it can be based
     * on the loopers and handlers).
     *
     * @param deliveryContext
     *         The delivery context to check whether it is the current one.
     * @return If the specified delivery context is the current one then {@code true} else {@code false}.
     */
    abstract boolean isCurrentDeliveryContext(DeliveryContext deliveryContext);
}
