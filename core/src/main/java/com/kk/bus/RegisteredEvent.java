package com.kk.bus;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * List of events being registered to be handled by the bus.
 *
 * @author Jiri Krivanek
 */
class RegisteredEvent {

    /**
     * Map of registered objects and related methods to call to handle the event.
     * <p/>
     * Keys are registered objects, values are sets of methods to call when event should be delivered.
     */
    private final Map<Object, EventDeliverer> mObjectMethodsMap = new HashMap<>();

    /**
     * Registers the object to handle the event.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>Registering the object which has already been registered has no effect.</dd></dl>
     *
     * @param objectToRegister
     *         The object to register.
     * @param methods
     *         The set of methods to call to deliver the event.
     */
    void register(Object objectToRegister, Set<Method> methods) {
        EventDeliverer eventDeliverer = new EventDeliverer(objectToRegister,
                                                           methods,
                                                           DeliveryContextManagers.getCurrentDeliveryContext());
        synchronized (this) {
            mObjectMethodsMap.put(objectToRegister, eventDeliverer);
        }
    }

    /**
     * Unregisters the object handling the event.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>Unregistering the object which was not registered yet or has already been
     * unregistered has no effect.</dd></dl>
     *
     * @param objectToUnregister
     *         The object to unregister.
     */
    void unregister(Object objectToUnregister) {
        EventDeliverer registeredEventObjectContext;
        synchronized (this) {
            registeredEventObjectContext = mObjectMethodsMap.remove(objectToUnregister);
        }
        if (registeredEventObjectContext != null) {
            registeredEventObjectContext.clearOut();
        }
    }

    /**
     * Posts event to be delivered to the registered objects and their respective method(s).
     *
     * @param event
     *         The event to deliver.
     */
    void post(Object event) {
        Collection<EventDeliverer> eventDeliverers;
        synchronized (this) {
            eventDeliverers = new ArrayList<>(mObjectMethodsMap.values());
        }
        for (EventDeliverer registeredEventObjectContext : eventDeliverers) {
            registeredEventObjectContext.deliverEvent(event);
        }
    }
}
