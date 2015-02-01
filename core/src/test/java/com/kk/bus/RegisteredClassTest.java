package com.kk.bus;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RegisteredClassTest {

    private static class EventA {}


    private static class EventB {}


    private static interface EventInterface {}

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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(1, registeredClass.getSubscribedEvents().size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventA.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(1, registeredClass.getSubscribedEvents().size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventA.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(1, registeredClass.getSubscribedEvents().size());
        assertEquals(2, registeredClass.getSubscribedMethods(EventA.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(1, registeredClass.getSubscribedEvents().size());
        assertEquals(2, registeredClass.getSubscribedMethods(EventA.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(2, registeredClass.getSubscribedEvents().size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventA.class).size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventB.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(2, registeredClass.getSubscribedEvents().size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventA.class).size());
        assertEquals(1, registeredClass.getSubscribedMethods(EventB.class).size());
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
        assertNotNull(registeredClass.getSubscribedEvents());
        assertEquals(2, registeredClass.getSubscribedEvents().size());
        assertEquals(2, registeredClass.getSubscribedMethods(EventA.class).size());
        assertEquals(2, registeredClass.getSubscribedMethods(EventB.class).size());
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


    private static class ClassSubscriberErrorInterface {

        @Subscribe
        public void onEventA(EventInterface event) {
        }
    }

    @Test
    public void testClassSubscriberErrorInterface() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassSubscriberErrorInterface.class);
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
        assertNotNull(exception);
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
        assertNotNull(exception);
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
        assertNotNull(exception);
    }

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


    private static class ClassProducerErrorInterfaceResult {

        @Produce
        public EventInterface produceEventA() {
            return null;
        }
    }

    @Test
    public void testClassProducerErrorInterfaceResult() throws Exception {
        IllegalArgumentException exception = null;
        try {
            new RegisteredClass(ClassProducerErrorInterfaceResult.class);
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
        assertNotNull(exception);
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
        assertNotNull(exception);
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
        assertNotNull(exception);
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
