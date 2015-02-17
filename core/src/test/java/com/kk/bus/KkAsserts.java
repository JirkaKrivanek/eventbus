package com.kk.bus;


public class KkAsserts {

    public static void assertHigherOrEqual(long higherOrEqual, long than) {
        if (higherOrEqual < than) {
            throw new AssertionError(String.format("%d >= %d", higherOrEqual, than));
        }
    }

}
