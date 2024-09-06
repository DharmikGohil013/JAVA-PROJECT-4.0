package util;

import javax.swing.*;
import java.awt.*;

public class inadminpage {
    public inadminpage() {
        JFrame frame = new JFrame("Admin Section");
        

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new inadminpage();
    }
}
