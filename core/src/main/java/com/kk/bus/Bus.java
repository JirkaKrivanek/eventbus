package com.kk.bus;


/**
 * Bus interface class.
 *
 * @author Jiri Krivanek
 */
public class Bus {

    /**
     * Denotes the broadcast subscriber and/or event.
     */
    public static final int BROADCAST_TOKEN = 0;

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
    public void register(final Object objectToRegister) {
        mRegisteredEvents.register(this, objectToRegister);
    }

    /**
     * Unregisters the object from the event bus.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>Unregistering the object which was not registered (or was already unregistered) has
     * no effect.</dd></dl>
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>Unregister method MUST be called from the same thread from which the register
     * was called, otherwise race conditions may happen.</dd></dl>
     *
     * @param objectToUnregister
     *         The object to unregister from the event bus.
     */
    public void unregister(final Object objectToUnregister) {
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
    public void post(final Object event) {
        mRegisteredEvents.post(event);
    }

    /**
     * Posts an event to the event bus.
     * <p/>
     * The event will be delivered to all registered objects. The delivery will occurs on the {@link DeliveryContext} on
     * which the {@link #register(Object)} method was called for that delivery context.
     *
     * @param event
     *         The event to post to the event bus.
     * @param token
     *         The token to which the event shall be delivered (subscribers filtering). If {@link Bus#BROADCAST_TOKEN}
     *         then the event is delivered to ALL subscribers irrespectively to the tokens.
     */
    public void post(final Object event, final int token) {
        mRegisteredEvents.post(event, token);
    }
}
