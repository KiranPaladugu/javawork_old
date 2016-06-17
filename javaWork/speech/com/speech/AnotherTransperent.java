package com.speech;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class AnotherTransperent
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("The Frame");
        frame.setSize(300, 300);
        frame.setLocation(100, 200);

        JWindow window = new JWindow();
        window.setSize(300, 300);
        window.setLocation(500, 200);       
//        AWTUtilities.setWindowOpacity(window, 0.5f);
//        AWTUtilities.setWindowOpacity(frame, 0.5f);

        JPanel pan = new JPanel();
        JPanel pan2 = new JPanel();

        window.add(pan, "Center");
        frame.getContentPane().add(pan2);

        pan.setLayout(new FlowLayout());
        pan.add(new JButton("Hello"));

        pan2.setLayout(new FlowLayout());
        pan2.add(new JButton("Hello"));

        frame.setVisible(true);
        window.setVisible(true);

    }
}