package util;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import javax.swing.SwingConstants;

public class infacultysection {
    public infacultysection() {
        JFrame frame = new JFrame("Hello World Page");
        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(label, BorderLayout.CENTER);

        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new infacultysection();
    }
}
