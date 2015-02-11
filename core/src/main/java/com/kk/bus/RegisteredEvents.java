package com.kk.bus;


import java.lang.reflect.Method;
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


    private class ProducerSubscribers {

        EventDeliverer       producer;
        List<EventDeliverer> subscribers;

        public ProducerSubscribers(EventDeliverer producer, List<EventDeliverer> subscribers) {
            this.producer = producer;
            this.subscribers = subscribers;
        }
    }

    /**
     * Registers the object to the bus.
     *
     * @param bus
     *         The bus object to be used to post the produced events.
     * @param objectToRegister
     *         The object to register to the bus.
     */
    void register(Bus bus, Object objectToRegister) {
        RegisteredClass registeredClass = mRegisteredClasses.getRegisteredClass(objectToRegister, true);
        DeliveryContext deliveryContext = DeliveryContextManagers.getCurrentDeliveryContext();
        Set<Class<?>> subscribedEvents = registeredClass.getSubscribedEvents();
        Set<Class<?>> producedEvents = registeredClass.getProducedEvents();

        // Registers subscriber methods
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
                Set<Method> subscriberMethods = registeredClass.getSubscribedMethods(eventClass);
                registeredEvent.register(bus, objectToRegister, subscriberMethods, deliveryContext);
            }
        }

        // Register producer methods
        if (producedEvents != null) {
            for (Class<?> eventClass : producedEvents) {
                RegisteredEvent registeredEvent;
                synchronized (this) {
                    registeredEvent = mRegisteredEvents.get(eventClass);
                    if (registeredEvent == null) {
                        registeredEvent = new RegisteredEvent();
                        mRegisteredEvents.put(eventClass, registeredEvent);
                    }
                }
                Method producerMethod = registeredClass.getProducerMethodForClassExactly(eventClass);
                registeredEvent.register(bus, objectToRegister, producerMethod, deliveryContext);
            }
        }

        // Collect producer-subscribers map
        List<ProducerSubscribers> eventProducerSubscribers = null;

        // First collect the producers of the just being registered class
        // - but only those which produce events of interest by some other registered subscribing object
        if (producedEvents != null) {
            for (Class<?> producedEventClass : producedEvents) {
                // For each produced event class
                // Collect event deliverers
                // - For produced event class or any super class
                // - Having any subscriber method set
                List<EventDeliverer> eventSubscribers = null;
                synchronized (this) {
                    for (Class<?> subscribedEventClass : mRegisteredEvents.keySet()) {
                        if (subscribedEventClass.isAssignableFrom(producedEventClass)) {
                            RegisteredEvent registeredEvent = mRegisteredEvents.get(subscribedEventClass);
                            eventSubscribers = registeredEvent.retrieveEventDeliverersHavingAnySubscribers(
                                    eventSubscribers,
                                    null);
                        }
                    }
                }
                // Only if found any event deliverers with subscribers
                if (eventSubscribers != null) {
                    // Collect the event deliverer for producer class
                    // - For the object being registered
                    // - Having the producer method set
                    EventDeliverer eventProducer;
                    synchronized (this) {
                        eventProducer = mRegisteredEvents.get(producedEventClass).getEventDelivererWithProducerForRegisteredObject(
                                objectToRegister);
                    }
                    if (eventProducer != null) {
                        if (eventProducerSubscribers == null) {
                            eventProducerSubscribers = new ArrayList<>();
                        }
                        eventProducerSubscribers.add(new ProducerSubscribers(eventProducer, eventSubscribers));
                    }
                }
            }
        }

        // Then collect the producers from all other registered objects
        // - except the currently being registered one
        // - for all event classes subscribed by the currently being registered one
        // - the subscriber is only the object currently being registered
        if (subscribedEvents != null) {
            for (Class<?> subscribedEventClass : subscribedEvents) {
                synchronized (this) {
                    for (Class<?> producedEventClass : mRegisteredEvents.keySet()) {
                        if (subscribedEventClass.isAssignableFrom(producedEventClass)) {
                            List<EventDeliverer> eventProducers = mRegisteredEvents.get(producedEventClass).getEventDeliverersHavingAnyProducer(
                                    null,
                                    objectToRegister);
                            if (eventProducers != null) {
                                RegisteredEvent registeredEvent = mRegisteredEvents.get(subscribedEventClass);
                                List<EventDeliverer> eventSubscribers = registeredEvent.retrieveEventDeliverersHavingAnySubscribers(
                                        null,
                                        objectToRegister);
                                for (EventDeliverer eventProducer : eventProducers) {
                                    if (eventProducerSubscribers == null) {
                                        eventProducerSubscribers = new ArrayList<>();
                                    }
                                    eventProducerSubscribers.add(new ProducerSubscribers(eventProducer,
                                                                                         eventSubscribers));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Finally call the production
        if (eventProducerSubscribers != null) {
            for (ProducerSubscribers producerSubscribers : eventProducerSubscribers) {
                producerSubscribers.producer.requestCallProducerMethod(producerSubscribers.subscribers);
            }
        }
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
        List<RegisteredEvent> registeredEvents = new ArrayList<>();
        synchronized (this) {
            for (Class<?> clazz : mRegisteredEvents.keySet()) {
                if (clazz.isInstance(event)) {
                    registeredEvents.add(mRegisteredEvents.get(clazz));
                }
            }
        }
        for (RegisteredEvent registeredEvent : registeredEvents) {
            registeredEvent.post(event);
        }
    }
}
