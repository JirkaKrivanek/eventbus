package com.kk.bus.thread;


import com.kk.bus.Bus;
import com.kk.bus.EventDeliverer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

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
    private final CountDownLatch        mStarted     = new CountDownLatch(1);
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
        super();
        mBus = bus;
        mDeliveryContextThread = new DeliveryContextThread(this);
    }

    /**
     * Starts the thread operation and waits until the thread executes and registers to the bus.
     *
     * @throws InterruptedException
     */
    public void startAndWait() throws InterruptedException {
        start();
        mStarted.await();
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
        init();
        try {
            loop();
        } finally {
            done();
        }
    }

    /**
     * Initializes the thread for bus.
     * <p/>
     * <dl><dt>Attention:</dt><dd>Must be called from within the context of the</dd></dl>
     */
    protected void init() {
        DeliveryContextManagerThread.registerDeliveryContext(mDeliveryContextThread);
        mBus.register(this);
        mStarted.countDown();
    }

    /**
     * Deinitializes th thread from bus.
     */
    protected void done() {
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
     * Handles events posted to this thread until terminated.
     */
    void loop() {
        try {
            while (!isInterrupted()) {
                handleOrWait();
            }
        } catch (InterruptedException e) {
            // Intentionally empty: This exception fulfilled its purpose: Terminated the thread
        }
    }

    /**
     * Subscriber variant: Posts the event to be handled by the thread. And ensures the thread is awaken.
     *
     * @param event
     *         The event to post.
     * @param eventDeliverer
     *         The object to be used to deliver the event.
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
     * Producer variant: Posts the event to be handled by the thread. And ensures the thread is awaken.
     *
     * @param eventDeliverer
     *         The object to be used to deliver the event.
     * @param subscriberDeliverers
     *         The subscriber deliverers which should be used as soon as the event is produced.
     */
    synchronized void postEvent(EventDeliverer eventDeliverer, List<EventDeliverer> subscriberDeliverers) {
        BusThreadEvent busThreadEvent = mEventsPool.poll();
        if (busThreadEvent == null) {
            busThreadEvent = new BusThreadEvent();
        }
        busThreadEvent.set(eventDeliverer, subscriberDeliverers);
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
