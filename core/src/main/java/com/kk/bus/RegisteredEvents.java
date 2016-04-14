package com.kk.bus;


import java.lang.reflect.InvocationTargetException;
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
     * Registered subscribed events.
     * <p/>
     * Keys are subscribers of the events, values are registered event objects.
     */
    private final Map<Subscriber, RegisteredEvent> mRegisteredSubscribedEvents = new HashMap<>();

    /**
     * Registered produced events.
     * <p/>
     * Keys are classes of the events, values are registered event objects.
     */
    private final Map<Class<?>, RegisteredEvent> mRegisteredProducedEvents = new HashMap<>();


    private static class ProducerSubscribers {

        final EventDeliverer       producer;
        final List<EventDeliverer> subscribers;

        ProducerSubscribers(EventDeliverer producer, List<EventDeliverer> subscribers) {
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
        final RegisteredClass registeredClass = mRegisteredClasses.getRegisteredClass(objectToRegister, true);
        final DeliveryContext deliveryContext = DeliveryContextManagers.getCurrentDeliveryContext();
        final Set<Subscriber> eventSubscribers = registeredClass.getEventSubscribers();
        final Set<Class<?>> producedEventClasses = registeredClass.getProducedEventClasses();

        // Registers subscriber methods
        if (eventSubscribers != null) {
            for (final Subscriber eventSubscriber : eventSubscribers) {
                RegisteredEvent registeredEvent;
                synchronized (this) {
                    registeredEvent = mRegisteredSubscribedEvents.get(eventSubscriber);
                    if (registeredEvent == null) {
                        registeredEvent = new RegisteredEvent();
                        mRegisteredSubscribedEvents.put(eventSubscriber, registeredEvent);
                    }
                }
                final Set<Method> subscriberMethods = registeredClass.getSubscriberMethods(eventSubscriber);
                registeredEvent.register(bus, objectToRegister, subscriberMethods, deliveryContext);
            }
        }

        // Register producer methods
        if (producedEventClasses != null) {
            for (Class<?> producedEventClass : producedEventClasses) {
                RegisteredEvent registeredEvent;
                synchronized (this) {
                    registeredEvent = mRegisteredProducedEvents.get(producedEventClass);
                    if (registeredEvent == null) {
                        registeredEvent = new RegisteredEvent();
                        mRegisteredProducedEvents.put(producedEventClass, registeredEvent);
                    }
                }
                final Method producerMethod = registeredClass.getProducerMethod(producedEventClass);
                registeredEvent.register(bus, objectToRegister, producerMethod, deliveryContext);
            }
        }

        // Collect producer-subscribers map
        List<ProducerSubscribers> eventProducerSubscribers = null;

        // First collect the producers of the just being registered class
        // - but only those which produce events of interest by some other registered subscribing object
        if (producedEventClasses != null) {
            for (final Class<?> producedEventClass : producedEventClasses) {
                // For each produced event class
                // Collect event deliverers
                // - For produced event class or any super class
                // - Having any subscriber method set
                List<EventDeliverer> eventDeliverers = null;
                synchronized (this) {
                    for (final Subscriber subscriber : mRegisteredSubscribedEvents.keySet()) {
                        if (subscriber.getEventClass().isAssignableFrom(producedEventClass)) {
                            final RegisteredEvent registeredEvent = mRegisteredSubscribedEvents.get(subscriber);
                            eventDeliverers = registeredEvent.retrieveEventDeliverersHavingAnySubscribers(
                                    eventDeliverers,
                                    null);
                        }
                    }
                }
                // Only if found any event deliverers with subscribers
                if (eventDeliverers != null) {
                    // Collect the event deliverer for producer class
                    // - For the object being registered
                    // - Having the producer method set
                    EventDeliverer eventProducer;
                    synchronized (this) {
                        eventProducer = mRegisteredProducedEvents.get(producedEventClass)
                                                                 .getEventDelivererWithProducerForRegisteredObject(
                                                                         objectToRegister);
                    }
                    if (eventProducer != null) {
                        if (eventProducerSubscribers == null) {
                            eventProducerSubscribers = new ArrayList<>();
                        }
                        eventProducerSubscribers.add(new ProducerSubscribers(eventProducer, eventDeliverers));
                    }
                }
            }
        }

        // Then collect the producers from all other registered objects
        // - except the currently being registered one
        // - for all event classes subscribed by the currently being registered object
        // - the subscriber is only the object currently being registered
        if (eventSubscribers != null) {
            for (final Subscriber eventSubscriber : eventSubscribers) {
                synchronized (this) {
                    for (final Class<?> producedEventClass : mRegisteredProducedEvents.keySet()) {
                        if (eventSubscriber.getEventClass().isAssignableFrom(producedEventClass)) {
                            RegisteredEvent re = mRegisteredProducedEvents.get(producedEventClass);
                            final List<EventDeliverer> eventProducers = re.getEventDeliverersHavingAnyProducer(null,
                                                                                                               objectToRegister);
                            if (eventProducers != null) {
                                re = mRegisteredSubscribedEvents.get(eventSubscriber);
                                final List<EventDeliverer> eventDeliverers = re.retrieveEventDeliverersHavingAnySubscribers(
                                        null,
                                        objectToRegister);
                                for (final EventDeliverer eventProducer : eventProducers) {
                                    if (eventProducerSubscribers == null) {
                                        eventProducerSubscribers = new ArrayList<>();
                                    }
                                    eventProducerSubscribers.add(new ProducerSubscribers(eventProducer,
                                                                                         eventDeliverers));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Finally call the production
        if (eventProducerSubscribers != null) {
            for (final ProducerSubscribers producerSubscribers : eventProducerSubscribers) {
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
    void unregister(final Object objectToUnregister) {
        final RegisteredClass registeredClass = mRegisteredClasses.getRegisteredClass(objectToUnregister, false);

        // Unregisters subscriber methods
        if (registeredClass != null) {
            final Set<Subscriber> subscribedEvents = registeredClass.getEventSubscribers();
            if (subscribedEvents != null) {
                for (final Subscriber eventSubscriber : subscribedEvents) {
                    RegisteredEvent registeredEvent;
                    synchronized (this) {
                        registeredEvent = mRegisteredSubscribedEvents.get(eventSubscriber);
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
     *         The event object to post. Never {@code null}.
     */
    void post(final Object event) {
        int subToken = Bus.BROADCAST_TOKEN;
        final Class<?> clazz = event.getClass();
        for (final Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Token.class)) {
                try {
                    final boolean needsAccessBoost = !method.isAccessible();
                    if (needsAccessBoost) {
                        method.setAccessible(true);
                    }
                    try {
                        subToken = (Integer) method.invoke(event);
                    } finally {
                        if (needsAccessBoost) {
                            method.setAccessible(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Could not retrieve token", e);
                } catch (InvocationTargetException e) {
                    throw new IllegalArgumentException("Could not retrieve token", e);
                }
                break;
            }
        }
        post(event, subToken);
    }

    /**
     * Posts the event to be delivered to the bus subscribers with specified token.
     *
     * @param event
     *         The event object to post.
     * @param token
     *         The token to which the event shall be delivered (subscribers filtering). If {@link Bus#BROADCAST_TOKEN}
     *         then the event is delivered to ALL subscribers irrespectively to the tokens.
     */
    void post(final Object event, final int token) {
        final List<RegisteredEvent> registeredEvents = new ArrayList<>();
        synchronized (this) {
            for (final Subscriber subscriber : mRegisteredSubscribedEvents.keySet()) {
                final int subToken = subscriber.getToken();
                if (token == Bus.BROADCAST_TOKEN || subToken == Bus.BROADCAST_TOKEN || subToken == token) {
                    if (subscriber.getEventClass().isInstance(event)) {
                        registeredEvents.add(mRegisteredSubscribedEvents.get(subscriber));
                    }
                }
            }
        }
        for (final RegisteredEvent registeredEvent : registeredEvents) {
            registeredEvent.post(event);
        }
    }
}
