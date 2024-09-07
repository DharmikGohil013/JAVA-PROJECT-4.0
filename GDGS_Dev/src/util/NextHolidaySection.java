package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NextHolidaySection {

    JFrame frame;

    public NextHolidaySection() {
        frame = new JFrame("Next Holiday Information");
        frame.setLayout(new BorderLayout());  // Use BorderLayout for main frame

        // Create panel for department buttons with GridBagLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around buttons
        gbc.anchor = GridBagConstraints.CENTER;

        // Create buttons for each department
        JButton itButton = new JButton("IT");
        JButton ceButton = new JButton("CE");
        JButton cdButton = new JButton("CD");
        JButton csButton = new JButton("CS");

        // Set button properties
        Dimension buttonSize = new Dimension(250, 60);
        Font buttonFont = new Font("Arial", Font.BOLD, 18);
        Color buttonColor = new Color(51, 153, 255);  // Light blue color
        Color textColor = Color.WHITE;

        JButton[] buttons = {itButton, ceButton, cdButton, csButton};

        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false); // Remove button border on click
        }

        // Add buttons to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(itButton, gbc);
        
        gbc.gridy = 1;
        buttonPanel.add(ceButton, gbc);
        
        gbc.gridy = 2;
        buttonPanel.add(cdButton, gbc);
        
        gbc.gridy = 3;
        buttonPanel.add(csButton, gbc);

        // Create Back button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(255, 69, 69));  // Red color for visibility
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false); // Remove button border on click

        // Add action listener for Back button
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current window
            new MainMenu(); // Replace with the actual previous window class
        });

        // Add components to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);

        // Button actions to open HolidayInfoPage for respective department
        itButton.addActionListener(e -> openHolidayInfoPage("IT"));
        ceButton.addActionListener(e -> openHolidayInfoPage("CE"));
        cdButton.addActionListener(e -> openHolidayInfoPage("CD"));
        csButton.addActionListener(e -> openHolidayInfoPage("CS"));

        // Frame settings
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application
        frame.setVisible(true);
    }

    private void openHolidayInfoPage(String department) {
        frame.dispose(); // Close the current window
        new HolidayInfoPage(department); // Open HolidayInfoPage for the selected department
    }

    public static void main(String[] args) {
        new NextHolidaySection();
    }
}
