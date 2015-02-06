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
     * The bus object to be used to post the produced events.
     */
    private Bus mBus;

    /**
     * The object to which the event has to be delivered.
     */
    private Object mObject;

    /**
     * The subscriber methods of the object to be called in order to deliver the event.
     */
    private Set<Method> mSubscriberMethods;

    /**
     * The producer method of the object to be called in order to produce the event.
     */
    private Method mProducerMethod;

    /**
     * The delivery context to be used to deliver the methods.
     */
    private DeliveryContext mDeliveryContext;

    /**
     * Constructs the registered event object.
     *
     * @param bus
     *         The bus object to be used to post the produced events.
     * @param object
     *         The object to which the events have to be delivered.
     * @param deliveryContext
     *         The delivery context to use to deliver the event.
     */
    EventDeliverer(Bus bus, Object object, DeliveryContext deliveryContext) {
        mBus = bus;
        mObject = object;
        mSubscriberMethods = null;
        mProducerMethod = null;
        mDeliveryContext = deliveryContext;
    }

    /**
     * Sets the subscriber methods for this event deliverer.
     *
     * @param subscriberMethods
     *         The subscriber methods of the object to be called in order to deliver the event.
     */
    synchronized void setSubscriberMethods(Set<Method> subscriberMethods) {
        mSubscriberMethods = subscriberMethods;
    }

    /**
     * Sets the producer methods for this event deliverer.
     *
     * @param producerMethod
     *         The producer method of the object to be called in order to produce the event.
     */
    synchronized void setProducerMethod(Method producerMethod) {
        mProducerMethod = producerMethod;
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
     * It is intentionally synchronized to avoid the race with the {@link #callSubscriberMethods(Object)} and {@link
     * #requestCallSubscriberMethods(Object)} methods.
     */
    synchronized void clearOut() {
        mBus = null;
        mObject = null;
        mSubscriberMethods = null;
        mProducerMethod = null;
        mDeliveryContext = null;
    }

    /**
     * Requests the call of the subscriber methods upon the registered object via the delivery context.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     *
     * @param event
     *         The event to deliver.
     */
    synchronized void requestCallSubscriberMethods(Object event) {
        if (mDeliveryContext != null) {
            mDeliveryContext.requestCallSubscriberMethods(event, this);
        }
    }

    /**
     * Requests the call of the producer method upon the registered object via the delivery context.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     */
    synchronized void requestCallProducerMethod() {
        if (mDeliveryContext != null) {
            mDeliveryContext.requestCallProducerMethod(this);
        }
    }

    /**
     * Performs the call to the subscriber methods of the object passing the event to them.
     * <p/>
     * It is sure the call is made on the correct delivery context (thread) - i.e. the one which called the {@link
     * Bus#register(Object)} method.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     *
     * @param event
     *         The event to pass to the methods.
     */
    synchronized void callSubscriberMethods(Object event) {
        if (mObject != null && mSubscriberMethods != null) {
            for (Method method : mSubscriberMethods) {
                try {
                    method.invoke(mObject, event);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e); // Not really nice but effective
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e); // Not really nice but effective
                }
            }
        }
    }

    /**
     * Performs the call to the producer method of the object.
     * <p/>
     * It is sure the call is made on the correct delivery context (thread) - i.e. the one which called the {@link
     * Bus#register(Object)} method.
     * <p/>
     * It is intentionally synchronized to prevent the race with the {@link #clearOut()} method.
     */
    synchronized void callProducerMethod() {
        if (mObject != null && mProducerMethod != null) {
            try {
                Object event = mProducerMethod.invoke(mObject);
                if (event != null) {
                    mBus.post(event);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e); // Not really nice but effective
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e); // Not really nice but effective
            }
        }
    }
}
