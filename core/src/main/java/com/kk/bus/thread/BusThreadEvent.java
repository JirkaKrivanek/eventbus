package com.kk.bus.thread;


import com.kk.bus.EventDeliverer;

/**
 * Message item carrying the event to be delivered.
 *
 * @author Jiri Krivanek
 */
class BusThreadEvent {

    private Object         mEvent;
    private EventDeliverer mEventDeliverer;

    void set(Object event, EventDeliverer eventDeliverer) {
        mEvent = event;
        mEventDeliverer = eventDeliverer;
    }

    void clear() {
        mEvent = null;
        mEventDeliverer = null;
    }

    Object getEvent() {
        return mEvent;
    }

    EventDeliverer getEventDeliverer() {
        return mEventDeliverer;
    }
}
