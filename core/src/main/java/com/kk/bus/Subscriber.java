package com.kk.bus;


/**
 * Subscriber of the event carrying the class and the token uniquely identifying particular subscriber within the
 * registered object.
 */
class Subscriber {

    private final Class<?> mEventClass;
    private final int      mToken;

    /**
     * Constructs the subscriber.
     *
     * @param eventClass
     *         The class to subscribe.
     * @param token
     *         The token to subscribe. Zero value means ALL events of the {@code aClass} will be handled.
     */
    Subscriber(final Class<?> eventClass, final int token) {
        mEventClass = eventClass;
        mToken = token;
    }

    /**
     * Retrieves the event class.
     *
     * @return The event class.
     */
    Class<?> getEventClass() {
        return mEventClass;
    }

    /**
     * Retrieves the event token.
     *
     * @return The event token. The zero value means ALL events of specified class are handled.
     */
    int getToken() {
        return mToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return -76565 + mEventClass.hashCode() + mToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Subscriber) {
            final Subscriber subscriber = (Subscriber) obj;
            if (mToken == subscriber.mToken && mEventClass.equals(subscriber.mEventClass)) {
                return true;
            }
        }
        return false;
    }
}
