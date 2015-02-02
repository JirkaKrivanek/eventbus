package com.kk.bus.awt.demo;


import com.kk.bus.Subscribe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Test form.
 *
 * @author Jiri Krivanek
 */
public class FormMain {

    private static class EventCount {}


    private JFrame     mJFrame;
    private JPanel     mPanel;
    private JButton    mIncrementButton;
    private JTextField mCounter;

    public FormMain() {
        mJFrame = new JFrame("AWT Bus Test");
        mJFrame.setContentPane(mPanel);
        mJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mJFrame.pack();
        mJFrame.setVisible(true);
        mIncrementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventBus.post(new EventCount());
            }
        });
        EventBus.register(this);
    }

    @Subscribe
    public void onEventCount(EventCount event) {
        try {
            int cnt = Integer.parseInt(mCounter.getText());
            mCounter.setText(Integer.toString(cnt + 1));
        } catch (NumberFormatException e) {
            mCounter.setText("1");
        }
    }
}
