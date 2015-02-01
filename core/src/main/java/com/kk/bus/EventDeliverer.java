package com.kk.bus;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Encapsulates the object and its subscriber methods and the delivery context.
 * <p/>
 * Serves methods for delivering events via the given delivery context.
 * <p/>
 * <dl><dt><b>Attention:</b></dt><dd>The user of this class MUST ensure that the object is of the class implementing the
 * methods. And the methods must take the correct event class instance as its only parameter. For more details, please
 * see the {@link RegisteredClass} class</dd></dl>
 *
 * @author Jiri Krivanek
 */
public class EventDeliverer {

    /**
     * The object to which the event has to be delivered.
     */
    private Object mObject;

    /**
     * The methods of the object to be called.
     */
    private Set<Method> mMethods;

    /**
     * The delivery context to be used to deliver the methods.
     */
    private DeliveryContext mDeliveryContext;

    /**
     * Constructs the registered event object.
     *
     * @param object
     *         The object to which the events have to be delivered.
     * @param methods
     *         The methods of the object to be called.
     * @param deliveryContext
     *         The delivery context to use to deliver the event.
     */
    EventDeliverer(Object object, Set<Method> methods, DeliveryContext deliveryContext) {
        mObject = object;
        mMethods = methods;
        mDeliveryContext = deliveryContext;
    }

    /**
     * Retrieves the delivery context through which the events are to be delivered.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     *
     * @return The delivery context.
     */
    public synchronized DeliveryContext getDeliveryContext() {
        return mDeliveryContext;
    }

    /**
     * Called when the object was unregistered from the bus to prevent any deferred calls take place.
     * <p/>
     * It is intentionally synchronized to avoid the race with the {@link #callMethods(Object)} and {@link
     * #deliverEvent(Object)} methods.
     */
    synchronized void clearOut() {
        mObject = null;
        mMethods = null;
        mDeliveryContext = null;
    }

    /**
     * Delivers the event to the object via the delivery context.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     *
     * @param event
     *         The event to deliver.
     */
    synchronized void deliverEvent(Object event) {
        if (mDeliveryContext != null) {
            mDeliveryContext.deliverEvent(event, this);
        }
    }

    /**
     * Performs the call to the methods of the object passing the event to them.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     *
     * @param event
     *         The event to pass to the methods.
     * @throws java.lang.reflect.InvocationTargetException
     *         When the method invocation failed.
     */
    synchronized void callMethods(Object event) throws InvocationTargetException {
        if (mObject != null && mMethods != null) {
            for (Method method : mMethods) {
                try {
                    method.invoke(mObject, event);
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e); // TODO: Find better solution (this is a brutal hack which suppresses the need for thrown exception declaration)
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof Error) {
                        throw (Error) e.getCause();
                    }
                    throw e;
                }
            }
        }
    }
}
