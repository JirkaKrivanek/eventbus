package com.kk.bus;


import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RegisteredClassTest {

    private static class EventA {}


    private static class EventAA extends EventA {}


    private static class EventAAA extends EventAA {}


    private static class EventB {}


    private static interface EventInterface {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: Constructor(s)
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassConstructor {}

    public void constructors() {
        RegisteredClass registeredClass = new RegisteredClass(ClassConstructor.class);
        assertNotNull(registeredClass);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: hasAnySubscribers()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassHasAnySubscribers_None {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }


    private static class ClassHasAnySubscribers_One {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }
    }


    private static class ClassHasAnySubscribers_Two {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }

    @Test
    public void hasAnySubscribers() {
        RegisteredClass registeredClass = new RegisteredClass(ClassHasAnySubscribers_None.class);
        assertFalse(registeredClass.hasAnySubscribers());
        //
        registeredClass = new RegisteredClass(ClassHasAnySubscribers_One.class);
        assertTrue(registeredClass.hasAnySubscribers());
        //
        registeredClass = new RegisteredClass(ClassHasAnySubscribers_Two.class);
        assertTrue(registeredClass.hasAnySubscribers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: getSubscriberMethods()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class GetSubscribedMethods_None {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }


    private static class GetSubscribedMethods_One {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }
    }


    private static class GetSubscribedMethods_Two {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }

    @Test
    public void getSubscribedMethods() {
        RegisteredClass registeredClass = new RegisteredClass(GetSubscribedMethods_None.class);
        Set<Method> methods = registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0));
        assertNull(methods);
        methods = registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0));
        assertNull(methods);
        //
        registeredClass = new RegisteredClass(GetSubscribedMethods_One.class);
        methods = registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0));
        assertNotNull(methods);
        assertEquals(1, methods.size());
        methods = registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0));
        assertNull(methods);
        //
        registeredClass = new RegisteredClass(GetSubscribedMethods_Two.class);
        methods = registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0));
        assertNotNull(methods);
        assertEquals(1, methods.size());
        methods = registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0));
        assertNotNull(methods);
        assertEquals(1, methods.size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: getEventSubscribers()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassGetSubscribedEventClasses_None {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }


    private static class ClassGetSubscribedEventClasses_One {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }
    }


    private static class ClassGetSubscribedEventClasses_Two {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }

    @Test
    public void getSubscribedEventClasses() {
        RegisteredClass registeredClass = new RegisteredClass(ClassGetSubscribedEventClasses_None.class);
        assertNull(registeredClass.getEventSubscribers());
        //
        registeredClass = new RegisteredClass(ClassGetSubscribedEventClasses_One.class);
        Set<Subscriber> subscribers = registeredClass.getEventSubscribers();
        assertNotNull(subscribers);
        assertEquals(1, subscribers.size());
        assertTrue(subscribers.contains(new Subscriber(EventA.class, 0)));
        //
        registeredClass = new RegisteredClass(ClassGetSubscribedEventClasses_Two.class);
        subscribers = registeredClass.getEventSubscribers();
        assertNotNull(subscribers);
        assertEquals(2, subscribers.size());
        assertTrue(subscribers.contains(new Subscriber(EventA.class, 0)));
        assertTrue(subscribers.contains(new Subscriber(EventB.class, 0)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: hasAnyProducers()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class HasAnyProducers_None {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }


    private static class HasAnyProducers_One {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }
    }


    private static class HasAnyProducers_Two {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }

    @Test
    public void hasAnyProducers() {
        RegisteredClass registeredClass = new RegisteredClass(HasAnyProducers_None.class);
        assertFalse(registeredClass.hasAnyProducers());
        //
        registeredClass = new RegisteredClass(HasAnyProducers_One.class);
        assertTrue(registeredClass.hasAnyProducers());
        //
        registeredClass = new RegisteredClass(HasAnyProducers_Two.class);
        assertTrue(registeredClass.hasAnyProducers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: getProducedEventClasses()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class GetProducedEventClasses_None {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }


    private static class GetProducedEventClasses_One {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }
    }


    private static class GetProducedEventClasses_Two {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }

    @Test
    public void getProducedEventClasses() {
        RegisteredClass registeredClass = new RegisteredClass(GetProducedEventClasses_None.class);
        Set<Class<?>> classes = registeredClass.getProducedEventClasses();
        assertNull(classes);
        //
        registeredClass = new RegisteredClass(GetProducedEventClasses_One.class);
        classes = registeredClass.getProducedEventClasses();
        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertTrue(classes.contains(EventA.class));
        //
        registeredClass = new RegisteredClass(GetProducedEventClasses_Two.class);
        assertTrue(registeredClass.hasAnyProducers());
        classes = registeredClass.getProducedEventClasses();
        assertNotNull(classes);
        assertEquals(2, classes.size());
        assertTrue(classes.contains(EventA.class));
        assertTrue(classes.contains(EventB.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods: getProducerMethod()
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    class ClassGetProducerMethod {

        public void fakePubA(EventA event) {
        }

        public void fakePubB(EventB event) {
        }

        public void fakePubC(int event) {
        }

        public int fakePubD() {
            return 0;
        }

        protected void fakeProA(EventA event) {
        }

        protected void fakeProB(EventB event) {
        }

        protected void fakeProC(int event) {
        }

        protected int fakeProD() {
            return 0;
        }

        void fakePproA(EventA event) {
        }

        void fakePproB(EventB event) {
        }

        void fakePproC(int event) {
        }

        int fakePproD() {
            return 0;
        }

        private void fakePriA(EventA event) {
        }

        private void fakePriB(EventB event) {
        }

        private void fakePriC(int event) {
        }

        private int fakePriD() {
            return 0;
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventAA produceEventAA() {
            return null;
        }

        @Produce
        public EventAAA produceEventAAA() {
            return null;
        }
    }

    @Test
    public void getProducerMethod() {
        RegisteredClass registeredClass = new RegisteredClass(ClassGetProducerMethod.class);
        assertNotNull(registeredClass.getProducerMethod(EventA.class));
        assertNotNull(registeredClass.getProducerMethod(EventAA.class));
        assertNotNull(registeredClass.getProducerMethod(EventAAA.class));
        assertNull(registeredClass.getProducerMethod(EventB.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Functionality: Subscribers
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoSubscribersA {}

    @Test
    public void testClassNoSubscribersA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoSubscribersA.class);
        assertFalse(registeredClass.hasAnySubscribers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoSubscribersB {

        public void fakeMethod() {
        }
    }

    @Test
    public void testClassNoSubscribersB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoSubscribersB.class);
        assertFalse(registeredClass.hasAnySubscribers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoSubscribersC {

        public void fakeMethod() {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassNoSubscribersC() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoSubscribersC.class);
        assertFalse(registeredClass.hasAnySubscribers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventA {

        @Subscribe
        public void onEventA(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberEventA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventA.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(1, registeredClass.getEventSubscribers().size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventFA {

        public void fakeMethod() {
        }

        @Subscribe
        public void onEventA(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberEventFA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventFA.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(1, registeredClass.getEventSubscribers().size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventAA {

        @Subscribe
        public void onEventA1(EventA event) {
        }

        @Subscribe
        public void onEventA2(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberEventAA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventAA.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(1, registeredClass.getEventSubscribers().size());
        assertEquals(2, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventFAA {

        public void fakeMethod() {
        }

        @Subscribe
        public void onEventA1(EventA event) {
        }

        @Subscribe
        public void onEventA2(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberEventFAA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventFAA.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(1, registeredClass.getEventSubscribers().size());
        assertEquals(2, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventAB {

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }

    @Test
    public void testClassSubscriberEventAB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventAB.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(2, registeredClass.getEventSubscribers().size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventFAB {

        public void fakeMethod() {
        }

        @Subscribe
        public void onEventA(EventA event) {
        }

        @Subscribe
        public void onEventB(EventB event) {
        }
    }

    @Test
    public void testClassSubscriberEventFAB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventFAB.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(2, registeredClass.getEventSubscribers().size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
        assertEquals(1, registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberEventFAABB {

        public void fakeMethod() {
        }

        @Subscribe
        public void onEventA1(EventA event) {
        }

        @Subscribe
        public void onEventA2(EventA event) {
        }

        @Subscribe
        public void onEventB1(EventB event) {
        }

        @Subscribe
        public void onEventB2(EventB event) {
        }
    }

    @Test
    public void testClassSubscriberEventFAABB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassSubscriberEventFAABB.class);
        assertTrue(registeredClass.hasAnySubscribers());
        assertNotNull(registeredClass.getEventSubscribers());
        assertEquals(2, registeredClass.getEventSubscribers().size());
        assertEquals(2, registeredClass.getSubscriberMethods(new Subscriber(EventA.class, 0)).size());
        assertEquals(2, registeredClass.getSubscriberMethods(new Subscriber(EventB.class, 0)).size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberErrorTooManyParams {

        @Subscribe
        public void onEventA(EventA event, int count) {
        }
    }

    @Test
    public void testClassSubscriberErrorTooManyParams() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassSubscriberErrorTooManyParams.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberErrorPrivate {

        @Subscribe
        private void onEventA(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberErrorPrivate() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassSubscriberErrorPrivate.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberErrorProtected {

        @Subscribe
        protected void onEventA(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberErrorProtected() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassSubscriberErrorProtected.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassSubscriberErrorPackageProtected {

        @Subscribe
        void onEventA(EventA event) {
        }
    }

    @Test
    public void testClassSubscriberErrorPackageProtected() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassSubscriberErrorPackageProtected.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Functionality: Producers
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoProducerA {}

    @Test
    public void testClassNoProducerA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoProducerA.class);
        assertFalse(registeredClass.hasAnyProducers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoProducerB {

        public void fakeMethod() {
        }
    }

    @Test
    public void testClassNoProducerB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoProducerB.class);
        assertFalse(registeredClass.hasAnyProducers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassNoProducerC {

        public void fakeMethod() {
        }

        @Subscribe
        public void onEventA1(EventA event) {
        }

        @Subscribe
        public void onEventA2(EventA event) {
        }

        @Subscribe
        public void onEventB1(EventB event) {
        }

        @Subscribe
        public void onEventB2(EventB event) {
        }
    }

    @Test
    public void testClassNoProducerC() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassNoProducerC.class);
        assertFalse(registeredClass.hasAnyProducers());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerEventA {

        @Produce
        public EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerEventA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassProducerEventA.class);
        assertTrue(registeredClass.hasAnyProducers());
        assertNotNull(registeredClass.getProducerMethod(EventA.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerEventFA {

        public void fakeMethod() {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerEventFA() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassProducerEventFA.class);
        assertTrue(registeredClass.hasAnyProducers());
        assertNotNull(registeredClass.getProducerMethod(EventA.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerEventAB {

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }

    @Test
    public void testClassProducerEventAB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassProducerEventAB.class);
        assertTrue(registeredClass.hasAnyProducers());
        assertNotNull(registeredClass.getProducerMethod(EventA.class));
        assertNotNull(registeredClass.getProducerMethod(EventB.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerEventFAB {

        public void fakeMethod() {
        }

        @Produce
        public EventA produceEventA() {
            return null;
        }

        @Produce
        public EventB produceEventB() {
            return null;
        }
    }

    @Test
    public void testClassProducerEventFAB() throws Exception {
        RegisteredClass registeredClass = new RegisteredClass(ClassProducerEventFAB.class);
        assertTrue(registeredClass.hasAnyProducers());
        assertNotNull(registeredClass.getProducerMethod(EventA.class));
        assertNotNull(registeredClass.getProducerMethod(EventB.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorUnexpectedParameters {

        @Produce
        public EventA produceEventA(int count) {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorUnexpectedParameters() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorUnexpectedParameters.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorNoResult {

        @Produce
        public void produceEventA() {
        }
    }

    @Test
    public void testClassProducerErrorNoResult() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorNoResult.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorVoidResult {

        @Produce
        public Void produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorVoidResult() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorVoidResult.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorPrivate {

        @Produce
        private EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorPrivate() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorPrivate.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorProtected {

        @Produce
        protected EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorProtected() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorProtected.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorPackageProtected {

        @Produce
        EventA produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorPackageProtected() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorPackageProtected.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNull(exception);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class ClassProducerErrorDuplicate {

        @Produce
        public EventA produceEventA1() {
            return null;
        }

        @Produce
        public EventA produceEventA2() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorDuplicate() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorDuplicate.class);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}
