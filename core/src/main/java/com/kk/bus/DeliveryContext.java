package com.kk.bus;


import java.lang.reflect.InvocationTargetException;

/**
 * Interface of the event delivery context.
 * <p/>
 * The event delivery context typically is a thread.
 * <p/>
 * The context is the same which called the {@link Bus#register(Object)} method.
 * <p/>
 * The events are delivered under the same context which was used to call the {@link Bus#register(Object)} method.
 *
 * @author Jiri Krivanek
 */
public abstract class DeliveryContext {

    /**
     * To be implemented by som subclass in order to defer the call to the context carried by the second parameter See
     * the {@link EventDeliverer#getDeliveryContext()} method.
     *
     * @param event
     *         The event to deliver.
     * @param eventDeliverer
     *         The object carrying the context through which the event has to be delivered.
     */
    protected abstract void deliverEvent(Object event, EventDeliverer eventDeliverer);

    /**
     * Performs the call of the methods.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>The subclass <b>MUST</b> ensure that this method is called on the delivery
     * context  carried by the second parameter See the {@link EventDeliverer#getDeliveryContext()} method.</dd></dl>
     *
     * @param event
     *         The event to deliver.
     * @param eventDeliverer
     *         The object carrying the target object and its methods to be invoked.
     * @throws java.lang.reflect.InvocationTargetException
     *         When there is any error in invoking the method.
     */
    protected void callMethods(Object event, EventDeliverer eventDeliverer) throws InvocationTargetException {
        if (eventDeliverer != null && event != null) { // Defense
            eventDeliverer.callMethods(event);
        }
    }
}
