package com.kk.bus;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manager of events registered to be handled by the bus.
 *
 * @author Jiri Krivanek
 */
class RegisteredEvents {

    /**
     * Cache of classes of the objects registered to the event bus.
     * <p/>
     * <dl><dt><b>Note:</b></dt><dd>The cache is currently only be filled in - i.e. no clean up of the registered class
     * objects which are not needed any more.</dd></dl>
     */
    private final RegisteredClasses mRegisteredClasses = new RegisteredClasses();

    /**
     * Registered events.
     * <p/>
     * Keys are classes of the events, values are registered event objects.
     */
    private final Map<Class<?>, RegisteredEvent> mRegisteredEvents = new HashMap<>();

    /**
     * Registers the object to the bus.
     *
     * @param objectToRegister
     *         The object to register to the bus.
     */
    void register(Object objectToRegister) {
        RegisteredClass registeredClass = mRegisteredClasses.getRegisteredClass(objectToRegister, true);

        // Registers subscriber methods
        Set<Class<?>> subscribedEvents = registeredClass.getSubscribedEvents();
        if (subscribedEvents != null) {
            for (Class<?> eventClass : subscribedEvents) {
                RegisteredEvent registeredEvent;
                synchronized (this) {
                    registeredEvent = mRegisteredEvents.get(eventClass);
                    if (registeredEvent == null) {
                        registeredEvent = new RegisteredEvent();
                        mRegisteredEvents.put(eventClass, registeredEvent);
                    }
                }
                registeredEvent.register(objectToRegister, registeredClass.getSubscribedMethods(eventClass));
            }
        }

        // Call all the producers of the registered class now
        // Call producers of all other registered methods but only those producing any method which this class is registered for
    }

    /**
     * Unregisters object from the bus.
     *
     * @param objectToUnregister
     *         The object to unregister from the bus.
     */
    void unregister(Object objectToUnregister) {
        RegisteredClass registeredClass = mRegisteredClasses.getRegisteredClass(objectToUnregister, false);

        // Unregisters subscriber methods
        if (registeredClass != null) {
            Set<Class<?>> subscribedEvents = registeredClass.getSubscribedEvents();
            if (subscribedEvents != null) {
                for (Class<?> eventClass : subscribedEvents) {
                    RegisteredEvent registeredEvent;
                    synchronized (this) {
                        registeredEvent = mRegisteredEvents.get(eventClass);
                    }
                    if (registeredEvent != null) {
                        registeredEvent.unregister(objectToUnregister);
                    }
                }
            }
        }
    }

    /**
     * Posts the event to be delivered to the bus subscribers.
     *
     * @param event
     *         The event object to post.
     */
    void post(Object event) {
        List<RegisteredEvent> registeredEvents = getRegisteredEvents(event);
        for (RegisteredEvent registeredEvent : registeredEvents) {
            registeredEvent.post(event);
        }
    }

    /**
     * Retrieves the list of registered event object for the specified object. The object class must equal or inherit
     * from the registered class.
     *
     * @param forObject
     *         The object for which the list of registered events has to be returned.
     * @return The list of registered events.
     */
    private synchronized List<RegisteredEvent> getRegisteredEvents(Object forObject) {
        List<RegisteredEvent> ret = new ArrayList<>();
        for (Class<?> clazz : mRegisteredEvents.keySet()) {
            if (clazz.isInstance(forObject)) {
                ret.add(mRegisteredEvents.get(clazz));
            }
        }
        return ret;
    }
}
