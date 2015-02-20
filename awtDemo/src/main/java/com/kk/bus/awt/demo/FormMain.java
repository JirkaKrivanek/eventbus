package com.kk.bus.awt.demo;


import com.kk.bus.Subscribe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mPanel = new JPanel();
        mPanel.setLayout(new GridBagLayout());
        final JLabel label1 = new JLabel();
        label1.setText("Event counter:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 0);
        mPanel.add(label1, gbc);
        mCounter = new JTextField();
        mCounter.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        mPanel.add(mCounter, gbc);
        mIncrementButton = new JButton();
        mIncrementButton.setText("Increment");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mIncrementButton, gbc);
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return mPanel;
    }
}
