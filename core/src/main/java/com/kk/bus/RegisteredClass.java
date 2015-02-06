package com.kk.bus;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Retrieves and maintains the annotated subscriber and producer methods for the specified class - AKA the registered
 * class descriptor.
 *
 * @author Jiri Krivanek
 */
class RegisteredClass {

    /**
     * Registered subscriber methods.
     * <p/>
     * Key is an event type class. Value is the set of methods handling that event
     */
    private Map<Class<?>, Set<Method>> mSubscriberMethods = null;

    /**
     * Registered producer methods.
     * <p/>
     * Key is the event type class. Value is the only registered method.
     */
    private Map<Class<?>, Method> mProducerMethods = null;

    /**
     * Retrieves and maintains the annotated methods for the specified class.
     *
     * @param registeredClass
     *         The class for which the annotated methods have to be retrieved.
     */
    RegisteredClass(Class<?> registeredClass) {
        for (Method method : registeredClass.getDeclaredMethods()) {
            if (!method.isBridge()) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires " +
                                                                   parameterTypes.length + " arguments. Methods must require a single argument.");
                    }
                    Class<?> eventType = parameterTypes[0];
                    if (eventType.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType +
                                                                   " which is an interface. Subscription must be on a concrete class type.");
                    }
                    if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType +
                                                                   " but is not 'public'.");
                    }
                    if (mSubscriberMethods == null) {
                        mSubscriberMethods = new HashMap<>();
                    }
                    Set<Method> methods = mSubscriberMethods.get(eventType);
                    if (methods == null) {
                        methods = new HashSet<>();
                        mSubscriberMethods.put(eventType, methods);
                    }
                    methods.add(method);
                } else if (method.isAnnotationPresent(Produce.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 0) {
                        throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires " + parameterTypes.length +
                                                                   " arguments. Methods must require zero arguments.");
                    }
                    if (method.getReturnType() == Void.class) {
                        throw new IllegalArgumentException("Method " + method + " has a return type of void. Must declare a non-void type.");
                    }
                    Class<?> eventType = method.getReturnType();
                    if (eventType.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType +
                                                                   " which is an interface. Producers must return a concrete class type.");
                    }
                    if (eventType.equals(Void.TYPE)) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
                    }
                    if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType +
                                                                   " but is not 'public'.");
                    }
                    if (mProducerMethods == null) {
                        mProducerMethods = new HashMap<>();
                    } else {
                        if (mProducerMethods.containsKey(eventType)) {
                            throw new IllegalArgumentException("Producer for type " + eventType + " has already been registered.");
                        }
                    }
                    mProducerMethods.put(eventType, method);
                }
            }
        }
    }

    /**
     * Checks whether there are any subscriber methods or nor.
     *
     * @return If there are any subscriber methods then {@code true} else {@code false}.
     */
    boolean hasAnySubscribers() {
        return mSubscriberMethods != null && mSubscriberMethods.size() > 0;
    }

    /**
     * Retrieves the set of subscribed event classes.
     *
     * @return The set of subscribed event classes.
     */
    Set<Class<?>> getSubscribedEvents() {
        if (mSubscriberMethods != null) {
            return mSubscriberMethods.keySet();
        }
        return null;
    }

    /**
     * Retrieves the set of produced event classes.
     * 
     * TODO: New/changed method: Unit test needed!
     *
     * @return The set of produced event classes.
     */
    Set<Class<?>> getProducedEvents() {
        if (mProducerMethods != null) {
            return mProducerMethods.keySet();
        }
        return null;
    }

    /**
     * Retrieves the subscriber methods for specified event class.
     *
     * @param forClass
     *         The class for which to retrieve the set of subscribing methods.
     * @return The set of subscribing methods or {@code null} if no methods subscribed.
     */
    Set<Method> getSubscribedMethods(Class<?> forClass) {
        if (mSubscriberMethods != null) {
            return mSubscriberMethods.get(forClass);
        }
        return null;
    }

    /**
     * Checks whether there are any producer methods or nor.
     *
     * @return If there are any producer methods then {@code true} else {@code false}.
     */
    boolean hasAnyProducers() {
        return mProducerMethods != null && mProducerMethods.size() > 0;
    }

    /**
     * Retrieves the producer method for the specified event class or its any subclass.
     *
     * TODO: New/changed method: Unit test needed!
     *
     * @param forClassOrAnySubclass
     *         The class or its any subclass for which the producer method has to be retrieved.
     * @return The producer method or {@code null} if no such producer exists.
     */
    Method getProducerMethodForClassOrAnySubclass(Class<?> forClassOrAnySubclass) {
        if (mProducerMethods != null) {
            for (Class<?> clazz : mProducerMethods.keySet()) {
                if (forClassOrAnySubclass.isAssignableFrom(clazz)) {
                    return mProducerMethods.get(clazz);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the producer method exactly for the specified event class.
     *
     * TODO: New/changed method: Unit test needed!
     *
     * @param forClass
     *         The class exactly for which the producer method has to be retrieved.
     * @return The producer method or {@code null} if no such producer exists.
     */
    Method getProducerMethodForClassExactly(Class<?> forClass) {
        if (mProducerMethods != null) {
            return mProducerMethods.get(forClass);
        }
        return null;
    }
}
