package com.kk.bus;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class RegisteredClassesTest {

    private RegisteredClasses mRegisteredClasses;

    @Before
    public void setUp() throws Exception {
        mRegisteredClasses = new RegisteredClasses();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static class HandlerA {}


    private static class HandlerB {}

    @Test
    public void testGetRegisteredClass() throws Exception {
        HandlerA handlerA = new HandlerA();
        RegisteredClass registeredClassForHandlerA = mRegisteredClasses.getRegisteredClass(handlerA, false);
        assertNull(registeredClassForHandlerA);
        registeredClassForHandlerA = mRegisteredClasses.getRegisteredClass(handlerA, true);
        assertNotNull(registeredClassForHandlerA);
        HandlerB handlerB = new HandlerB();
        RegisteredClass registeredClassForHandlerB = mRegisteredClasses.getRegisteredClass(handlerB, false);
        assertNull(registeredClassForHandlerB);
        registeredClassForHandlerB = mRegisteredClasses.getRegisteredClass(handlerB, true);
        assertNotNull(registeredClassForHandlerB);
        assertNotSame(registeredClassForHandlerA, registeredClassForHandlerB);
    }
}
