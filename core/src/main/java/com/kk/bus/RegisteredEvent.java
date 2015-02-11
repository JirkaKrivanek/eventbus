package com.kk.bus;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
     * Keys are registered objects, values are deliverer objects.
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
    synchronized void register(Bus bus,
                               Object objectToRegister,
                               Set<Method> subscriberMethods,
                               DeliveryContext deliveryContext) {
        EventDeliverer eventDeliverer = mObjectDeliverers.get(objectToRegister);
        if (eventDeliverer == null) {
            eventDeliverer = new EventDeliverer(bus, objectToRegister, deliveryContext);
            mObjectDeliverers.put(objectToRegister, eventDeliverer);
        }
        eventDeliverer.setSubscriberMethods(subscriberMethods);
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
    synchronized void register(Bus bus,
                               Object objectToRegister,
                               Method producerMethod,
                               DeliveryContext deliveryContext) {
        EventDeliverer eventDeliverer = mObjectDeliverers.get(objectToRegister);
        if (eventDeliverer == null) {
            eventDeliverer = new EventDeliverer(bus, objectToRegister, deliveryContext);
            mObjectDeliverers.put(objectToRegister, eventDeliverer);
        }
        eventDeliverer.setProducerMethod(producerMethod);
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
    synchronized void unregister(Object objectToUnregister) {
        EventDeliverer registeredEventObjectContext = mObjectDeliverers.remove(objectToUnregister);
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
        // TODO: This is perfectly safe but pretty ineffective: Think out better solution
        synchronized (this) {
            eventDeliverers = new ArrayList<>(mObjectDeliverers.values());
        }
        for (EventDeliverer registeredEventObjectContext : eventDeliverers) {
            registeredEventObjectContext.requestCallSubscriberMethods(event);
        }
    }

    /**
     * Retrieves the list of all event deliverers having any subscriber and adds them to the specified list.
     *
     * @param storeTo
     *         The output list to add the matching event deliverers to. If {@code null} then a new list is created and
     *         return.
     * @param onlyIncludeObjectToRegister
     *         If not {@code null} then only this object will be included with the result list.
     * @return The {@code storeTo} list or a new list (if the {@code storeTo} was passed as {@code null}).
     */
    synchronized List<EventDeliverer> retrieveEventDeliverersHavingAnySubscribers(List<EventDeliverer> storeTo,
                                                                                  Object onlyIncludeObjectToRegister) {
        if (onlyIncludeObjectToRegister == null) {
            for (EventDeliverer eventDeliverer : mObjectDeliverers.values()) {
                if (eventDeliverer.hasSubscriberMethods()) {
                    if (storeTo == null) {
                        storeTo = new ArrayList<>();
                    }
                    storeTo.add(eventDeliverer);
                }
            }
        } else {
            EventDeliverer eventDeliverer = mObjectDeliverers.get(onlyIncludeObjectToRegister);
            if (eventDeliverer != null) {
                if (eventDeliverer.hasSubscriberMethods()) {
                    if (storeTo == null) {
                        storeTo = new ArrayList<>();
                    }
                    storeTo.add(eventDeliverer);
                }
            }
        }
        return storeTo;
    }

    /**
     * Retrieves the list of all event deliverers and adds them to the specified list.
     *
     * @param storeTo
     *         The output list to add the matching event deliverers to. If {@code null} then a new list is created and
     *         return.
     * @param excludeObjectToRegister
     *         If not {@code null} then this object will be excluded from the result list.
     * @return The {@code storeTo} list or a new list (if the {@code storeTo} was passed as {@code null}).
     */
    synchronized List<EventDeliverer> getEventDeliverersHavingAnyProducer(List<EventDeliverer> storeTo,
                                                                          Object excludeObjectToRegister) {
        for (EventDeliverer eventDeliverer : mObjectDeliverers.values()) {
            if (eventDeliverer.hasProducerMethod()) {
                if (excludeObjectToRegister == null || !eventDeliverer.hasRegisteredObject(excludeObjectToRegister)) {
                    if (storeTo == null) {
                        storeTo = new ArrayList<>();
                    }
                    storeTo.add(eventDeliverer);
                }
            }
        }
        return storeTo;
    }

    synchronized EventDeliverer getEventDelivererWithProducerForRegisteredObject(Object registeredObject) {
        EventDeliverer eventDeliverer = mObjectDeliverers.get(registeredObject);
        if (eventDeliverer != null) {
            if (eventDeliverer.hasProducerMethod()) {
                return eventDeliverer;
            }
        }
        return null;
    }
}
