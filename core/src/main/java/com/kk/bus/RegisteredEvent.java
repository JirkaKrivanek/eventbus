package com.kk.bus;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * List of event deliverers related to the specific even object class. Each event deliverer consist of the registered
 * object instance, its subscriber and producer methods and the delivery context.
 *
 * @author Jiri Krivanek
 */
class RegisteredEvent {

    /**
     * Map of registered objects and related methods to call to handle the event.
     * <p/>
     * Keys are registered objects, values are sets of methods to call when event should be delivered.
     */
    private final Map<Object, EventDeliverer> mObjectDeliverers = new HashMap<>();

    /**
     * Registers the subscriber methods for the object.
     *
     * @param bus
     *         The bus object to be used to post the produced events.
     * @param objectToRegister
     *         The object to register.
     * @param subscriberMethods
     *         The set of subscriber methods to call to deliver the event.
     * @param deliveryContext
     *         The delivery context under which the producing and subscribing should occur for that object.
     */
    void register(Bus bus, Object objectToRegister, Set<Method> subscriberMethods, DeliveryContext deliveryContext) {
        synchronized (this) {
            EventDeliverer eventDeliverer = mObjectDeliverers.get(objectToRegister);
            if (eventDeliverer == null) {
                eventDeliverer = new EventDeliverer(bus, objectToRegister, deliveryContext);
                mObjectDeliverers.put(objectToRegister, eventDeliverer);
            }
            eventDeliverer.setSubscriberMethods(subscriberMethods);
        }
    }

    /**
     * Registers the producer methods for the object.
     *
     * @param bus
     *         The bus object to be used to post the produced events.
     * @param objectToRegister
     *         The object to register.
     * @param producerMethod
     *         The producer method to call to produce the event for the delivery. Can be {@code null}.
     * @param deliveryContext
     *         The delivery context under which the producing and subscribing should occur for that object.
     */
    void register(Bus bus, Object objectToRegister, Method producerMethod, DeliveryContext deliveryContext) {
        synchronized (this) {
            EventDeliverer eventDeliverer = mObjectDeliverers.get(objectToRegister);
            if (eventDeliverer == null) {
                eventDeliverer = new EventDeliverer(bus, objectToRegister, deliveryContext);
                mObjectDeliverers.put(objectToRegister, eventDeliverer);
            }
            eventDeliverer.setProducerMethod(producerMethod);
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
            registeredEventObjectContext = mObjectDeliverers.remove(objectToUnregister);
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
        // The synchronized copy of the object methods is here to avoid the concurrency issues
        // TODO: It is perfectly safe but pretty ineffective: Think out better solution
        synchronized (this) {
            eventDeliverers = new ArrayList<>(mObjectDeliverers.values());
        }
        for (EventDeliverer registeredEventObjectContext : eventDeliverers) {
            registeredEventObjectContext.requestCallSubscriberMethods(event);
        }
    }

    /**
     * Calls all the producer methods of the specified registered object.
     *
     * @param forThisRegisteredObjectOnly
     *         The object for which all its producers have to be called.
     */
    public void callProducer(Object forThisRegisteredObjectOnly) {
        EventDeliverer eventDeliverer;
        synchronized (this) {
            eventDeliverer = mObjectDeliverers.get(forThisRegisteredObjectOnly);
        }
        if (eventDeliverer != null) {
            eventDeliverer.requestCallProducerMethod();
        }
    }

    /**
     * Calls the producer methods returning the specified classes or any subclasses on all registered objects except the
     * specified one to exclude.
     *
     * @param forEventClassesOrSubclasses
     *         The set of classes for which the producers should be called.
     * @param excludeRegisteredObject
     *         The registered object to exclude from producing. If {@code null} then no exclusion.
     */
    public void callProducers(Set<Class<?>> forEventClassesOrSubclasses, Object excludeRegisteredObject) {
    }
}
