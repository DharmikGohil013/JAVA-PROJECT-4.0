package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentDetailsSection {

    JFrame frame;

    public StudentDetailsSection() {
        frame = new JFrame("Student Details");
        frame.setLayout(new BorderLayout());

        // Create buttons for each department
        JButton itButton = new JButton("IT");
        JButton ceButton = new JButton("CE");
        JButton csButton = new JButton("CS");
        JButton cdButton = new JButton("CD");
        JButton backButton = new JButton("Back");

        // Set button properties
        Font buttonFont = new Font("Arial", Font.PLAIN, 20);
        Color buttonColor = new Color(51, 153, 255);
        Color textColor = Color.WHITE;

        JButton[] buttons = {itButton, ceButton, csButton, cdButton};
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(150, 50));
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(textColor);
            button.setFocusPainted(false);
        }

        backButton.setPreferredSize(new Dimension(150, 50));
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(255, 51, 51)); // Red color for back button
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        // Create panel for department buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.add(itButton);
        buttonPanel.add(ceButton);
        buttonPanel.add(csButton);
        buttonPanel.add(cdButton);

        // Add components to frame
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);

        // Button action listeners
        itButton.addActionListener(e -> showStudentDetails("IT"));
        ceButton.addActionListener(e -> showStudentDetails("CE"));
        csButton.addActionListener(e -> showStudentDetails("CS"));
        cdButton.addActionListener(e -> showStudentDetails("CD"));
        backButton.addActionListener(e -> {
            frame.dispose();  // Close the current StudentDetailsSection window
            // Implement code to go back to the previous section
            // For example: new AdminSection(); or any other previous screen
        });

        // Frame settings
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void showStudentDetails(String department) {
        new StudentDetailsTable(department); // Open table view for selected department
    }

    public static void main(String[] args) {
        new StudentDetailsSection(); // Display department selection section
    }
}
