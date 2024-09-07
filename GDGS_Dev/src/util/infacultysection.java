package util;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import javax.swing.SwingConstants;

public class infacultysection {
    public infacultysection() {
        JFrame frame = new JFrame("Faculty Section");
        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new infacultysection();
    }
}
