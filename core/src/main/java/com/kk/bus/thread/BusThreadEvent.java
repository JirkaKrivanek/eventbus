package com.kk.bus.thread;


import com.kk.bus.EventDeliverer;

/**
 * Message item carrying the event to be delivered.
 *
 * @author Jiri Krivanek
 */
class BusThreadEvent {

    enum Type {SUBSCRIBE, PRODUCE}

    private Type mType;
    private Object         mEvent;
    private EventDeliverer mEventDeliverer;

    void set(Object event, EventDeliverer eventDeliverer) {
        mType = Type.SUBSCRIBE;
        mEvent = event;
        mEventDeliverer = eventDeliverer;
    }

    void set(EventDeliverer eventDeliverer) {
        mType = Type.PRODUCE;
        mEvent = null;
        mEventDeliverer = eventDeliverer;
    }

    void clear() {
        mEvent = null;
        mEventDeliverer = null;
    }

    public Type getType() {
        return mType;
    }

    Object getEvent() {
        return mEvent;
    }

    EventDeliverer getEventDeliverer() {
        return mEventDeliverer;
    }
}
