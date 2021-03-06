package com.kk.bus;


import java.util.List;

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
     * Requests the call of the subscriber methods deferred by this delivery context.
     * <p/>
     * See also the {@link EventDeliverer#getDeliveryContext()} method.
     *
     * @param event
     *         The event to deliver.
     * @param eventDeliverer
     *         The object carrying the context through which the event has to be delivered.
     */
    protected abstract void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer);

    /**
     * Requests the call of the producer method deferred by this delivery context.
     * <p/>
     * See also the {@link EventDeliverer#getDeliveryContext()} method.
     *
     * @param producerDeliverer
     *         The producer deliverer to be used to produce the event.
     * @param subscriberDeliverers
     *         The subscriber deliverers which should be used as soon as the event is produced.
     */
    protected abstract void requestCallProducerMethod(EventDeliverer producerDeliverer,
                                                      List<EventDeliverer> subscriberDeliverers);

    /**
     * Performs the call of the subscriber methods.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>The subclass <b>MUST</b> ensure that this method is called on the delivery
     * context carried by the second parameter.
     * <p/>
     * See the {@link EventDeliverer#getDeliveryContext()} method.</dd></dl>
     *
     * @param event
     *         The event to deliver.
     * @param eventDeliverer
     *         The object carrying the target object and its methods to be invoked.
     */
    protected void callSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
        if (eventDeliverer != null && event != null) { // Defense
            eventDeliverer.callSubscriberMethods(event);
        }
    }

    /**
     * Performs the call of the producer method.
     * <p/>
     * <dl><dt><b>Attention:</b></dt><dd>The subclass <b>MUST</b> ensure that this method is called on the delivery
     * context carried by the parameter.
     * <p/>
     * See the {@link EventDeliverer#getDeliveryContext()} method.</dd></dl>
     *
     * @param producerDeliverer
     *         The producer deliverer to be used to produce the event.
     * @param subscriberDeliverers
     *         The subscriber deliverers which should be used as soon as the event is produced.
     */
    protected void callProducerMethod(EventDeliverer producerDeliverer, List<EventDeliverer> subscriberDeliverers) {
        if (producerDeliverer != null) { // Defense
            producerDeliverer.callProducerMethod(subscriberDeliverers);
        }
    }
}
