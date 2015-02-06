package com.kk.bus.thread;


import com.kk.bus.DeliveryContext;
import com.kk.bus.EventDeliverer;

/**
 * Delivery context for the bus thread.
 *
 * @author Jiri Krivanek
 */
class DeliveryContextThread extends DeliveryContext {

    private final BusThread mBusThread;

    /**
     * Constructs the delivery contexts and remembers the bus thread in it for posting the events to be delivered.
     *
     * @param busThread
     *         The bus thread to associate with this delivery context.
     */
    DeliveryContextThread(BusThread busThread) {
        mBusThread = busThread;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void requestCallSubscriberMethods(Object event, EventDeliverer eventDeliverer) {
        mBusThread.postEvent(event, eventDeliverer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void requestCallProducerMethod(EventDeliverer eventDeliverer) {
        mBusThread.postEvent(eventDeliverer);
    }

    /**
     * Calls the methods from within the context of the thread which registered itself to the bus.
     *
     * @param event
     *         The bus thread event object carrying the event to deliver.
     */
    void callMethods(BusThreadEvent event) {
        switch (event.getType()) {
            case SUBSCRIBE:
                callSubscriberMethods(event.getEvent(), event.getEventDeliverer());
                break;
            case PRODUCE:
                callProducerMethod(event.getEventDeliverer());
                break;
        }
    }
}
