package com.kk.bus.thread;


import com.kk.bus.Bus;
import com.kk.bus.EventDeliverer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread which handles the events delivered via the bus.
 * <p/>
 * <dl><dt><b>Attention:</b></dt><dd>Do not forget to call the {@link #init()} method at the point in which you want to
 * start the events delivery.</dd></dl>
 *
 * @author Jiri Krivanek
 */
public class BusThread extends Thread {

    private static final int MAX_POOL_SIZE = 100;
    private final Bus mBus;
    private final Queue<BusThreadEvent> mEventsQueue = new LinkedList<>();
    private final Queue<BusThreadEvent> mEventsPool  = new LinkedList<>();
    private final DeliveryContextThread mDeliveryContextThread;

    /**
     * Constructs the bus thread.
     * <p/>
     * It does not start the thread.
     *
     * @param bus
     *         The event bus to which the thread will be attached.
     */
    public BusThread(Bus bus) {
        mBus = bus;
        mDeliveryContextThread = new DeliveryContextThread(this);
    }

    /**
     * Terminates the thread operation and waits until it really finishes.
     *
     * @throws InterruptedException
     */
    public void terminateAndWait() throws InterruptedException {
        interrupt();
        synchronized (this) {
            notify();
        }
        join();
    }

    /**
     * Terminates the thread operation and waits until it really finishes.
     *
     * @param millis
     *         The timeout in milliseconds to wait for the thread to finish.
     * @return If the thread finished in time then {@code true} else {@code false}.
     * @throws InterruptedException
     */
    public boolean terminateAndWait(long millis) throws InterruptedException {
        interrupt();
        synchronized (this) {
            notify();
        }
        join(millis);
        return !isAlive();
    }

    /**
     * Terminates the thread operation w/o waiting until it really finishes.
     */
    public void terminate() {
        interrupt();
        synchronized (this) {
            notify();
        }
    }

    /**
     * By default, the standard thread run method registers to the event bus and handles events posted to this thread
     * until terminated.
     */
    @Override
    public void run() {
        try {
            loop();
        } finally {
            done();
        }
    }

    /**
     * Initializes the thread for bus.
     */
    public void init() {
        DeliveryContextManagerThread.registerDeliveryContext(mDeliveryContextThread);
        mBus.register(this);
    }

    /**
     * Deinitializes th thread from bus.
     */
    void done() {
        mBus.unregister(this);
        DeliveryContextManagerThread.clearDeliveryContext();
    }

    /**
     * Handles all the events posted to this thread.
     *
     * @throws InterruptedException
     */
    void handleOrWait() throws InterruptedException {
        while (!isInterrupted()) {
            BusThreadEvent event;
            synchronized (this) {
                event = mEventsQueue.poll();
                if (event == null) {
                    wait();
                    return;
                }
            }
            deliverEvent(event);
        }
    }

    /**
     * Tries to handle one event posted to this thread.
     *
     * @return If the event was handled then {@code true} else {@code false}.
     */
    protected boolean tryToHandle() {
        BusThreadEvent event;
        synchronized (this) {
            event = mEventsQueue.poll();
            if (event == null) {
                return false;
            }
        }
        deliverEvent(event);
        return true;
    }

    /**
     * Handles events posted to this thread until terminated.
     */
    void loop() {
        while (!isInterrupted()) {
            try {
                handleOrWait();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Posts the event to be handled by the thread. And ensures the thread is awake.
     *
     * @param event
     *         The event to post.
     */
    synchronized void postEvent(Object event, EventDeliverer eventDeliverer) {
        BusThreadEvent busThreadEvent = mEventsPool.poll();
        if (busThreadEvent == null) {
            busThreadEvent = new BusThreadEvent();
        }
        busThreadEvent.set(event, eventDeliverer);
        mEventsQueue.add(busThreadEvent);
        notify();
    }

    /**
     * Delivers event and returns the object back to pool.
     *
     * @param event
     *         The event to deliver.
     */
    private void deliverEvent(BusThreadEvent event) {
        mDeliveryContextThread.callMethods(event);
        event.clear();
        synchronized (this) {
            if (mEventsPool.size() < MAX_POOL_SIZE) {
                mEventsPool.add(event);
            }
        }
    }
}
