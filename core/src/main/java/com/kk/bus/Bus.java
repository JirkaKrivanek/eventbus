package com.kk.bus;


/**
 * Bus interface class.
 *
 * @author Jiri Krivanek
 */
public class Bus {

    private final RegisteredEvents mRegisteredEvents = new RegisteredEvents();

    /**
     * Registers an object with the event bus.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>Registering the same object more than once has no effect (no reference
     * counting).</dd></dl>
     * <p/>
     * The register method is called for the specified object from within certain {@link DeliveryContext}. The events
     * for that object will be delivered from within the same context.
     *
     * @param objectToRegister
     *         The object to register to the event bus.
     */
    public void register(Object objectToRegister) {
        mRegisteredEvents.register(objectToRegister);
    }

    /**
     * Unregisters the object from the event bus.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>Unregistering the object which was not registered (or was already unregistered) has
     * no effect.</dd></dl>
     *
     * @param objectToUnregister
     *         The object to unregister from the event bus.
     */
    public void unregister(Object objectToUnregister) {
        mRegisteredEvents.unregister(objectToUnregister);
    }

    /**
     * Posts an event to the event bus.
     * <p/>
     * The event will be delivered to all registered objects. The delivery will occurs on the {@link DeliveryContext} on
     * which the {@link #register(Object)} method was called for that delivery context.
     *
     * @param event
     *         The event to post to the event bus.
     */
    public void post(Object event) {
        mRegisteredEvents.post(event);
    }
}
