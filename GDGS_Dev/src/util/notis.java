package util;

import javax.swing.*;
import java.awt.*;

public class notis {

    JFrame frame;

    public notis() {
        frame = new JFrame("notis");
        frame.setLayout(new BorderLayout());  // Use BorderLayout for main frame

        // Create a panel to hold the "Coming Soon" message
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout()); // Center the message
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Padding around the message
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and style the "Coming Soon" label
        JLabel comingSoonLabel = new JLabel("Coming Soon");
        comingSoonLabel.setFont(new Font("Arial", Font.BOLD, 48));
        comingSoonLabel.setForeground(new Color(255, 69, 69)); // Red color for visibility

        messagePanel.add(comingSoonLabel, gbc);
        
        // Create Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(51, 153, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove button border on click

        // Add action listener for Back button
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window // Replace with the actual previous window class
        });

        // Create panel for Back button and set its layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 20)); // Align to the right with padding
        buttonPanel.add(backButton);
        
        // Add components to the frame
        frame.add(messagePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new notis();  // Test the section
    }
}

