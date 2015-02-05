package com.kk.bus;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registered classes manager.
 *
 * @author Jiri Krivanek
 */
class RegisteredClasses {

    private final Map<Class<?>, RegisteredClass> mRegisteredClassMap = new HashMap<>();

    /**
     * Retrieves the registered class descriptor for the specified object.
     * <p/>
     * If not registered yet then registers it (if allowed by the second parameter only).
     *
     * @param object
     *         The object for which to retrieve the registered class descriptor.
     * @param createIfUnknown
     *         Pass {@code true} to let the object to be created if not existing yet.
     * @return The registered class descriptor.
     */
    synchronized RegisteredClass getRegisteredClass(Object object, boolean createIfUnknown) {
        Class<?> classToRegister = object.getClass();
        RegisteredClass registeredClass = mRegisteredClassMap.get(classToRegister);
        if (registeredClass == null && createIfUnknown) {
            registeredClass = new RegisteredClass(classToRegister);
            mRegisteredClassMap.put(classToRegister, registeredClass);
        }
        return registeredClass;
    }

    /**
     * Retrieves the registered classes producing the specified events, taking the events inheritance into account.
     *
     * @param eventClass
     *         The event class or any subclass to be produced.
     * @return The list of registered classes. TODO: Optimize this: We already have the method to call to do the
     * production but later we will look for it again!
     */
    synchronized List<RegisteredClass> getRegisteredClassesProducingEvent(Class<?> eventClass) {
        List<RegisteredClass> registeredClasses = new ArrayList<>();
        for (Map.Entry<Class<?>, RegisteredClass> entry : mRegisteredClassMap.entrySet()) {
            Method method = entry.getValue().getProducerMethod(eventClass);
            if (method != null) {
                registeredClasses.add(entry.getValue());
            }
        }
        return registeredClasses;
    }
}
