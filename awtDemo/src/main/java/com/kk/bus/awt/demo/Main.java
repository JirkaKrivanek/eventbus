package com.kk.bus.awt.demo;


/**
 * @author Jiri Krivanek
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormMain();
            }
        });
    }
}
