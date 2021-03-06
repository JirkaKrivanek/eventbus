package com.kk.bus.integration;


import com.kk.bus.Bus;
import com.kk.bus.Subscribe;
import com.kk.bus.thread.BusThread;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The multi threaded tests.
 */
public class MultiThreadedSubscriberTest {

    private static Bus sBus;


    private static class EventA {}


    private static class EventB {}


    private static class EventC {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @BeforeClass
    public static void setUpTest() {
        sBus = new Bus();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Normal delivery
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static class NormalDeliveryA extends BusThread {

        final CountDownLatch eventCount;

        public NormalDeliveryA(Bus bus, int count) throws InterruptedException {
            super(bus);
            eventCount = new CountDownLatch(count);
            setName("normalDeliveryA");
            setDaemon(true);
                startAndWait();
        }

        @Subscribe
        public void onEventA(EventA event) {
            eventCount.countDown();
        }
    }


    public static class NormalDeliveryB extends BusThread {

        final CountDownLatch eventCount;

        public NormalDeliveryB(Bus bus, int count) throws InterruptedException {
            super(bus);
            eventCount = new CountDownLatch(count);
            setName("normalDeliveryB");
            setDaemon(true);
            startAndWait();
        }

        @Subscribe
        public void onEventB(EventB event) {
            eventCount.countDown();
        }
    }


    public static class NormalDeliveryC extends BusThread {

        final CountDownLatch eventCount;

        public NormalDeliveryC(Bus bus, int count) throws InterruptedException {
            super(bus);
            eventCount = new CountDownLatch(count);
            setName("normalDeliveryC");
            setDaemon(true);
            startAndWait();
        }

        @Subscribe
        public void onEventC(EventC event) {
            eventCount.countDown();
        }
    }


    public static class NormalDeliveryABC extends BusThread {

        final CountDownLatch eventCount;

        public NormalDeliveryABC(Bus bus, int count) throws InterruptedException {
            super(bus);
            eventCount = new CountDownLatch(3 * count);
            setName("normalDeliveryABC");
            setDaemon(true);
            startAndWait();
        }

        @Subscribe
        public void onEventA(EventA event) {
            eventCount.countDown();
        }

        @Subscribe
        public void onEventB(EventB event) {
            eventCount.countDown();
        }

        @Subscribe
        public void onEventC(EventC event) {
            eventCount.countDown();
        }
    }

    @Test
    public void normalDeliveryA() throws Exception {
        NormalDeliveryA normalDeliveryA = new NormalDeliveryA(sBus, 1);
        sBus.post(new EventA());
        sBus.post(new EventB());
        assertTrue(normalDeliveryA.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryA.terminateAndWait(2000));
    }

    @Test
    public void normalDeliveryB() throws Exception {
        NormalDeliveryB normalDeliveryB = new NormalDeliveryB(sBus, 1);
        sBus.post(new EventA());
        sBus.post(new EventB());
        assertTrue(normalDeliveryB.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryB.terminateAndWait(2000));
    }

    @Test
    public void stressByManyEventsDeliveryA() throws Exception {
        final int COUNT = 10000;
        NormalDeliveryA normalDeliveryA = new NormalDeliveryA(sBus, COUNT);
        for (int i = 0; i < COUNT; i++) {
            sBus.post(new EventA());
            sBus.post(new EventB());
        }
        assertTrue(normalDeliveryA.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryA.terminateAndWait(2000));
    }

    @Test
    public void stressByManyEventsDeliveryB() throws Exception {
        final int COUNT = 10000;
        NormalDeliveryB normalDeliveryB = new NormalDeliveryB(sBus, COUNT);
        for (int i = 0; i < COUNT; i++) {
            sBus.post(new EventA());
            sBus.post(new EventB());
        }
        assertTrue(normalDeliveryB.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryB.terminateAndWait(2000));
    }

    @Test
    public void stressByManyEventsDeliveryAB() throws Exception {
        final int COUNT = 10000;
        NormalDeliveryA normalDeliveryA = new NormalDeliveryA(sBus, COUNT);
        NormalDeliveryB normalDeliveryB = new NormalDeliveryB(sBus, COUNT);
        for (int i = 0; i < COUNT; i++) {
            sBus.post(new EventA());
            sBus.post(new EventB());
        }
        assertTrue(normalDeliveryA.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryB.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryA.terminateAndWait(2000));
        assertTrue(normalDeliveryB.terminateAndWait(2000));
    }

    @Test
    public void stressByManyEventsDeliveryABC() throws Exception {
        final int COUNT = 10000;
        NormalDeliveryA normalDeliveryA = new NormalDeliveryA(sBus, COUNT);
        NormalDeliveryB normalDeliveryB = new NormalDeliveryB(sBus, COUNT);
        NormalDeliveryC normalDeliveryC = new NormalDeliveryC(sBus, COUNT);
        for (int i = 0; i < COUNT; i++) {
            sBus.post(new EventA());
            sBus.post(new EventB());
            sBus.post(new EventC());
        }
        assertTrue(normalDeliveryA.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryB.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryC.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryA.terminateAndWait(2000));
        assertTrue(normalDeliveryB.terminateAndWait(2000));
        assertTrue(normalDeliveryC.terminateAndWait(2000));
    }

    @Test
    public void stressByManyEventsDeliveryABC_ABC() throws Exception {
        final int COUNT = 10000;
        NormalDeliveryA normalDeliveryA = new NormalDeliveryA(sBus, COUNT);
        NormalDeliveryB normalDeliveryB = new NormalDeliveryB(sBus, COUNT);
        NormalDeliveryC normalDeliveryC = new NormalDeliveryC(sBus, COUNT);
        NormalDeliveryABC normalDeliveryABC1 = new NormalDeliveryABC(sBus, COUNT);
        NormalDeliveryABC normalDeliveryABC2 = new NormalDeliveryABC(sBus, COUNT);
        NormalDeliveryABC normalDeliveryABC3 = new NormalDeliveryABC(sBus, COUNT);
        NormalDeliveryABC normalDeliveryABC4 = new NormalDeliveryABC(sBus, COUNT);
        NormalDeliveryABC normalDeliveryABC5 = new NormalDeliveryABC(sBus, COUNT);
        for (int i = 0; i < COUNT; i++) {
            sBus.post(new EventA());
            sBus.post(new EventB());
            sBus.post(new EventC());
        }
        assertTrue(normalDeliveryA.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryB.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryC.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryABC1.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryABC2.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryABC3.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryABC4.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryABC5.eventCount.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(normalDeliveryA.terminateAndWait(2000));
        assertTrue(normalDeliveryB.terminateAndWait(2000));
        assertTrue(normalDeliveryC.terminateAndWait(2000));
        assertTrue(normalDeliveryABC1.terminateAndWait(2000));
        assertTrue(normalDeliveryABC2.terminateAndWait(2000));
        assertTrue(normalDeliveryABC3.terminateAndWait(2000));
        assertTrue(normalDeliveryABC4.terminateAndWait(2000));
        assertTrue(normalDeliveryABC5.terminateAndWait(2000));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Multi-threaded hell
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static final int MESS_TOTAL_TIMEOUT_SEC        = 60;
    private static final int MESS_TOTAL_COUNT              = 100;
    private static final int MESS_MAX_COUNT_PER_NEXT       = 10;
    private static final int MESS_MAX_COUNT_PER_BUS_THREAD = 1;
    private static final int MESS_MAX_COUNT_PER_GEN_THREAD = 1;
    private static final int MESS_MAX_GEN_DELAY_MS         = 1;

    private final Random mRandom = new Random();

    private int            mMessCount;
    private CountDownLatch mMessCountDown;
    private AtomicInteger  mCurrentThreadsCount;
    private AtomicInteger  mTotalThreadsCount;

    private int randomCount(int max) {
        return mRandom.nextInt(max) + 1;
    }

    private int randomCount(int max, int limit) {
        int ret = randomCount(max);
        if (ret > limit) {
            ret = limit;
        }
        return ret;
    }

    private synchronized int randomRemainingCount(int max) {
        int cnt = randomCount(max, mMessCount);
        mMessCount -= cnt;
        return cnt;
    }

    private static class MessEventA {

        private final MessBusThread mMessBusThread;

        public MessEventA(MessBusThread messBusThread) {
            mMessBusThread = messBusThread;
        }

        public MessBusThread getMessBusThread() {
            return mMessBusThread;
        }
    }


    private static class MessEventB {

        private final MessBusThread mMessBusThread;

        public MessEventB(MessBusThread messBusThread) {
            mMessBusThread = messBusThread;
        }

        public MessBusThread getMessBusThread() {
            return mMessBusThread;
        }
    }


    private static class MessEventC {

        private final MessBusThread mMessBusThread;

        public MessEventC(MessBusThread messBusThread) {
            mMessBusThread = messBusThread;
        }

        public MessBusThread getMessBusThread() {
            return mMessBusThread;
        }
    }


    public class MessGenThread extends Thread {

        private final Bus           mBus;
        private       int           mCount;
        private final MessBusThread mMessBusThread;

        public MessGenThread(Bus bus, MessBusThread messBusThread, int numberOfEventsToProduce) {
            super();
            mBus = bus;
            mMessBusThread = messBusThread;
            mCount = numberOfEventsToProduce;
            setName("MessGenThread:" + numberOfEventsToProduce);
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            mCurrentThreadsCount.incrementAndGet();
            mTotalThreadsCount.incrementAndGet();
            while (mCount > 0) {
                mCount--;
                try {
                    Thread.sleep(randomCount(MESS_MAX_GEN_DELAY_MS));
                } catch (InterruptedException e) {
                    break;
                }
                switch (randomCount(3)) {
                    case 1:
                        mBus.post(new MessEventA(mMessBusThread));
                        break;
                    case 2:
                        mBus.post(new MessEventB(mMessBusThread));
                        break;
                    default:
                        mBus.post(new MessEventC(mMessBusThread));
                        break;
                }
            }
            mCurrentThreadsCount.decrementAndGet();
        }
    }


    public class MessBusThread extends BusThread {

        private int mCount;

        public MessBusThread(Bus bus, int numberOfEventsToProduceAndConsume) throws InterruptedException {
            super(bus);
            mCount = numberOfEventsToProduceAndConsume;
            setName("MessBusThread");
            setDaemon(true);
            startAndWait();
            while (numberOfEventsToProduceAndConsume > 0) {
                int cnt = randomCount(MESS_MAX_COUNT_PER_GEN_THREAD, numberOfEventsToProduceAndConsume);
                numberOfEventsToProduceAndConsume -= cnt;
                new MessGenThread(sBus, this, cnt);
            }
        }

        private void next() throws InterruptedException {
            int count = randomRemainingCount(MESS_MAX_COUNT_PER_NEXT);
            List<Integer> counts = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append(count).append(",");
            while (count > 0) {
                int cnt = randomCount(MESS_MAX_COUNT_PER_BUS_THREAD, count);
                counts.add(cnt);
                sb.append(cnt).append(",");
                count -= cnt;
            }
            //System.out.format("MessBusThread.next(%s)\n", sb.toString());
            for (Integer cnt : counts) {
                new MessBusThread(sBus, cnt);
            }
        }

        private void dec() throws InterruptedException {
            mMessCountDown.countDown();
            if (--mCount <= 0) {
                next();
                terminate();
            }
        }

        @Subscribe
        public void onEventA(MessEventA event) throws InterruptedException {
            if (event.getMessBusThread() == this) {
                dec();
            }
        }

        @Subscribe
        public void onEventB(MessEventB event) throws InterruptedException {
            if (event.getMessBusThread() == this) {
                dec();
            }
        }

        @Subscribe
        public void onEventC(MessEventC event) throws InterruptedException {
            if (event.getMessBusThread() == this) {
                dec();
            }
        }

        @Override
        public void run() {
            mCurrentThreadsCount.incrementAndGet();
            mTotalThreadsCount.incrementAndGet();
            super.run();
            mCurrentThreadsCount.decrementAndGet();
        }
    }

    @Test
    public void multiThreadedMess() throws Exception {
        // Prepare it
        mCurrentThreadsCount = new AtomicInteger(0);
        mTotalThreadsCount = new AtomicInteger(0);
        mMessCountDown = new CountDownLatch(MESS_TOTAL_COUNT);
        mMessCount = MESS_TOTAL_COUNT;
        // Start it
        new MessBusThread(sBus, randomRemainingCount(MESS_MAX_COUNT_PER_BUS_THREAD));
        // Wait all events produced and consumed
        assertTrue(mMessCountDown.await(MESS_TOTAL_TIMEOUT_SEC, TimeUnit.SECONDS));
        // Wait all threads finished with the timeout
        long ts = System.currentTimeMillis() + 10000;
        while (mCurrentThreadsCount.get() > 0 && System.currentTimeMillis() < ts) {
            Thread.sleep(100);
        }
        assertEquals(0, mCurrentThreadsCount.get());
        // Display how many threads we had here in this game
        System.out.format("Total threads count: %d\n", mTotalThreadsCount.get());
    }
}
