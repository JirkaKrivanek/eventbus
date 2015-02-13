package com.kk.bus;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeliveryContextManagersTest {

    @Test
    public void testAllInOneShot_toEnsureProperOperationsOrder_toSimplifyTestConditions() throws Exception {
        DeliveryContext deliveryContext1 = mock(DeliveryContext.class);
        DeliveryContext deliveryContext2 = mock(DeliveryContext.class);
        DeliveryContextManager deliveryContextManager = mock(DeliveryContextManager.class);
        when(deliveryContextManager.getCurrentDeliveryContext()).thenReturn(deliveryContext1);
        when(deliveryContextManager.isCurrentDeliveryContext(null)).thenReturn(false);
        when(deliveryContextManager.isCurrentDeliveryContext(deliveryContext1)).thenReturn(true);
        when(deliveryContextManager.isCurrentDeliveryContext(deliveryContext2)).thenReturn(false);

        // No delivery context manager registered yet
        try {
            DeliveryContextManagers.getCurrentDeliveryContext();
            assertTrue(false);
        } catch (IllegalStateException e) {
        }
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(null));
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext1));
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext2));

        // Register delivery context manager
        DeliveryContextManagers.registerDeliveryContextManager(deliveryContextManager);

        // Duplicate registration must not throw exception
        DeliveryContextManagers.registerDeliveryContextManager(deliveryContextManager);

        // Delivery context manager registered
        assertSame(deliveryContext1, DeliveryContextManagers.getCurrentDeliveryContext());
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(null));
        assertTrue(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext1));
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext2));

        // Unregister delivery context manager
        DeliveryContextManagers.unregisterDeliveryContextManager(deliveryContextManager);

        // Duplicate unregistration must not throw exception
        DeliveryContextManagers.unregisterDeliveryContextManager(deliveryContextManager);

        // No delivery context manager registered after all
        try {
            DeliveryContextManagers.getCurrentDeliveryContext();
            assertTrue(false);
        } catch (IllegalStateException e) {
        }
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(null));
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext1));
        assertFalse(DeliveryContextManagers.isCurrentDeliveryContext(deliveryContext2));
    }
}
