package com.kk.bus;


import java.util.HashMap;
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
}
