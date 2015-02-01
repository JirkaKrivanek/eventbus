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
     * Retrieves the registered class object for the specified class.
     * <p/>
     * If not registered yet then registers it.
     *
     * @param objectToUnregister
     *         The object to register.
     * @param createIfUnknown
     *         Pass {@code true} to let the object to be created if not existing yet.
     * @return The registered class object.
     */
    synchronized RegisteredClass getRegisteredClass(Object objectToUnregister, boolean createIfUnknown) {
        Class<?> classToRegister = objectToUnregister.getClass();
        RegisteredClass registeredClass = mRegisteredClassMap.get(classToRegister);
        if (registeredClass == null && createIfUnknown) {
            registeredClass = new RegisteredClass(classToRegister);
            mRegisteredClassMap.put(classToRegister, registeredClass);
        }
        return registeredClass;
    }
}
