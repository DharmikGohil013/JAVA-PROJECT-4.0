package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogoutPage {

    JFrame frame;

    public LogoutPage() {
        frame = new JFrame("Logout");

        // Create confirmation label
        JLabel logoutLabel = new JLabel("Are you sure you want to logout?");
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 20));
        logoutLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create Yes and No buttons
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        yesButton.setPreferredSize(new Dimension(100, 40));
        noButton.setPreferredSize(new Dimension(100, 40));

        // Set button actions
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the logout process here
                // Example: close the current window and return to the login page
                frame.dispose();
                new index();  // Redirect back to login screen
            }
        });

        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the logout confirmation window (cancel logout)
                frame.dispose();
            }
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Layout settings for the frame
        frame.setLayout(new BorderLayout());
        frame.add(logoutLabel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);  // Center the frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LogoutPage();  // Open the logout page
    }
}
