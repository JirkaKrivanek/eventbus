package com.kk.bus.thread;


import com.kk.bus.EventDeliverer;

import java.util.List;

/**
 * Message item carrying the event to be delivered.
 *
 * @author Jiri Krivanek
 */
class BusThreadEvent {

    enum Type {SUBSCRIBE, PRODUCE}


    private Type                 mType;
    private Object               mEvent;
    private EventDeliverer       mEventDeliverer;
    private List<EventDeliverer> mSubscriberDeliverers;

    void set(Object event, EventDeliverer eventDeliverer) {
        mType = Type.SUBSCRIBE;
        mEvent = event;
        mEventDeliverer = eventDeliverer;
        mSubscriberDeliverers = null;
    }

    void set(EventDeliverer eventDeliverer, List<EventDeliverer> subscriberDeliverers) {
        mType = Type.PRODUCE;
        mEvent = null;
        mEventDeliverer = eventDeliverer;
        mSubscriberDeliverers = subscriberDeliverers;
    }

    void clear() {
        mEvent = null;
        mEventDeliverer = null;
    }

    Type getType() {
        return mType;
    }

    Object getEvent() {
        return mEvent;
    }

    EventDeliverer getEventDeliverer() {
        return mEventDeliverer;
    }

    List<EventDeliverer> getSubscriberDeliverers() {
        return mSubscriberDeliverers;
    }
}
